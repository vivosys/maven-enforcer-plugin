package org.apache.maven.enforcer.rule.api;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

/**
 * This is the interface that all helpers will use. This
 * provides access to the log, session and components to the
 * rules.
 * 
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 * @version $Id: EnforcerRuleHelper.java 571673 2007-09-01
 *          03:04:24Z brianf $
 */
public interface EnforcerRuleHelper
    extends ExpressionEvaluator
{
    public Log getLog ();

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.maven.shared.enforcer.rule.api.EnforcerRuleHelper#getRuntimeInformation()
     */
    public Object getComponent ( Class clazz )
        throws ComponentLookupException;

    public Object getComponent ( String componentKey )
        throws ComponentLookupException;

    public Object getComponent ( String role, String roleHint )
        throws ComponentLookupException;

    public Map getComponentMap ( String role )
        throws ComponentLookupException;

    public List getComponentList ( String role )
        throws ComponentLookupException;
    
    public PlexusContainer getContainer(); 
}
