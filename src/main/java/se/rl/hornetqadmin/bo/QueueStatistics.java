/*
 * Simple POJO containing queue statistics for a specific queue or topic
 * 
 */
package se.rl.hornetqadmin.bo;

/**
 *
 * @author bilsjb
 */
public class QueueStatistics {
    private String queueName;
    private long depth;
    private long depthDelta;
    private long count;
    private long countDelta;
    private String lastAddTimestamp;
    private String updateTimestamp;
    private int consumerCount;

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public QueueStatistics(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }
    
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getCountDelta() {
        return countDelta;
    }

    public void setCountDelta(long countDelta) {
        this.countDelta = countDelta;
    }

    public long getDepth() {
        return depth;
    }

    public void setDepth(long depth) {
        this.depth = depth;
    }

    public long getDepthDelta() {
        return depthDelta;
    }

    public void setDepthDelta(long depthDelta) {
        this.depthDelta = depthDelta;
    }

    public String getLastAddTimestamp() {
        return lastAddTimestamp;
    }

    public void setLastAddTimestamp(String lastAddTimestamp) {
        this.lastAddTimestamp = lastAddTimestamp;
    }

    public int getConsumerCount() {
        return consumerCount;
    }

    public void setConsumerCount(int consumerCount) {
        this.consumerCount = consumerCount;
    }
}
