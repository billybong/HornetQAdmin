package se.rl.hornetqadmin;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.jms.*;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.jms.client.HornetQConnectionFactory;

public class HornetAccessor {

	private Connection connection;
	private Session session;

    public void postJmsMessageToQueue(String message, String queueName) throws JMSException{
        setup();
        Queue queue = session.createQueue(queueName.replace("jms.queue.", ""));
        MessageProducer producer = session.createProducer(queue);

        TextMessage msg = session.createTextMessage();
        msg.setText(message);
        msg.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
        producer.send(msg);
        producer.close();
        tearDown();
    }

	public List<Message> retrieveMessages(String queue) throws Exception {
		
		List<Message> ret = new ArrayList<Message>();
		
		try{
			setup();
			Enumeration enumeration = this.constructQueueBrowser(queue, null).getEnumeration();
			
			while(enumeration.hasMoreElements()){
				ret.add((Message) enumeration.nextElement());
			}
		}catch(Exception e){
			throw e;
		}finally{
			this.tearDown();
		}
		return ret;
	}

	
	public Message retrieveMessage(String queue, String messageFilter) throws JMSException{
		try {
			setup();
			QueueBrowser queueBrowser = constructQueueBrowser(queue, messageFilter);
				
			if(queueBrowser.getEnumeration().hasMoreElements()){
				return (Message)queueBrowser.getEnumeration().nextElement();
			}else{
				return null;
			}
			
		} catch (JMSException e) {
			throw e;
		} finally{
			this.tearDown();
		}		
	}
	
	private void setup() throws JMSException{
		HornetQConnectionFactory cf = HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, new TransportConfiguration(NettyConnectorFactory.class.getName()));
        connection = cf.createConnection();
		
		session = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
		
		connection.start();	
	}
	
	private void tearDown() throws JMSException{
		if(session != null)
			session.close();
		
		if(connection != null){
			connection.close();
		}
	}
	
	private QueueBrowser constructQueueBrowser(String queue, String messageFilter) throws JMSException{
		queue = queue.replace("jms.queue.", "");
		queue = queue.replace("jms.topic.", "");
		
		QueueBrowser consumer = session.createBrowser(session.createQueue(queue), messageFilter);

		return consumer;
	}
}
