package hu.bme.mit.v37zen.prepayment.integration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.xml.xpath.XPathException;
import org.springframework.xml.xpath.XPathParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Kiss Dániel
 *
 */
@ManagedResource(
        objectName="bean:name=ContentBasedRouter",
        description="Route xml dom documents.")
public class ContentBasedRouter {
	
	public final static Logger logger = LoggerFactory.getLogger(ContentBasedRouter.class);

	//private NamespaceHandler namespaceHandler;
	
	private Map<String, RoutingRule> routingTable = new HashMap<String, RoutingRule>();
	
	public void route(Document document) {
		
		//String xml = (new DOMNodeToString()).nodeToString(document);
		logger.info("XML message has arrived.");
		//logger.debug('\n' + xml);
		
		document.getDocumentElement().normalize();
		Node node = document;
		
		try {
			
			for (RoutingRule rr : this.routingTable.values()) {
				
				boolean isContentMatch = rr.evaluate(node);
				if(isContentMatch){
					
					rr.getRoute().send(new GenericMessage<Document>(document));
				}
			}
			
		} catch (XPathParseException e) {
			logger.error(e.getMessage());
		} catch (XPathException e) {
			logger.error(e.getMessage());
		}
		
	}
	
	public ContentBasedRouter() {	
	}
	
	public ContentBasedRouter(Map<String, RoutingRule> routingTable) {
		if(routingTable == null ){
			this.routingTable = new HashMap<String, RoutingRule>();
		}
		this.routingTable = routingTable;
	}
	
	public RoutingRule putRoutingRule(String ruleName, RoutingRule rule){
		return this.routingTable.put(ruleName, rule);
	}
	
	public RoutingRule removeRoutingRule(String ruleName){
		return this.routingTable.remove(ruleName);
	}

	@ManagedAttribute
	public Map<String, RoutingRule> getRoutingTable() {
		return routingTable;
	}
	
}
