/**
 * 
 */
package org.nww.modules.messenger.orm;

import java.util.Date;

import org.nww.modules.users.orm.User;
import org.nww.modules.users.orm.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;

@ReadingConverter
@Component
public class DBObjectToChatMessageConverter implements Converter<DBObject, ChatMessage> {

	@Autowired
	private UserManager userManager;
	
	/* (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public ChatMessage convert(DBObject source) {
		String userId = (String) source.get("senderUUID");
		
		User u = userManager.findOne(userId);
		
		return ChatMessage.of(
				source.get("_id").toString(), 
				u, 
				(String)source.get("content"), 
				(Date)source.get("lastModified"));
	}

}
