<%@page
	import="se.rl.hornetqadmin.MBeanAdmin,se.rl.hornetqadmin.bo.QueueStatistics, se.rl.hornetqadmin.bo.TopicStatistics"%>
	
<%
	//Should messages get purged?
	boolean isPurgeAll = request.getParameter("purgeAllQueues") != null;
%>
    <script type="text/javascript">
        function confirmSubmit() {
            if (confirm("Are you sure you want to remove ALL messages from ALL queues?")) {
                return true;
            } else {
                return false;
            }
        }
    </script>	
<html>
<head>
<title>HornetQ Queue View</title>
<style type="text/css">
      th {background:blue;color:yellow;}
</style>
</head>
<body>
	<h1>HornetQ Queue View</h1>
	<table>
        <tr>
            <td>
                <form action="index.jsp" method="post">
                    <button name="purgeAllQueues" value="true" type="submit" onclick="return confirmSubmit()">Purge all messages</button>
                </form>
            </td>
            <td>
                <form action="index.jsp" method="get">
		            <button name="reload" value="true" type="submit">Reload</button>
	            </form>
            </td>
        </tr>
    </table>

<%
if(isPurgeAll){
	out.write("<div>" + MBeanAdmin.purgeAllQueues() + " messages were removed</div>");
}
%>

	<!--  All queues -->
	<table border="1" width="1200">
		<tr>
			<th>Queue Name</th>
			<th>Depth</th>
			<th>DepthDelta</th>
			<th>Count</th>
			<th>CountDelta</th>
			<th>Consumers</th>
			<th>Last Add</th>
			<th>Last Update</th>
		</tr>
		<%
		
		long depthTotal = 0;
		long depthDeltaTotal = 0;
		long countTotal = 0;
		long countDeltaTotal = 0;
		long consumerCountTotal = 0;
		
			for (String queueName : MBeanAdmin.queryQueueNames()) {

				QueueStatistics qstat = MBeanAdmin.queryQueue(queueName);
				out.write("<tr>");
				out.write("<td><a href=\"queuecontent.jsp?jms_queue="+ qstat.getQueueName() + "\">" + qstat.getQueueName().replace("jms.queue.", "") + "</td>");
				
				long depth = qstat.getDepth();
				long depthDelta = qstat.getDepthDelta();
				long count = qstat.getCount();
				long countDelta = qstat.getCountDelta();
				long consumerCount = qstat.getConsumerCount();
				
				out.write("<td>" + depth + "</td>");
				out.write("<td>" + depthDelta + "</td>");
				out.write("<td>" + count+ "</td>");
				out.write("<td>" + countDelta + "</td>");
				out.write("<td>" + consumerCount + "</td>");
				out.write("<td>" + qstat.getLastAddTimestamp() + "</td>");
				out.write("<td>" + qstat.getUpdateTimestamp() + "</td>");
				out.write("</tr>");
				
				depthTotal = depthTotal + depth;
				depthDeltaTotal = depthDeltaTotal + depthDelta;
				countTotal = countTotal + count;
				countDeltaTotal = countDeltaTotal + countDelta;
				consumerCountTotal = consumerCountTotal + consumerCount;
			}
		%>
		<tr>
			<th>SUM</th>
			<%
			out.write("<th>" + depthTotal + "</th>");
			out.write("<th>" + depthDeltaTotal + "</th>"); 
			out.write("<th>" + countTotal + "</th>"); 
			out.write("<th>" + countDeltaTotal + "</th>"); 
			out.write("<th>" + consumerCountTotal + "</th>"); 
			%>
			<th>-</th>
			<th>-</th>
		</tr>
	</table>
	<hr width="1200" align="left"/>
	<!--  All topics -->
	<table border="1" width="1200">
		<tr>
			<th>Topic Name</th>
			<th>Subscriptions</th>
			<th>Durable</th>
			<th>Nondurable</th>
		</tr>
		<%
		
			long subscriptionCountTotal = 0;
			long durableSubscriptionCountTotal = 0;
			long nonDurableSubscriptionCountTotal = 0;
		
			for (String topicName : MBeanAdmin.queryTopicNames()) {

				topicName = topicName.replace("jms.topic.", "");
				TopicStatistics tstat = MBeanAdmin.queryTopic(topicName);

				out.write("<tr>");
				
				//Only make queues clickable, not topics since that is not implemented...
				
				out.write("<td><a href=\"topic.jsp?topic="+ tstat.getTopicName() + "\">" + tstat.getTopicName() + "</td>");
				
				long subscriptionCount = tstat.getSubscriptionCount();
				long durableSubscriptionCount = tstat.getDurableCount();
				long nonDurableSubscriptionCount = tstat.getNondurableCount();
				
				out.write("<td>" + subscriptionCount + "</td>");
				out.write("<td>" + durableSubscriptionCount + "</td>");
				out.write("<td>" + nonDurableSubscriptionCount + "</td>");
				out.write("</tr>");
				
				subscriptionCountTotal = subscriptionCountTotal + subscriptionCount;
				durableSubscriptionCountTotal = durableSubscriptionCountTotal + durableSubscriptionCount;
				nonDurableSubscriptionCountTotal = nonDurableSubscriptionCountTotal + nonDurableSubscriptionCount;
			}
		%>
		<tr>
			<th>SUM</th>
			<%
			out.write("<th>" + subscriptionCountTotal + "</th>");
			out.write("<th>" + durableSubscriptionCountTotal + "</th>");
			out.write("<th>" + nonDurableSubscriptionCountTotal + "</th>");
			%>
		</tr>
	</table>
	
</body>
</html>
