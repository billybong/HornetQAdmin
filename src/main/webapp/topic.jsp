<%@page import="se.rl.hornetqadmin.HornetAccessor"%>
<%@page import="se.rl.hornetqadmin.MBeanAdmin"%>
<%@page import="se.rl.hornetqadmin.bo.Subscription"%>
<%@page import="javax.jms.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page
	import="java.util.Map,java.util.Iterator,se.rl.hornetqadmin.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>Topic view</title>
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />

    <script type="text/javascript">

        function confirmSubmit() {
            if (confirm("Are you sure you want to remove the subscription?")) {
                return true;
            } else {
                return false;
            }
        }
    </script>

</head>
<body>

    <%
		String topic = request.getParameter("topic");
        String droppedSubClientId = request.getParameter("clientId");
    	String droppedSubName = request.getParameter("subscription");
    
        out.write("<h1>Subscriptions for Topic " + topic + "</h1>");
        
        //Drop subscriptions
        if((droppedSubClientId != null && !droppedSubClientId.equals(""))
    			&& droppedSubName != null && !droppedSubName.equals("")){
    		String result = MBeanAdmin.dropSubscription(topic, droppedSubName, droppedSubClientId);
    		
    		out.write(result);
    	}
	%>
	
	
	<table border="1" align="left" valign="top" cellpadding="5" width="80%">

		<tr style="background-color: blue; color: yellow;">
			<th>Subscription name</th>
			<th>DeliveryCount</th>
			<th>ClientID</th>
			<th>QueueName</th>
			<th>MessageCount</th>
			<th>IsDurable</th>
			<th>ConsumerCount</th>
			<th>Drop subscription</th>
		</tr>
	
	    <%
	        for(Subscription s : MBeanAdmin.queryTopic(topic).getSubscriptions()) {
	            out.write("<tr>");
	            out.write("<td>" + s.getName() + "</td>");
	            out.write("<td>" + s.getDeliveryCount() + "</td>");
	            out.write("<td>" + s.getClientId() + "</td>");
	            out.write("<td>" + s.getQueueName() + "</td>");
	            out.write("<td>" + s.getMessageCount() + "</td>");
	            out.write("<td>" + s.isDurable() + "</td>");
	            out.write("<td>" + s.getConsumerCount() + "</td>");
	            
	            %>
	    <td>
		    <form method="post">
		    	<input type="hidden" name="topic" value="<% out.write(topic); %>"/>
		    	<input type="hidden" name="clientId" value="<% out.write(s.getClientId()); %>"/>
		    	<input type="hidden" name="subscription" value="<% out.write(s.getName()); %>"/>
	         	<button name="dropSubscription" value="true" type="submit" onclick="return confirmSubmit()">Drop</button>
	    	</form>
    	</td>        
	            <%
				out.write("</tr>");
	        }
	    %>

    </table>
</body>
</html>
