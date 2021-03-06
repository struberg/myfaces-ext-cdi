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
package org.apache.myfaces.extensions.cdi.javaee.jsf2.impl.scope.conversation;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.util.Iterator;

/**
 * @author Gerhard Petracek
 */
public class CodiRenderKitFactory extends RenderKitFactory
{
    private final RenderKitFactory wrapped;

    public CodiRenderKitFactory(RenderKitFactory wrapped)
    {
        this.wrapped = wrapped;
    }

    public void addRenderKit(String s, RenderKit renderKit)
    {
        wrapped.addRenderKit(s, renderKit);
    }

    public RenderKit getRenderKit(FacesContext facesContext, String s)
    {
        RenderKit renderKit = wrapped.getRenderKit(facesContext, s);

        if (renderKit == null)
        {
            return null;
        }
        return new InterceptedRenderKit(renderKit);
    }

    public Iterator<String> getRenderKitIds()
    {
        return wrapped.getRenderKitIds();
    }

    @Override
    public RenderKitFactory getWrapped()
    {
        return this.wrapped;
    }
}
