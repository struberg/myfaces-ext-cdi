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
package org.apache.myfaces.extensions.cdi.message.test;

import org.apache.myfaces.extensions.cdi.message.api.Formatter;
import org.apache.myfaces.extensions.cdi.message.api.MessageContext;

import java.io.Serializable;

/**
 * @author Gerhard Petracek
 */
public class TestFormatter implements Formatter<Object>, Serializable
{
    private static final long serialVersionUID = 3529715901768617301L;

    private Class responsibleFor;

    TestFormatter(Class type)
    {
        this.responsibleFor = type;
    }

    public boolean isResponsibleFor(Class<?> type)
    {
        return type.isAssignableFrom(this.responsibleFor);
    }

    public boolean isStateless()
    {
        return true;
    }

    public String format(MessageContext messageContext, Object valueToFormat)
    {
        return valueToFormat != null ? valueToFormat.toString() : "";
    }
}
