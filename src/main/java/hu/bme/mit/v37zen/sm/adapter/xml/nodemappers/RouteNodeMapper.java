package hu.bme.mit.v37zen.sm.adapter.xml.nodemappers;

import java.util.Map;

import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.Route;

import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class RouteNodeMapper implements NodeMapper<Route> {

	
	public Route mapNode(Node node, int nodeNum) throws DOMException {
		Map<String, String> namespaces = NamespaceHandler.getNamespaces();
		
		Route route = new Route();
		
		XPathExpression expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:mRID/text()", namespaces);
		route.setMRID(expr.evaluateAsString(node));
				
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:status/text()", namespaces);
		route.setStatus(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:routeType/text()", namespaces);
		route.setRouteType(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:billingCycle/text()", namespaces);
		route.setBillingCycle(expr.evaluateAsString(node));		
		
		return route;
	}

}
