package hu.bme.mit.v37zen.prepayment.rating;

import hu.bme.mit.v37zen.prepayment.util.xml.DOMNodeToString;
import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.w3c.dom.Node;

public class MeterDataProcessor implements Runnable, ApplicationContextAware {

	public static Logger logger = LoggerFactory.getLogger(MeterDataProcessor.class);
	
	private Node xmlNode;
	
	private NamespaceHandler namespaces;
	
	@SuppressWarnings("unused")
	private ApplicationContext applicationContext;

	
	public MeterDataProcessor(NamespaceHandler namespaceHandler){
		
		this.namespaces = namespaceHandler;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void run() {
		
		logger.debug((new DOMNodeToString()).nodeToString(xmlNode));
		
		
	}

	public Node getXmlNode() {
		return xmlNode;
	}

	public void setXmlNode(Node xmlNode) {
		this.xmlNode = xmlNode;
	}

	public NamespaceHandler getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(NamespaceHandler namespaces) {
		this.namespaces = namespaces;
	}
	
}
