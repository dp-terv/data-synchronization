package hu.bme.mit.v37zen.sm.adapter.xml.nodemappers;

import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.SdpRouteAssociation;

import java.util.Map;

import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class SdpRouteAssociationNodeMapper implements
		NodeMapper<SdpRouteAssociation> {


	public SdpRouteAssociation mapNode(Node node, int nodeNum)
			throws DOMException {
		
		Map<String, String> namespaces = NamespaceHandler.getNamespaces();
		
		SdpRouteAssociation sdpRouteAss = new SdpRouteAssociation();
		
		XPathExpression expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:startDate/text()", namespaces);
		sdpRouteAss.setStartDate(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:status/text()", namespaces);
		sdpRouteAss.setStatus(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:routeId/syncif:mRID/text()", namespaces);
		sdpRouteAss.setRouteMRID(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:sdpId/syncif:mRID/text()", namespaces);
		sdpRouteAss.setSdpMRID(expr.evaluateAsString(node));
		
		
		return sdpRouteAss;
	}

}
