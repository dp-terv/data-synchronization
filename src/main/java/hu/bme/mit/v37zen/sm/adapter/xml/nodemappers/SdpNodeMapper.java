package hu.bme.mit.v37zen.sm.adapter.xml.nodemappers;

import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.Parameter;
import hu.bme.mit.v37zen.sm.jpa.datamodel.PremiseId;
import hu.bme.mit.v37zen.sm.jpa.datamodel.ServiceDeliveryPoint;

import java.util.List;
import java.util.Map;

import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class SdpNodeMapper implements NodeMapper<ServiceDeliveryPoint>   {
	
	
	public ServiceDeliveryPoint mapNode(Node node, int nodeNum)
			throws DOMException {
		
		Map<String, String> namespaces = NamespaceHandler.getNamespaces();
		
		ServiceDeliveryPoint sdp = new ServiceDeliveryPoint();
				
		XPathExpression expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:mRID/text()", namespaces);
		sdp.setMRID(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:virtualInd/text()", namespaces);
		sdp.setVirtualInd(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:serviceType/text()", namespaces);
		sdp.setServiceType(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:premiseId/syncif:mRID/text()", namespaces);
		String premiseIdMRID = expr.evaluateAsString(node);
		sdp.addPremiseId(new PremiseId(premiseIdMRID));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:parameter", namespaces);
		List<Parameter> paramList = expr.evaluate(node, new ParameterNodeMapper(namespaces, "syncif"));
		for (Parameter parameter : paramList) {
			sdp.addParameter(parameter);
		}
				
		return sdp;
	}
	

}
