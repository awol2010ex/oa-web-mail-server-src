package org.apache.james.transport.matchers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import javax.mail.Header;
import javax.mail.MessagingException;

import org.apache.avalon.cornerstone.services.datasources.DataSourceSelector;
import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.james.Constants;
import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;

import sun.rmi.transport.Connection;

//超出容量时限制接收邮件
public class BeyondCapacityMatches extends GenericMatcher {

	@Override
	public Collection match(Mail mail) throws MessagingException {
		// TODO Auto-generated method stub
		Iterator it = mail.getAttributeNames();

		while (it.hasNext()) {
			String key = (String) it.next();

			String value = mail.getAttribute(key).toString();

			System.out.println(key + "=" + value);
		}

		String To = null;// 接收人用户

		Enumeration e = (mail.getMessage().getAllHeaders());
		while (e.hasMoreElements()) {
			Header h = ((Header) e.nextElement());

			System.out.println(h.getName() + "=" + h.getValue());

			if ("To".equals(h.getName())) {
				To = h.getValue().split(" ")[0];

				System.out.println("To=" + To);
			}
		}

		if (To != null) {

			ServiceManager componentManager = (ServiceManager) getMailetContext()
					.getAttribute(Constants.AVALON_COMPONENT_MANAGER);
			// Get the DataSourceSelector service

			long capacity =0L;
			try {
				DataSourceSelector datasources = (DataSourceSelector) componentManager
						.lookup(DataSourceSelector.ROLE);

				// Get the data-source required.
				DataSourceComponent datasource = (DataSourceComponent) datasources
						.select("maildb");

				java.sql.Connection conn = datasource.getConnection();

				
				PreparedStatement p = conn.prepareStatement("select * from users_static where username =?");
				p.setString(1, To);
				ResultSet  r=p.executeQuery();
				while(r.next()){
					capacity = r.getLong("totalcapacity");
				}
				
				r.close();
				p.close();
				conn.close();
			} catch (ServiceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			//超过一定容量
			if(capacity >=100*1024*1024L){
				
				return mail.getRecipients();
			}
		}

		return null;
	}

}
