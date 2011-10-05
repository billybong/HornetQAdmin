<%@page import="se.rl.hornetqadmin.HornetAccessor"%>
<%@page import="se.rl.hornetqadmin.MBeanAdmin"%>
<%@page import="javax.jms.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page
	import="java.util.Map,java.util.Iterator,se.rl.hornetqadmin.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>Contents of queue</title>
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />

    <script type="text/javascript">

        function confirmSubmit() {
            if (confirm("Are you sure?")) {
                return true;
            } else {
                return false;
            }
        }
    </script>

</head>
<body>

    <%
		String jms_queue = request.getParameter("jms_queue");
        String inputMessage = request.getParameter("txtMessage");
        boolean deleteMessages = Boolean.parseBoolean(request.getParameter("purgeQueue"));

        out.write("<h1>Contents of queue " + jms_queue + "</h1>");
        
        HornetAccessor hornet = new HornetAccessor();

        //Removing messages
        if(deleteMessages){
        	long deletedMessages = MBeanAdmin.purgeQueue(jms_queue);
        	out.write("<p>Deleted " + deletedMessages + " messages.</p>");
        }
        
        //For submitting message
        if(inputMessage != null) {
            if(!inputMessage.equals("")) {
                hornet.postJmsMessageToQueue(inputMessage, jms_queue);
            }
        }

        //Listing message
		List<Message> messages = hornet.retrieveMessages(jms_queue);
	%>
	
	<form method="post">
         <button name="purgeQueue" value="true" type="submit" onclick="return confirmSubmit()">Delete messages</button>
    </form>
	
	<table border="1" align="left" valign="top" cellpadding="5" width="80%">

		<tr style="background-color: blue; color: yellow;">
			<th>MessageID</th>
			<th>CorrelationID</th>
			<th>Timestamp</th>
			<th>JMSType</th>
			<th>DeliveryMode</th>
			<th>Payload</th>
			<th>Selection</th>
		</tr>
	
	    <%
	        for(Message message : messages) {
	            out.write("<tr>");
	            out.write("<td><a href=\"msgDetails.jsp?jms_queue=" + jms_queue + "&messageId=" + message.getJMSMessageID() + "\">" + message.getJMSMessageID() + "</a></td>");
	            out.write("<td>" + message.getJMSCorrelationID() + "</td>");
	            out.write("<td>" + new Date(message.getJMSTimestamp()).toString() + "</td>");
	            out.write("<td>" + JMSHelper.assertType(message) + "</td>"); //HornetQ JMSType field returns null, use instanceof checks instead
	            out.write("<td>" + ((message.getJMSDeliveryMode() == DeliveryMode.PERSISTENT)?"PERSISTENT":"NON_PERSISTENT") + "</td>");
	
	            if(message instanceof TextMessage){
	
	                TextMessage t_message = (TextMessage)message;
	
	                String payload = (String)t_message.getText().subSequence(0, Math.min(100, t_message.getText().length()));
	
	                payload = payload.replaceAll("(\\\\000|\\\\001)", "");
	
	                // Line feed
	                payload = payload.replaceAll("(\\\\012)", "\n");
	
	                out.write("<td>" + Html.stringToHTMLString(payload) + "</td>");
	            }else{
	                out.write("<td><Not a JMS TextMessage></td>");
	            }
	            
	            out.write("</tr>");
	            // Unprintable
	
	        }
	    %>

    </table>
    <table border="0" cellpadding="0" cellspacing="0" width="50%">
    	<form method="post">
		    <tr>
	              <td><h2>Post a message</h2></td>
	        </tr>
	    	<tr>
	        	<td><textarea name="txtMessage" cols=120 rows=15></textarea></td>
	        </tr>
	        <tr>
	        	<td><input type="submit" name="B1" value="Submit" onclick="return confirmSubmit()" /><input type="reset" name="B2" value="Reset" /></td>
	        </tr>
        </form>
    </table>
</body>
</html>
