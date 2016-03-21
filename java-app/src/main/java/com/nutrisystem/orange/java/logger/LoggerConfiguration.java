/**
 * 
 */
package com.nutrisystem.orange.java.logger;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.extras.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.nutrisystem.orange.java.ws.helper.Session;

/**
 * @author Wei Gao
 * 
 */
@Configuration
public class LoggerConfiguration {
    @Autowired
    private Resource dataLog4jXmlFile;

    @Bean
    Logger dataLogger() {
	Logger logger = LoggerFactory.getLogger(Session.class);
	Document doc;
	try {
	    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(dataLog4jXmlFile.getFile());
	} catch (SAXException | IOException | ParserConfigurationException e) {
	    throw new RuntimeException(e);
	}
	DOMConfigurator.configure(doc.getDocumentElement());
	return logger;
    }
}
