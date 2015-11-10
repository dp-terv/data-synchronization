/**
 * 
 */
package hu.bme.mit.v37zen.prepayment.integration;

import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.xml.xpath.XPathException;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.springframework.xml.xpath.XPathParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Kiss Dániel
 *
 */
@ManagedResource(
        objectName="bean:name=DOMDocumentHandler",
        description="Dispatch xml dom documents.")
public class DOMDocumentHandler {
	
	public final static Logger logger = LoggerFactory.getLogger(DOMDocumentHandler.class);
	
	private MessageChannel toSyncDataHandlerChannel;
	private MessageChannel toMeterDataHandlerChannel;
	
	private String syncMessageSelectorString = "/syncif:SDPSyncMessage/syncif:header/syncif:noun/text()";
	private String meterDataMessageSelectorString = "/mdif:MeterDataMessage/mdif:Header/mdif:noun/text()";
	
	private String syncMessageDistinctiveValue = "SDPSync";
	private String meterDataMessageDistinctiveValue = "MeterData";
	
	private NamespaceHandler namespaceHandler;
	
	
	public DOMDocumentHandler(MessageChannel toSyncDataHandlerChannel, MessageChannel toMeterDataHandlerChannel){
		this.toMeterDataHandlerChannel = toMeterDataHandlerChannel;
		this.toSyncDataHandlerChannel = toSyncDataHandlerChannel;
	}
	
	public void process(Document document) {
				
		//String xml = (new DOMNodeToString()).nodeToString(document);
		logger.info("XML message has arrived.");
		//logger.debug('\n' + xml);
		
		document.getDocumentElement().normalize();
		Node node = document;
		
		try {
			XPathExpression expr = XPathExpressionFactory.createXPathExpression(
					syncMessageSelectorString, namespaceHandler.getNamespaces());
			if (expr.evaluateAsString(node).equalsIgnoreCase(syncMessageDistinctiveValue)) {
				this.toSyncDataHandlerChannel.send(new GenericMessage<Document>(document));
			}
			
			expr = XPathExpressionFactory.createXPathExpression(
					meterDataMessageSelectorString, namespaceHandler.getNamespaces());
			if (expr.evaluateAsString(node).equalsIgnoreCase(meterDataMessageDistinctiveValue)) {
				this.toMeterDataHandlerChannel.send(new GenericMessage<Document>(document));
			}
		} catch (XPathParseException e) {
			logger.error(e.getMessage());
		} catch (XPathException e) {
			logger.error(e.getMessage());
		}
		
		
	}

	public MessageChannel getToSyncDataHandlerChannel() {
		return toSyncDataHandlerChannel;
	}

	public void setToSyncDataHandlerChannel(MessageChannel toSyncDataHandlerChannel) {
		this.toSyncDataHandlerChannel = toSyncDataHandlerChannel;
	}

	public MessageChannel getToMeterDataHandlerChannel() {
		return toMeterDataHandlerChannel;
	}

	public void setToMeterDataHandlerChannel(
			MessageChannel toMeterDataHandlerChannel) {
		this.toMeterDataHandlerChannel = toMeterDataHandlerChannel;
	}

	@ManagedAttribute
	public String getSyncMessageSelectorString() {
		return syncMessageSelectorString;
	}

	@ManagedAttribute
	public void setSyncMessageSelectorString(String syncMessageSelectorString) {
		this.syncMessageSelectorString = syncMessageSelectorString;
	}

	@ManagedAttribute
	public String getMeterDataMessageSelectorString() {
		return meterDataMessageSelectorString;
	}

	@ManagedAttribute
	public void setMeterDataMessageSelectorString(
			String meterDataMessageSelectorString) {
		this.meterDataMessageSelectorString = meterDataMessageSelectorString;
	}

	@ManagedAttribute
	public String getSyncMessageDistinctiveValue() {
		return syncMessageDistinctiveValue;
	}

	@ManagedAttribute
	public void setSyncMessageDistinctiveValue(String syncMessageDistinctiveValue) {
		this.syncMessageDistinctiveValue = syncMessageDistinctiveValue;
	}

	@ManagedAttribute
	public String getMeterDataMessageDistinctiveValue() {
		return meterDataMessageDistinctiveValue;
	}

	@ManagedAttribute
	public void setMeterDataMessageDistinctiveValue(
			String meterDataMessageDistinctiveValue) {
		this.meterDataMessageDistinctiveValue = meterDataMessageDistinctiveValue;
	}

	
	public NamespaceHandler getNamespaceHandler() {
		return namespaceHandler;
	}

	
	public void setNamespaceHandler(NamespaceHandler namespaceHandler) {
		this.namespaceHandler = namespaceHandler;
	}
	
	
	
}
