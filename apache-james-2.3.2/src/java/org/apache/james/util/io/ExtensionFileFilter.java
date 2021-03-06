/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.util.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * This filters files based on the extension (what the filename
 * ends with). This is used in retrieving all the files of a
 * particular type.
 *
 * <p>Eg., to retrieve and print all <code>*.java</code> files in the current directory:</p>
 *
 * <pre>
 * File dir = new File(".");
 * String[] files = dir.list( new ExtensionFileFilter( new String[]{"java"} ) );
 * for (int i=0; i&lt;files.length; i++)
 * {
 *     System.out.println(files[i]);
 * }
 * </pre>
 *
 * @author  Federico Barbieri <fede@apache.org>
 * @author Serge Knystautas <sergek@lokitech.com>
 * @author Peter Donald
 * @version CVS $Revision: 494012 $ $Date: 2007-01-08 10:23:58 +0000 (Mon, 08 Jan 2007) $
 * @since 4.0
 */
public class ExtensionFileFilter
    implements FilenameFilter
{
    private String[] m_extensions;

    public ExtensionFileFilter( final String[] extensions )
    {
        m_extensions = extensions;
    }

    public ExtensionFileFilter( final String extension )
    {
        m_extensions = new String[]{extension};
    }

    public boolean accept( final File file, final String name )
    {
        for( int i = 0; i < m_extensions.length; i++ )
        {
            if( name.endsWith( m_extensions[ i ] ) )
            {
                return true;
            }
        }
        return false;
    }
}


