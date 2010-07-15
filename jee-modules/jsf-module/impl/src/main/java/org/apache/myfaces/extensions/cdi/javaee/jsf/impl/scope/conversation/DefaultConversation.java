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
package org.apache.myfaces.extensions.cdi.javaee.jsf.impl.scope.conversation;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.Conversation;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.WindowScoped;
import org.apache.myfaces.extensions.cdi.core.impl.scope.conversation.spi.BeanEntry;
import org.apache.myfaces.extensions.cdi.core.impl.scope.conversation.spi.EditableConversation;
import org.apache.myfaces.extensions.cdi.javaee.jsf.impl.scope.conversation.spi.ConversationKey;
import static org.apache.myfaces.extensions.cdi.javaee.jsf.impl.util.ConversationUtils.getOldViewIdFromRequest;
import static org.apache.myfaces.extensions.cdi.javaee.jsf.impl.util.ConversationUtils.getNewViewIdFromRequest;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Gerhard Petracek
 */
public class DefaultConversation implements Conversation, EditableConversation
{
    private static final long serialVersionUID = -2958548175169003298L;

    //for easier debugging
    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
    private final ConversationKey conversationKey;

    private final boolean windowScoped;

    private String lastViewId; //for access scope

    private final BeanStorage beanStorage = new BeanStorage();

    private final long conversationTimeoutInMs;

    private boolean active;

    private Date lastAccess;

    public DefaultConversation(ConversationKey conversationKey, int conversationTimeoutInMinutes)
    {
        this.conversationKey = conversationKey;
        this.windowScoped = WindowScoped.class.isAssignableFrom(conversationKey.getConversationGroup());

        tryToProcessViewAccessScope();

        this.conversationTimeoutInMs = conversationTimeoutInMinutes * 60000;
    }

    public boolean isActive()
    {
        return !isConversationTimedout() && this.active;
    }

    public void deactivate()
    {
        if (!this.windowScoped)
        {
            this.active = false;
        }
    }

    public void end()
    {
        if(this.active)
        {
            this.active = false;
            this.beanStorage.resetStorage();
        }
    }

    public void restart()
    {
        touchConversation(true);
        this.beanStorage.resetStorage();
    }

    @SuppressWarnings({"unchecked"})
    public <T> T getBean(Class<T> key)
    {
        if (!isActive())
        {
            return null;
        }

        touchConversation(true);

        BeanEntry scopedBean = this.beanStorage.getBean(key);

        if (scopedBean == null)
        {
            return null;
        }

        return (T) scopedBean.getBeanInstance();
    }

    public <T> void addBean(BeanEntry<T> beanEntry)
    {
        tryToProcessViewAccessScope();

        //TODO check if conversation is active
        touchConversation(false);

        //TODO
        //noinspection unchecked
        this.beanStorage.addBean((BeanEntry<Serializable>) beanEntry);
    }

    private boolean isConversationTimedout()
    {
        if (this.windowScoped)
        {
            return false;
        }

        if (this.lastViewId != null)
        {
            return isInvalidConversationForCurrentView();
        }

        return this.lastAccess == null ||
                (this.lastAccess.getTime() + this.conversationTimeoutInMs) < System.currentTimeMillis();
    }

    private boolean isInvalidConversationForCurrentView()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String fromViewId = getOldViewIdFromRequest(facesContext);

        if (fromViewId != null && fromViewId.endsWith(this.lastViewId))
        {
            this.lastViewId = getNewViewIdFromRequest(facesContext);
        }
        return !this.lastViewId.equals(getCurrentViewId());
    }

    private void touchConversation(boolean updateViewId)
    {
        this.active = true;
        this.lastAccess = new Date();

        //just update it if it is a view-access scope (= there is already a value)
        if (updateViewId && this.lastViewId != null)
        {
            this.lastViewId = getCurrentViewId();
        }
    }

    private String getCurrentViewId()
    {
        return FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }

    private void tryToProcessViewAccessScope()
    {
        //workaround
        if(this.conversationKey instanceof DefaultConversationKey &&
                 ((DefaultConversationKey)this.conversationKey).isViewAccessScopedAnnotationPresent())
        {
            this.lastViewId = getCurrentViewId();
        }
    }
}
