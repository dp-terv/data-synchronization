package hu.bme.mit.v37zen.sm.adapter.xml.nodemappers;

import java.util.Map;

import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.SdpMeterAssociation;

import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class SdpMeterAssociationNodeMapper implements NodeMapper<SdpMeterAssociation> {

	public SdpMeterAssociation mapNode(Node node, int nodeNum)
			throws DOMException {
		Map<String, String> namespaces = NamespaceHandler.getNamespaces();
		
		SdpMeterAssociation sdpMeterAss = new SdpMeterAssociation();
		
		XPathExpression expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:startDate/text()", namespaces);
		sdpMeterAss.setStartDate(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:status/text()", namespaces);
		sdpMeterAss.setStatus(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:meterId/syncif:mRID/text()", namespaces);
		sdpMeterAss.setMeterAssetMRID(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:sdpId/syncif:mRID/text()", namespaces);
		sdpMeterAss.setSdpMRID(expr.evaluateAsString(node));
		
		
		return sdpMeterAss;
	}

	
}
