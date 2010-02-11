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
package org.apache.myfaces.extensions.cdi.api.tools.annotate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A small helper class to create a Annotation instance of the given annotation class
 * via {@link java.lang.reflect.Proxy}. The annotation literal gets filled with the default values.</p>
 *
 * <p>usage:</p>
 * <pre>
 * Class<? extends annotation> annotationClass = ...;
 * Annotation a = DefaultAnnotation.of(annotationClass)
 * </pre>
 * <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
*/
public class DefaultAnnotation implements InvocationHandler{
    
    static Map<Class<? extends Annotation>, Annotation> annotationCache 
                        = new HashMap<Class<? extends Annotation>, Annotation>();
    
    public static Annotation of(Class<? extends Annotation> annotationClass) 
    {
        Annotation annon = annotationCache.get(annotationClass);
        
        if (annon == null)
        {
            // switch into paranoia mode
            synchronized (annotationCache) 
            {
                annon = annotationCache.get(annotationClass);
                if (annon == null)
                {
                    annon = (Annotation) Proxy.newProxyInstance(annotationClass.getClassLoader(), 
                            new Class[] {annotationClass}, 
                            new DefaultAnnotation());
                    annotationCache.put(annotationClass, annon);
                }
            }
        }
        
        return annon;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
    {
        return method.getDefaultValue();
    }
}
