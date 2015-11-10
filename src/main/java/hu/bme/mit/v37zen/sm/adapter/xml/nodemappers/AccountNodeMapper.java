package hu.bme.mit.v37zen.sm.adapter.xml.nodemappers;

import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.Account;

import java.util.Map;

import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class AccountNodeMapper implements NodeMapper<Account> {


	public Account mapNode(Node node, int nodeNum) throws DOMException {
		
		Map<String, String> namespaces = NamespaceHandler.getNamespaces();
		
		Account account = new Account();
		
		XPathExpression expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:status/text()", namespaces);
		account.setStatus(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:mRID/text()", namespaces);
		account.setMRID(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:accountType/text()", namespaces);
		account.setAccountType(expr.evaluateAsString(node));
		
		expr = XPathExpressionFactory.
				createXPathExpression(".//syncif:name1/text()", namespaces);
		account.setName(expr.evaluateAsString(node));
		
		return account;
	}

}
