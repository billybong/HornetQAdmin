package se.rl.hornetqadmin.bo;

import java.util.ArrayList;
import java.util.List;

import org.hornetq.utils.json.JSONArray;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;

public class Subscription {

	private String name;
	private long deliveryCount;
	private String clientId;
	private String queueName;
	private long messageCount;
	private boolean durable;
	private long consumerCount;
	
	public static List<Subscription> fromJSON(String json) throws JSONException{
		List<Subscription> ret = new ArrayList<Subscription>();
		JSONArray subscriptionArray = new JSONArray(json);
		
		for(int i = 0; i < subscriptionArray.length(); i++){
			Subscription sub = new Subscription();
			
			JSONObject subAsJson = subscriptionArray.getJSONObject(i);
			
			sub.setName(subAsJson.getString("name"));
			sub.setDeliveryCount(subAsJson.getLong("deliveringCount"));
			sub.setClientId(subAsJson.getString("clientID"));
			sub.setQueueName(subAsJson.getString("queueName"));
			sub.setMessageCount(subAsJson.getLong("messageCount"));
			sub.setDurable(subAsJson.getBoolean("durable"));
			
			JSONArray consAsJson = subAsJson.getJSONArray("consumers");
			sub.setConsumerCount(consAsJson.length());
			
			ret.add(sub);
		}
		
		return ret;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public long getDeliveryCount() {
		return deliveryCount;
	}


	public void setDeliveryCount(long deliveryCount) {
		this.deliveryCount = deliveryCount;
	}


	public String getClientId() {
		return clientId;
	}


	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	public String getQueueName() {
		return queueName;
	}


	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}


	public long getMessageCount() {
		return messageCount;
	}


	public void setMessageCount(long messageCount) {
		this.messageCount = messageCount;
	}


	public boolean isDurable() {
		return durable;
	}


	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public long getConsumerCount() {
		return consumerCount;
	}


	public void setConsumerCount(long consumerCount) {
		this.consumerCount = consumerCount;
	}


	@Override
	public String toString() {
		return "Subscription [name=" + name + ", deliveryCount="
				+ deliveryCount + ", clientId=" + clientId + ", queueName="
				+ queueName + ", messageCount=" + messageCount + ", durable="
				+ durable + ", consumerCount=" + consumerCount + "]";
	}
	
	
}
