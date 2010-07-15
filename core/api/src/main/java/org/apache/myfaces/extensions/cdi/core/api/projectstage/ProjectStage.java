/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.extensions.cdi.core.api.projectstage;

import javax.enterprise.inject.Typed;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Logger;

/**
 * <p>This class is the base of all ProjectStages. A ProjectStage
 * identifies the environment the application currently runs in.
 * It provides the same functionality as the JSF-2 ProjectStage
 * but has a few additional benefits:
 * <ul>
 *  <li>it also works for JSF-1.0, JSF-1.1 and JSF-1.2 applications</li>
 *  <li>it also works in pure backends and unit tests without any faces-api</li>
 *  <li>it is dynamic. Everyone can add own ProjectStages!</p>
 * </ul>
 * </p>
 *
 * <p>Technically this is kind of a 'dynamic enum'.</p>
 * <p>The following ProjectStages are provided by default</p>
 * <ul>
 *  <li>UnitTest</li>
 *  <li>Development</li>
 *  <li>SystemTest</li>
 *  <li>IntegrationTest</li>
 *  <li>Staging</li>
 *  <li>Production</li>
 * </ul>
 *
 * <p>The following resolution mechanism is used to determine the current ProjectStage:
 * <ul>
 *  <li>TODO specify!</li>
 * </ul>
 * </p>
 *
 * <p>Adding a new ProjectStage is done via the
 * {@link java.util.ServiceLoader} mechanism. A class deriving from {@link ProjectStage}
 * must be provided and used for creating a single static instance of it. The name of 
 * the class should be the name of the ProjectStage prefixed with 'C', 
 * e.g. &quot;CUnitTest&quot;</p>
 *
 * <p>Custom ProjectStages can be implemented by writing anonymous ProjectStage
 * members into a registered {@link ProjectStageHolder} as shown in the following
 * sample:</p>
 * <pre>
 * package org.apache.myfaces.extensions.cdi.test.api.projectstage;
 * public class MyProjectStages implements ProjectStageHolder {
 *     public static final class CMyOwnProjectStage extends ProjectStage {};
 *     public static final CMyOwnProjectStage MyOwnProjectStage = new CMyOwnProjectStage();
 *    
 *     public static final class CMyOtherProjectStage extends ProjectStage {};
 *     public static final CMyOtherProjectStage MyOtherProjectStage = new CMyOtherProjectStage();
 * }
 * </pre>
 * <p>For activating those projectstages, you have to register this ProjectStageHolder class
 * to get picked up via the java.util.ServiceLoader mechanism. Simply create a file
 * <pre>
 * META-INF/services/org.apache.myfaces.extensions.cdi.core.api.projectstage.ProjectStageHolder
 * </pre>
 * which contains the fully qualified class name of custom ProjectStageHolder implementation:
 * <pre>
 * # this class now get's picked up by java.util.ServiceLoader
 * org.apache.myfaces.extensions.cdi.core.test.api.projectstage.MyProjectStages
 * </pre>
 * </p>
 * <p>You can use your own ProjectStages exactly the same way as all the ones provided
 * by the system:
 * <pre>
 * ProjectStage myOwnPs = ProjectStage.valueOf("MyOwnProjectStage");
   if (myOwnPs.equals(MyOwnProjectStage.MyOwnProjectStage)) ... 
 * </pre>
 *
 */
@Typed()
public abstract class ProjectStage implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * This map contains a static map with all registered projectStages.
     *
     * We don't need to use a ConcurrentHashMap because writing to it will
     * only be performed in the static initializer block which is guaranteed
     * to be atomic by the VM spec. 
     */
    private static Map<String, ProjectStage> projectStages = new HashMap<String, ProjectStage>();

    /**
     * All the registered ProjectStage values.
     * We don't need to make this volatile because of the classloader guarantees of
     * the VM.  
     */
    private static ProjectStage[] values = null;

    /**
     * logger for the ProjectStage
     */
    private static final Logger log = Logger.getLogger(ProjectStage.class.getName());

    
    /**
     * The static initializer block will register all custom ProjectStages
     * by simply touching their classes due loding it with the
     * {@link java.util.ServiceLoader}.
     */
    static
    {
        ServiceLoader<ProjectStageHolder> psSl = ServiceLoader.load(ProjectStageHolder.class);
        Iterator<ProjectStageHolder> psIt = psSl.iterator();
        while (psIt.hasNext())
        {
            ProjectStageHolder psH = psIt.next();
            log.fine("registering ProjectStages from ProjectStageHolder " + psH.getClass().getName());
        }
    }

    
    /** the name of the ProjectStage*/
    private String psName;

    /**
     * The protected constructor will register the given ProjectStage via its name.
     * The name is returned by the {@link #toString()} method of the ProjectStage.
     */
    protected ProjectStage()
    {
        String psNameIn = this.getClass().getSimpleName(); 
        psName = psNameIn.substring(1);

        if (!projectStages.containsKey(psNameIn))
        {
            projectStages.put(psName, this);
        }
        else
        {
            throw new IllegalArgumentException("ProjectStage with name " + psNameIn + " already exists!");
        }

        // we cannot do this in the static block since it's not really deterministic
        // when all ProjectStages got resolved.
        values = projectStages.values().toArray(new ProjectStage[ projectStages.size() ]);
    }

    /**
     * @param psName the name of the ProjectStage
     * @return the ProjectStage which is identified by it's name
     */
    public static ProjectStage valueOf(String psName)
    {
        return projectStages.get(psName);
    }

    public static ProjectStage[] values()
    {
        return values;
    }
    
    public String toString()
    {
        return psName;
    }


    @Typed()
    public static final class CUnitTest extends ProjectStage
    {
    };
    public static final CUnitTest UnitTest = new CUnitTest();


    @Typed()
    public static final class CDevelopment extends ProjectStage
    {
    };
    public static final CDevelopment Development = new CDevelopment();

    @Typed()
    public static final class CSystemTest extends ProjectStage
    {
    };
    public static final CSystemTest SystemTest = new CSystemTest();

    @Typed()
    public static final class CIntegrationTest extends ProjectStage
    {
    };
    public static final CIntegrationTest IntegrationTest = new CIntegrationTest();

    @Typed()
    public static final class CStaging extends ProjectStage
    {
    };
    public static final CStaging Staging = new CStaging();

    @Typed()
    public static final class CProduction extends ProjectStage
    {
    };
    public static final CProduction Production = new CProduction();
}
