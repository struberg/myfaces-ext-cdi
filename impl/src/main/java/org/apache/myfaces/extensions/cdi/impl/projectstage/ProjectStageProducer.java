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
package org.apache.myfaces.extensions.cdi.impl.projectstage;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.faces.application.Application;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;

/**
 * <p>Produces {@link ProjectStage} configurations.</p>
 *
 * <p>The producer will try to detect the currently active ProjectStage on startup
 * and use that for all generated fields.</p>
 * <p>In case a JSF runtime is not available (e.g. in unit tests) we do all the
 * determining ourself (but in the same way as MyFaces does it!)</p>
 *
 * <p>Usage:</p>
 * Simply inject the current ProjectStage into any bean:
 * <pre>
 * public class MyBean {
 *   private @Inject ProjectStage projectStage;
 *
 *   public void fn() {
 *     if(projectStage == ProjectStage.Production) {
 *        // do some prodution stuff...
 *     }
 *   }
 * }
 * </pre>
 *
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
public class ProjectStageProducer {

    /** JNDI path for the ProjectStage */
    public final static String PROJECT_STAGE_JNDI_NAME = "java:comp/env/jsf/ProjectStage";

    /** System Property to set the ProjectStage, if not present via the standard way */
    public final static String MYFACES_PROJECT_STAGE_SYSTEM_PROPERTY_NAME = "faces.PROJECT_STAGE";


    private static final Log log = LogFactory.getLog(ProjectStageProducer.class);

    private ProjectStage projectStage;

    /**
     * We can only produce @Dependent scopes since an enum is final.
     * @return current ProjectStage
     */
    @Produces @Dependent @Default
    public ProjectStage getProjectStage() {
        return projectStage;
    }

    /**
     * This function can be used to manually set the ProjectStage for the application.
     * This is e.g. useful in unit tests.
     * @param projectStage the ProjectStage to set
     */
    public void setProjectStage(ProjectStage projectStage) {
        this.projectStage = projectStage;
    }

    /**
     * Read the configuration from the stated places.
     * This can be overloaded to implement own lookup mechanisms.
     */
    @PostConstruct
    public void determineProjectStage() {
        try {
            projectStage = getProjectStageFromJsf();

            if (projectStage == null) {
                projectStage = getProjectStageFromEnvironment();
            }

            // the last resort is setting it to Production
            if (projectStage == null) {
                projectStage = ProjectStage.Production;
            }
        }
        catch (Exception e) {
            // repackaging, since PostConstruct functions may not throw checked Exceptions
            throw new RuntimeException(e);
        }

    }


    protected ProjectStage getProjectStageFromJsf() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            return null;
        }

        Application app = facesContext.getApplication();
        if (app == null) {
            return null;
        }

        return app.getProjectStage();
    }

    protected ProjectStage getProjectStageFromEnvironment() {
        String stageName = System.getProperty(MYFACES_PROJECT_STAGE_SYSTEM_PROPERTY_NAME);

        if (stageName != null) {
            return ProjectStage.valueOf(stageName);
        }
        return null;
    }

}
