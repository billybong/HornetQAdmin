package se.rl.hornetqadmin.bo;

import java.util.List;

public class TopicStatistics {

	private final String topicName;
	private int subscriptionCount;
	private List<Subscription> subscriptions;
	
	public int getSubscriptionCount() {
		return subscriptionCount;
	}

	public void setSubscriptionCount(int subscriptionCount) {
		this.subscriptionCount = subscriptionCount;
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

	public String getTopicName() {
		return topicName;
	}

	public TopicStatistics(String topicName) {
		this.topicName = topicName;
	}
	
	public long getDurableCount(){
		long durable = 0;
		
		for(Subscription s : subscriptions){
			if(s.isDurable())
				durable++;
		}
		return durable;
	}
	
	public long getNondurableCount(){
		long nondurable = 0;
		
		for(Subscription s : subscriptions){
			if(!s.isDurable())
				nondurable++;
		}
		return nondurable;
	}

	@Override
	public String toString() {
		return "TopicStatistics [topicName=" + topicName
				+ ", subscriptionCount=" + subscriptionCount
				+ ", subscriptionList=" + subscriptions + "]";
	}

}
