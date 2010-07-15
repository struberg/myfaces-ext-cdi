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
package org.apache.myfaces.extensions.cdi.javaee.jsf2.impl.request;

import org.apache.myfaces.extensions.cdi.javaee.jsf.api.request.AbstractRequestTypeResolver;
import org.apache.myfaces.extensions.cdi.javaee.jsf.api.request.RequestTypeResolver;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;

/**
 * @author Gerhard Petracek
 */
@ApplicationScoped
public class DefaultRequestTypeResolver extends AbstractRequestTypeResolver
{
    protected RequestTypeResolver createDefaultRequestTypeResolver()
    {
        return new RequestTypeResolver()
        {
            private boolean postRequest;
            private boolean partialRequest;

            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                this.postRequest = facesContext.isPostback();
                this.partialRequest = facesContext.getPartialViewContext().isPartialRequest();
            }

            public boolean isPartialRequest()
            {
                return this.partialRequest;
            }

            public boolean isPostRequest()
            {
                return this.postRequest;
            }
        };
    }
}
