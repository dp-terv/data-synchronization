package hu.bme.mit.v37zen.prepayment.integration;

import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.xml.xpath.XPathException;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.springframework.xml.xpath.XPathParseException;
import org.w3c.dom.Node;

public class RoutingRule {	
	public final static Logger logger = LoggerFactory.getLogger(RoutingRule.class);

	private NamespaceHandler namespaceHandler;
	
	private String contentSelectorString;

	private Pattern exceptedContent;

	private MessageChannel route;

	public RoutingRule(String contentSelectorString,
			String exceptedContentRegex, MessageChannel route, NamespaceHandler namespaces) {
		super();
		this.contentSelectorString = contentSelectorString;
		this.exceptedContent = Pattern.compile(exceptedContentRegex);
		this.namespaceHandler = namespaces;
		this.route = route;
	}

	public RoutingRule(String contentSelectorString, Pattern exceptedContent,
			MessageChannel route, NamespaceHandler namespaces) {
		super();
		this.contentSelectorString = contentSelectorString;
		this.exceptedContent = exceptedContent;
		this.namespaceHandler = namespaces;
		this.route = route;
	}

	public String getContentSelectorString() {
		return contentSelectorString;
	}

	public void setContentSelectorString(String contentSelectorString) {
		this.contentSelectorString = contentSelectorString;
	}

	public Pattern getExceptedContent() {
		return exceptedContent;
	}

	public void setExceptedContent(String exceptedContentRegex) {
		this.exceptedContent = Pattern.compile(exceptedContentRegex);
	}

	public void setExceptedContent(Pattern exceptedContent) {
		this.exceptedContent = exceptedContent;
	}

	public MessageChannel getRoute() {
		return route;
	}

	public void setRoute(MessageChannel route) {
		this.route = route;
	}

	public boolean evaluate(Node node) {
		try {
			logger.debug("");
			XPathExpression expr = XPathExpressionFactory
					.createXPathExpression(this.contentSelectorString,
							namespaceHandler.getNamespaces());

			return exceptedContent.matcher(expr.evaluateAsString(node))
					.matches();

		} catch (XPathParseException e) {
			logger.error(e.getMessage());
		} catch (XPathException e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	public NamespaceHandler getNamespaceHandler() {
		return namespaceHandler;
	}

	public void setNamespaceHandler(NamespaceHandler namespaceHandler) {
		this.namespaceHandler = namespaceHandler;
	}
}
