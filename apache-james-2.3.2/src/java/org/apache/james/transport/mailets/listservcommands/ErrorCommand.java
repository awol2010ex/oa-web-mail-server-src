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

package org.apache.james.transport.mailets.listservcommands;

import org.apache.james.transport.mailets.ICommandListservManager;
import org.apache.james.util.XMLResources;
import org.apache.mailet.Mail;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import javax.mail.MessagingException;
import java.util.Properties;

/**
 * Error handles the error command.
 * It is configured by:
 * <pre>&lt;command name="error" class="ErrorCommand"/&gt;</pre>
 *
 * <br />
 * <br />
 *
 * It uses the formatted text-based resources for its return mail body:
 * <ul>
 *  <li>header
 *  <li>error
 *  <li>admincommands
 * </ul>
 *
 * <br />
 * <br />
 * After formatting the text, the message is delivered with {@link #sendStandardReply}
 *
 * @version CVS $Revision: 494012 $ $Date: 2007-01-08 10:23:58 +0000 (Mon, 08 Jan 2007) $
 * @since 2.2.0
 */
public class ErrorCommand extends BaseCommand {

    //For resources
    protected XMLResources[] xmlResources;

    protected static final int HEADER = 0;
    protected static final int ERROR = 1;
    protected static final int ADMIN_COMMANDS = 2;

    public void init(ICommandListservManager commandListservManager, Configuration configuration) throws ConfigurationException {
        super.init(commandListservManager, configuration);
        xmlResources = initXMLResources(new String[]{"header", "error", "admincommands"});
    }

    /**
     * Delegate to {@link #onError}
     * @param mail
     * @throws MessagingException
     */
    public void onCommand(Mail mail) throws MessagingException {
        onError(mail, "an unknown error occurred", "an unknown error occurred");
    }

    /**
     * An error occurred, send a message with the following text resources:
     * <ul>
     *  <li>{@link #HEADER}
     *  <li>{@link #ERROR}
     *  <li>{@link #ADMIN_COMMANDS}
     * </ul>
     *
     * @param subject the subject of the message to send
     * @param mail
     * @param errorMessage
     */
    public void onError(Mail mail, String subject, String errorMessage) throws MessagingException {

        Properties props = getStandardProperties();
        props.put("ERROR_MESSAGE", errorMessage);

        StringBuffer plainTextMessage = new StringBuffer();
        String header = xmlResources[HEADER].getString("text", props);
        plainTextMessage.append(header);

        String errorText = xmlResources[ERROR].getString("text", props);
        plainTextMessage.append(errorText);

        String adminCommands = xmlResources[ADMIN_COMMANDS].getString("text", props);
        plainTextMessage.append(adminCommands);

        sendStandardReply(mail, subject, plainTextMessage.toString(), null);
    }
}
