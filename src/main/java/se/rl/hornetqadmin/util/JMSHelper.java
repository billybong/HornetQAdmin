package se.rl.hornetqadmin.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.*;

/**
 * @author bilsjb
 *
 *	Utility class for 
 *
 */
public class JMSHelper {

	public static Map<String, String> jmsFieldsAsString(Message msg) throws JMSException{
		if(msg == null)
			return null;
		
		
		Map<String, String> ret = new HashMap<String, String>();
		
		ret.put("JMSMessageID", msg.getJMSMessageID() + "");
		ret.put("JMSCorrelationID", msg.getJMSCorrelationID() + "");
		ret.put("Type", msg.getJMSType() + "");
		ret.put("InstanceOf", assertType(msg));
		ret.put("DeliveryMode", msg.getJMSDeliveryMode() == DeliveryMode.PERSISTENT?"Persistent": "NonPersistent");
		ret.put("Destination", msg.getJMSDestination().toString() + "");
		ret.put("Expiration", String.valueOf(msg.getJMSExpiration() + ""));
		ret.put("Priority", String.valueOf(msg.getJMSPriority() + ""));
		ret.put("Redelivered", String.valueOf(msg.getJMSRedelivered()));
		ret.put("ReplyTo", msg.getJMSReplyTo() + "");
		ret.put("Timestamp", new Date(msg.getJMSTimestamp()).toString());
				
		
		return ret;
	}

	/**
	 * Ugly as h*ll, should use cleaner reflection, but I'm to lazy... 
	 * 
	 * @param msg
	 * @return
	 */
	public static String assertType(Message msg) {
		if(msg instanceof TextMessage)
			return TextMessage.class.getName();
		
		if(msg instanceof ObjectMessage)
			return ObjectMessage.class.getName();
		
		if(msg instanceof BytesMessage)
			return BytesMessage.class.getName();
		
		if(msg instanceof MapMessage)
			return MapMessage.class.getName();
		
		if(msg instanceof StreamMessage)
			return StreamMessage.class.getName();
		
		return "Unknown";
	}
}
