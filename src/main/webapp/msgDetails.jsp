<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="se.rl.hornetqadmin.HornetAccessor"%>
<%@page import="se.rl.hornetqadmin.MBeanAdmin"%>
<%@page import="se.rl.hornetqadmin.util.JMSHelper"%>
<%@page import="javax.jms.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map,java.util.Iterator,se.rl.hornetqadmin.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Message Details</title>
</head>
<body>
<%
		String jmsQueue = request.getParameter("jms_queue");
		String messageId = request.getParameter("messageId");
		Message jmsMessage = new HornetAccessor().retrieveMessage(jmsQueue, "JMSMessageID = '"+messageId+"'");
		
		out.write("<h1>Contents of queue " + jmsQueue + "</h1>");
	%>
<table border="1" align="left" valign="top" cellpadding="5" width="80%">
	<tr><th colspan="2" bgcolor="blue" style="color: yellow;">Properties:</th></tr>

<%


	Map<String, String> standardJmsFields = JMSHelper.jmsFieldsAsString(jmsMessage);

	for(Entry<String, String> jmsProperty : standardJmsFields.entrySet()){
		out.write("<tr>");
		out.write("<td>" + jmsProperty.getKey() + "</td>");
		out.write("<td>" + jmsProperty.getValue() + "</td>");
		out.write("</tr>");
	}

	Enumeration<Object> propertyNames = jmsMessage.getPropertyNames();
	while(propertyNames.hasMoreElements()){
		String propertyName = (String)propertyNames.nextElement();
		out.write("<tr>");
		out.write("<td>"+ propertyName +"</td>");
		out.write("<td>"+ jmsMessage.getObjectProperty(propertyName)+ "</td>");
		out.write("<tr>");
	}
%>

	<tr><th colspan="2" bgcolor="blue" style="color: yellow;">Payload:</th></tr>
	<tr><td colspan="2" style="font-family: monospace">
	<%
	if(jmsMessage instanceof TextMessage){
		
		TextMessage t_message = (TextMessage)jmsMessage;
		
		String payload = (String)t_message.getText();
		
		//Try printing XML
		try{
			payload = DomUtils.prettyPrintXmlUsingDom(payload);
		}
		catch(Exception e){
			//Message was not xml or was non-valid, not much we can do to try and parse it...
			//Do nothing
		}
		
		out.write(Html.stringToHTMLString(payload));
	}else if(jmsMessage instanceof ObjectMessage){
		out.write(Html.stringToHTMLString(((ObjectMessage)jmsMessage).getObject().toString()));
	}else{
		out.write("<Neither a JMS TextMessage or ObjectMessage>");
	}
	%>
	</td></tr>
	</table>
</body>
</html>