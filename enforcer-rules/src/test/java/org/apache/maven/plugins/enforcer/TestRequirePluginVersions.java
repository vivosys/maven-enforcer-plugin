package org.apache.maven.plugins.enforcer;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugins.enforcer.utils.EnforcerRuleUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * 
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 * 
 */
public class TestRequirePluginVersions
    extends AbstractMojoTestCase
{
  
    public void testHasVersionSpecified ()
    {
        Plugin source = new Plugin();
        source.setArtifactId( "foo" );
        source.setGroupId( "group" );

        // setup the plugins. I'm setting up the foo group
        // with a few bogus entries and then a real one.
        // this is to test that the list is exhaustively
        // searched for versions before giving up.
        // banLatest/Release will fail if it is found
        // anywhere in the list
        List plugins = new ArrayList();
        plugins.add( EnforcerTestUtils.newPlugin( "group", "a-artifact", "1.0" ) );
        plugins.add( EnforcerTestUtils.newPlugin( "group", "foo", null ) );
        plugins.add( EnforcerTestUtils.newPlugin( "group", "foo", "" ) );
        plugins.add( EnforcerTestUtils.newPlugin( "group", "b-artifact", "1.0" ) );
        plugins.add( EnforcerTestUtils.newPlugin( "group", "foo", "1.0" ) );
        plugins.add( EnforcerTestUtils.newPlugin( "group", "c-artifact", "LATEST" ) );
        plugins.add( EnforcerTestUtils.newPlugin( "group", "c-artifact", "1.0" ) );
        plugins.add( EnforcerTestUtils.newPlugin( "group", "d-artifact", "RELEASE" ) );
        plugins.add( EnforcerTestUtils.newPlugin( "group", "d-artifact", "1.0" ) );
        plugins.add( EnforcerTestUtils.newPlugin( "group", "e-artifact", "1.0" ) );
        plugins.add( EnforcerTestUtils.newPlugin( "group", "e-artifact", "RELEASE" ) );
        plugins.add( EnforcerTestUtils.newPlugin( "group", "f-artifact", "1.0" ) );
        plugins.add( EnforcerTestUtils.newPlugin( "group", "f-artifact", "LATEST" ) );

        RequirePluginVersions rule = new RequirePluginVersions();
        rule.setBanLatest( false );
        rule.setBanRelease( false );

        assertTrue( rule.hasVersionSpecified( source, plugins ) );

        // check that LATEST is allowed
        source.setArtifactId( "c-artifact" );
        assertTrue( rule.hasVersionSpecified( source, plugins ) );

        // check that LATEST is banned
        rule.setBanLatest( true );
        assertFalse( rule.hasVersionSpecified( source, plugins ) );

        // check that LATEST is exhausively checked
        source.setArtifactId( "f-artifact" );
        assertFalse( rule.hasVersionSpecified( source, plugins ) );

        // check that RELEASE is allowed
        source.setArtifactId( "d-artifact" );
        assertTrue( rule.hasVersionSpecified( source, plugins ) );

        // check that RELEASE is banned
        rule.setBanRelease( true );
        assertFalse( rule.hasVersionSpecified( source, plugins ) );

        // check that RELEASE is exhausively checked
        source.setArtifactId( "e-artifact" );
        assertFalse( rule.hasVersionSpecified( source, plugins ) );
    }

  

    public void testGetAllPlugins ()
        throws ArtifactResolutionException, ArtifactNotFoundException, IOException, XmlPullParserException
    {
        RequirePluginVersions rule = new RequirePluginVersions();
        String path = "target/test-classes/requirePluginVersions/getPomRecursively/b/c";

        StringUtils.replace( path, "/", File.separator );

        File projectDir = new File( getBasedir(), path );

        MockProject project = new MockProject();
        project.setArtifactId( "c" );
        project.setGroupId( "group" );
        project.setVersion( "1.0" );
        project.setBaseDir( projectDir );

        rule.setUtils( new EnforcerRuleUtils(EnforcerTestUtils.getHelper( project )) );
        List plugins = rule.getAllPluginEntries( project );

        // there should be 3
        assertEquals( 3, plugins.size() );
    }
    
    public void testId ()
    {
        RequirePluginVersions rule = new RequirePluginVersions();
        rule.getCacheId();
    }
}
