package se.rl.hornetqadmin.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

public class DomUtils {

	public static String prettyPrintXmlUsingDom(String input) throws Exception{
		
		StringReader reader = new StringReader(input);
        StringWriter writer = new StringWriter();
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        transformer.transform(
                        new javax.xml.transform.stream.StreamSource(reader), 
                        new javax.xml.transform.stream.StreamResult(writer));

        return writer.toString();
	}
}
