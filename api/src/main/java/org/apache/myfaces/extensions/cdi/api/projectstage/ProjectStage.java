/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.apache.myfaces.extensions.cdi.api.projectstage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * <p>This class is the base of all ProjectStages. A ProjectStage
 * identifies an environment.</p>
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
 * <p>Adding a new ProjectStage is done via the
 * {@link java.util.ServiceLoader} mechanism.</p>
 *
 * <p>Custom ProjectStages can be implemented by writing anonymous ProjectStage
 * members into a registered {@link ProjectStageHolder} as shown in the following
 * sample:</p>
 * <pre>
 * package org.apache.myfaces.extensions.cdi.test.api.projectstage;
 * public class MyProjectStages implements ProjectStageHolder
 * {
 *    public static final ProjectStage MyOwnProjectStage = new ProjectStage("MyOwnProjectStage") {};
 *    public static final ProjectStage MyOtherProjectStage = new ProjectStage("MyOtherProjectStage") {};
  * }
 * </pre>
 * <p>For activating those projectstages, you have to register this ProjectStageHolder class
 * to get picked up via the java.util.ServiceLoader mechanism. Simply create a file
 * <pre>
 * META-INF/services/org.apache.myfaces.extensions.cdi.api.projectstage.ProjectStageHolder
 * </pre>
 * which contains the fully qualified class name of custom ProjectStageHolder implementation:
 * <pre>
 * # this class now get's picked up by java.util.ServiceLoader
 * org.apache.myfaces.extensions.cdi.test.api.projectstage.MyProjectStages
 * </pre>
 * </p>
 * <p>You can use your own ProjectStages exactly the same way as all the ones provided
 * by the system:
 * <pre>
 * ProjectStage myOwnPs = ProjectStage.valueOf("MyOwnProjectStage");
   if (myOwnPs.equals(MyOwnProjectStage.MyOwnProjectStage)) ... 
 * </pre>
 *
 * <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
public abstract class ProjectStage implements Serializable
{
    /**
     * This map contains a static map with all registered projectStages.
     *
     * We don't need to use a ConcurrentHashMap because writing to it will
     * only be performed in the static initializer block which is guaranteed
     * to be atomic by the VM spec. 
     *
     * TODO: not sure if I can leave this final or should make it volatile. Since
     * the class initialisation goes over multiple classes and is pretty dynamic.
     * So I'm not sure if the VM guarantees of an atomic classloading.
     */
    private static final Map<String, ProjectStage> projectStages = new HashMap<String, ProjectStage>();

    /**
     * All the registered ProjectStage values.
     */
    private static volatile ProjectStage[] values = null;

    
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
        }
    }

    
    /** the name of the ProjectStage*/
    private String psName;

    /**
     * The protected constructor will register the given ProjectStage via its name.
     * The name is returned by the {@link #toString()} method of the ProjectStage.
     *
     * @param psNameIn the name of the ProjectStage which should get registered.
     */
    protected ProjectStage(String psNameIn)
    {
        this.psName = psNameIn;

        if (!projectStages.containsKey(psNameIn))
        {
            projectStages.put(psNameIn, this);
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


    public static final ProjectStage UnitTest = new ProjectStage("UnitTest")
    {
    };

    public static final ProjectStage Development = new ProjectStage("Development")
    {
    };

    public static final ProjectStage SystemTest = new ProjectStage("SystemTest")
    {
    };

    public static final ProjectStage IntegrationTest = new ProjectStage("IntegrationTest")
    {
    };

    public static final ProjectStage Staging = new ProjectStage("Staging")
    {
    };

    public static final ProjectStage Production = new ProjectStage("Production")
    {
    };
}
