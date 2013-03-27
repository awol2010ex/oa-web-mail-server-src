package org.apache.james.transport.matchers;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;

//正则表达式过滤
public class SubjectMatches extends GenericMatcher {
	public Collection match(Mail mail) throws MessagingException {
		MimeMessage mm = mail.getMessage();
		String subject = mm.getSubject();

		if (subject != null) {
			Pattern pattern = Pattern.compile(this.getCondition());
			Matcher matcher = pattern.matcher(subject);
			if (matcher.matches()) {
				return mail.getRecipients();

			}
		}
		return null;
	}
}
