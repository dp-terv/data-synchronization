package hu.bme.mit.v37zen.sm.adapter.xml.nodemappers;

import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.AccountSDPAssociation;

import java.util.Map;

import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class AccountSDPAssociationNodeMapper implements NodeMapper<AccountSDPAssociation> {


	public AccountSDPAssociation mapNode(Node node, int nodeNum)
			throws DOMException {
		
		Map<String, String> namespaces = NamespaceHandler.getNamespaces();
		
		AccountSDPAssociation accSdpAss = new AccountSDPAssociation();
		
		XPathExpression expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:startDate/text()", namespaces);
		accSdpAss.setStartDate(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:status/text()", namespaces);
		accSdpAss.setStatus(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:accountId/syncif:mRID/text()", namespaces);
		accSdpAss.setAccountMRID(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:sdpId/syncif:mRID/text()", namespaces);
		accSdpAss.setSdpMRID(expr.evaluateAsString(node));
		
		return accSdpAss;
	}

}
