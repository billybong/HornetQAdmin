package se.rl.hornetqadmin;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.management.HornetQServerControl;
import org.hornetq.api.core.management.MessageCounterInfo;
import org.hornetq.api.core.management.ObjectNameBuilder;
import org.hornetq.api.core.management.QueueControl;
import org.hornetq.api.jms.management.TopicControl;

import se.rl.hornetqadmin.bo.QueueStatistics;
import se.rl.hornetqadmin.bo.Subscription;
import se.rl.hornetqadmin.bo.TopicStatistics;

/**
 *
 * @author bilsjb
 */
public class MBeanAdmin {
 
    private static MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
    
    public static String[] queryQueueNames() throws Exception{
    	List<String> ret = new ArrayList<String>();
    	
        for (String name : queryQueueAndTopicNames()) {
            if (name.startsWith("jms.queue"))
                ret.add(name);
        }
                
        return ret.toArray(new String[ret.size()]);
    }
    
    public static String[] queryTopicNames() throws Exception{
        
    	List<String> ret = new ArrayList<String>();
    	
        for (String name : queryQueueAndTopicNames()) {
            if (name.startsWith("jms.topic"))
                ret.add(name);
        }
                
        return ret.toArray(new String[ret.size()]);
    }
    
    private static List<String> queryQueueAndTopicNames() throws Exception{
    	ObjectName serverObjectName = ObjectNameBuilder.DEFAULT.getHornetQServerObjectName();
        HornetQServerControl serverControl = MBeanServerInvocationHandler.newProxyInstance(mbeanServer, serverObjectName, HornetQServerControl.class, false);
        
        String[] queueNames = serverControl.getQueueNames();
        Arrays.sort(queueNames);
        
        return Arrays.asList(queueNames);
    }
    
    public static QueueStatistics queryQueue(String queueName) throws Exception{
    
        QueueStatistics ret = new QueueStatistics(queueName);

        QueueControl queue = retrieveQueueControl(queueName);
        

        String counters = queue.listMessageCounter();
        MessageCounterInfo messageCounter = MessageCounterInfo.fromJSON(counters);
        
        ret.setCount(messageCounter.getCount());
        ret.setCountDelta(messageCounter.getCountDelta());
        ret.setDepth(messageCounter.getDepth());
        ret.setDepthDelta(messageCounter.getDepthDelta());
        ret.setLastAddTimestamp(messageCounter.getLastAddTimestamp());
        ret.setUpdateTimestamp(messageCounter.getUdpateTimestamp());
        ret.setConsumerCount(queue.getConsumerCount());

        return ret;
    }
    
    public static TopicStatistics queryTopic(String topicName) throws Exception{
    	TopicControl topicControl = retrieveTopicControl(topicName);
    	
    	TopicStatistics ret = new TopicStatistics(topicName);
    	ret.setSubscriptionCount(topicControl.getSubscriptionCount());
    	ret.setSubscriptions(Subscription.fromJSON(topicControl.listAllSubscriptionsAsJSON()));
    	
    	return ret;
    }
    
    public static long purgeAllQueues() throws Exception{
    	
    	long messagesPurged = 0;

        for(String queueName: queryQueueNames()){
            messagesPurged = messagesPurged + purgeQueue(queueName);
        }
    	
    	return messagesPurged;
    }
    
    public static String dropSubscription(String topicName, String subscriptionName, String clientId){
    	
    	try{
	    	TopicControl tc = retrieveTopicControl(topicName);
	    	tc.dropDurableSubscription(clientId, subscriptionName);
    	}catch(Exception e){
    		e.printStackTrace();
    		
    		return "Failed to drop subscription due to: " + e.getMessage();
    	}
    	
    	return "Dropped subscription for " + topicName + " " +subscriptionName + " " + clientId;
    }
    
    public static long purgeQueue(String queueName) throws Exception {
		
    	QueueControl queueControl = retrieveQueueControl(queueName);
    	
    	return queueControl.removeMessages(null);
	}

	private static QueueControl retrieveQueueControl(String queueName) throws Exception{
    	ObjectName queueObjectName = ObjectNameBuilder.DEFAULT.getQueueObjectName(new SimpleString(queueName), new SimpleString(queueName));

        return MBeanServerInvocationHandler.newProxyInstance(mbeanServer,
                 queueObjectName,
                 QueueControl.class,
                 false);
    }
	
	private static TopicControl retrieveTopicControl(String topicName) throws Exception{
    	ObjectName queueObjectName = ObjectNameBuilder.DEFAULT.getJMSTopicObjectName(topicName);

        return MBeanServerInvocationHandler.newProxyInstance(mbeanServer,
                 queueObjectName,
                 TopicControl.class,
                 false);
    }
}
