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
package org.apache.myfaces.blank.bv;

import org.hibernate.validator.constraints.impl.NotNullValidator;

import javax.validation.constraints.NotNull;
import javax.validation.ConstraintValidatorContext;
import javax.inject.Inject;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 * @author Gerhard Petracek
 */
public class CodiNotNullConstraintValidator extends NotNullValidator
{
    @Inject
    private FacesContext facesContext;

    //@Inject
    //private ProjectStage projectStage;

    @Override
    public void initialize(NotNull parameters)
    {
        super.initialize(parameters);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext)
    {
        boolean isValid = super.isValid(object, constraintValidatorContext);

        if(/*ProjectStage.Development.equals(this.projectStage) &&*/ !isValid)
        {
            String message = "violation found - constraint: @" +
                    NotNull.class.getSimpleName() + " detected by: " + getClass().getName();
            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
        }
        return isValid;
    }
}
