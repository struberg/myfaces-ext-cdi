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
package org.apache.myfaces.extensions.cdi.javaee.jsf.impl.scope.conversation.spi;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.Conversation;
import org.apache.myfaces.extensions.cdi.core.impl.scope.conversation.spi.BeanEntry;

/**
 * TODO
 * @author Gerhard Petracek
 */
public interface EditableConversation extends Conversation
{
    /**
     * @return evaluates and returns if the conversation is active
     */
    boolean isActive();

    /**
     * @return returns if the conversation is active (without evaluation)
     */
    boolean getActiveState();

    /**
     * has to expire a conversation. if the conversation is expired afterwards it has to be inactive
     */
    void deactivate();

    <T> void addBean(BeanEntry<T> beanInstance);

    /**
     * @param key class of the requested bean
     * @param <T> type of the requested bean
     * @return an instance of the requested bean if the conversation is active - null otherwise
     */
    <T> T getBean(Class<T> key);
}
