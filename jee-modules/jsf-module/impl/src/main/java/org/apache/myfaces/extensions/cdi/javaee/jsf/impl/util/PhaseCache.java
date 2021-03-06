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
package org.apache.myfaces.extensions.cdi.javaee.jsf.impl.util;

import org.apache.myfaces.extensions.cdi.javaee.jsf.impl.scope.conversation.spi.ConversationKey;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.Conversation;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Gerhard Petracek
 */
class PhaseCache
{
    private static ThreadLocal<Map<ConversationKey, Conversation>> conversationCache
            = new ThreadLocal<Map<ConversationKey, Conversation>>();

    protected void reset()
    {

    }
    public static Conversation getConversation(ConversationKey conversationKey)
    {
        return getConversationCache().get(conversationKey);
    }

    public static void setConversation(ConversationKey conversationKey, Conversation conversation)
    {
        getConversationCache().put(conversationKey, conversation);
    }

    private static Map<ConversationKey, Conversation> getConversationCache()
    {
        Map<ConversationKey, Conversation> conversationMap = conversationCache.get();

        if(conversationMap == null)
        {
            conversationMap = new HashMap<ConversationKey, Conversation>();
            conversationCache.set(conversationMap);
        }
        return conversationMap;
    }
}
