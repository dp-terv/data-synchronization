package hu.bme.mit.v37zen.sm.adapter.xml.nodemappers;

import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.ServiceLocation;

import java.util.Map;

import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class ServiceLocationNodeMapper implements NodeMapper<ServiceLocation> {
	

	public ServiceLocation mapNode(Node node, int nodeNum) throws DOMException {
		Map<String, String> namespaces = NamespaceHandler.getNamespaces();
		
		ServiceLocation sl = new ServiceLocation();
		
		XPathExpression expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:mRID/text()", namespaces);
		sl.setMRID(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:addressGeneral/text()", namespaces);
		sl.setAddressGeneral(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:city/text()", namespaces);
		sl.setCity(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:country/text()", namespaces);
		sl.setCountry(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:postalCode/text()", namespaces);
		sl.setPostalCode(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:stateOrProvince/text()", namespaces);
		sl.setStateOrProvince(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:timeZone/text()", namespaces);
		sl.setTimeZone(expr.evaluateAsString(node));
		
				
		return sl;
	}

}
