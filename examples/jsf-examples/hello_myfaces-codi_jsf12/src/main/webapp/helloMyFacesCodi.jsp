<%--
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>Hello World</title>
    </head>
    <body>
        <f:view>
            <h1>JSF-Demo</h1>
            <h:form id="jsfMainForm">
                <h:panelGrid columns="2">
                    <h:outputLabel for="txtInfo" value="Bean-value:"/>
                    <h:outputText id="txtInfo" value="#{facesDemoBean.text}"/>

                    <h:commandButton value="send"/>
                    <h:panelGroup/>
                </h:panelGrid>
            </h:form>

            <h1>BV-Demo</h1>
            <h:form id="bvMainForm">
                <h:panelGrid columns="3">
                    <h:outputLabel for="txtInfo" value="Bean-value:"/>
                    <h:inputText id="txtInfo" value="#{beanValidationDemoBean.text}" label="Bean-value"/>
                    <h:message for="txtInfo" showSummary="false" showDetail="true" errorStyle="color: red;"/>

                    <h:commandButton value="send" action="#{beanValidationDemoBean.send}"/>
                    <h:panelGroup/>
                    <h:panelGroup/>
                </h:panelGrid>
            </h:form>

            <h:messages globalOnly="true" infoStyle="color: blue;" warnStyle="color: orange;" errorStyle="color: red;" fatalStyle="color:darkred;"/>
        </f:view>
    </body>
</html>
