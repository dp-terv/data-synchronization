package hu.bme.mit.v37zen.sm.adapter.xml.nodemappers;

import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.MeterAsset;
import hu.bme.mit.v37zen.sm.jpa.datamodel.Parameter;

import java.util.List;
import java.util.Map;

import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class MeterAssetNodeMapper implements NodeMapper<MeterAsset> {


	public MeterAsset mapNode(Node node, int nodeNum) throws DOMException {
		Map<String, String> namespaces = NamespaceHandler.getNamespaces();
		
		MeterAsset meter = new MeterAsset();
		
		XPathExpression expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:mRID/text()", namespaces);
		meter.setMRID(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:virtualInd/text()", namespaces);
		meter.setVirtualInd(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:serviceType/text()", namespaces);
		meter.setServiceType(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:manufacturedDate/text()", namespaces);
		meter.setManufacturedDate(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:status/text()", namespaces);
		meter.setStatus(expr.evaluateAsString(node));
		
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:parameter", namespaces);
		List<Parameter> paramList = expr.evaluate(node, new ParameterNodeMapper(namespaces, "syncif"));
		for (Parameter parameter : paramList) {
			meter.addParameter(parameter);
		}
		
				
		return meter;
	}

}
