package org.infinitypfm.core.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.lang.StringUtils;

public class MailMonitor {

	public void checkMailbox() {
		
		try
		{
			Properties props = new Properties();
		    props.put("mail.pop3s.auth", true);
		    
		    final String mailBox = "TODO:emailaddress";
		    final String pwd = "TODO:MailPassword";
		    
		    Session session = Session.getDefaultInstance(props , new Authenticator() {
		        @Override
		                protected PasswordAuthentication getPasswordAuthentication() {
		                    return new PasswordAuthentication(mailBox,pwd);
		                }
		    });
		    
		    //session.setDebug(true);
		    
			Store store = session.getStore("TODO:MailProtocol");
			
			if (store.isConnected()){
				store.close();
			}
			
			//LOG.debug("Connecting to mailbox: " + map.getMailAddress());
			
			store.connect("TODO:MailServer", 25, "TODO:EmailAddress", "TODO:MailPassord");
			Folder inbox = store.getFolder("INBOX");
	        inbox.open(Folder.READ_WRITE);
	        Message[] mails = inbox.getMessages();
	         
	         for (Message msg : mails) {

		        	if (msg == null){
	        	 		continue;
	        	 	}
		        	 Multipart mp = (Multipart) msg.getContent();
		        	 for (int i = 0; i < mp.getCount(); i++) {
			        	 BodyPart bodyPart = mp.getBodyPart(i);
		                 if(!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
		                           StringUtils.isBlank(bodyPart.getFileName())) {
		                  continue; // dealing with attachments only
		                 } 
		                 InputStream is = bodyPart.getInputStream();
		                
		                 File f = new File("TODO:WORKFolder" + 
		                		 File.separator + 
		                		 bodyPart.getFileName());
		                 
		                 FileOutputStream fos = new FileOutputStream(f);
		                 byte[] buf = new byte[4096];
		                 int bytesRead;
		                 while((bytesRead = is.read(buf))!=-1) {
		                    fos.write(buf, 0, bytesRead);	         
		                 }
		                fos.close();
		                
		               // processor.Process(map.toFileMap(_config), f.toPath());
		                
		                msg.setFlag(Flags.Flag.DELETED, true);    
		                
		        	}
		         }
	         
        	  inbox.close(true);
        	  store.close();
		} catch (Exception e) {
			//TODO:Handle error
		}
	}
}		
