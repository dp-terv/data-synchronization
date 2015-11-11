package hu.bme.mit.v37zen.prepayment.datasync.nodemappers;

import hu.bme.mit.v37zen.prepayment.datasync.configurators.AssociationProcessorConfigurator;
import hu.bme.mit.v37zen.prepayment.util.datetime.DateTimeUtil;
import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.AccountContactAssociation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathException;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.springframework.xml.xpath.XPathParseException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class AccountContactAssociationNodeMapper implements NodeMapper<AccountContactAssociation> {
	
	public static Logger logger = LoggerFactory.getLogger(AccountContactAssociationNodeMapper.class);
	
	private NamespaceHandler namespaces;

	private AssociationProcessorConfigurator associationProcessorConfigurator;
	
	public AccountContactAssociationNodeMapper(
			AssociationProcessorConfigurator associationProcessorConfigurator, NamespaceHandler namespaces) {
		super();
		this.namespaces = namespaces;
		this.associationProcessorConfigurator = associationProcessorConfigurator;
	}


	public AccountContactAssociation mapNode(Node node, int nodeNum)
			throws DOMException {
		
		AccountContactAssociation accContactAss = new AccountContactAssociation();
		StringBuffer buff = new StringBuffer();
		
		String status = evaluate(associationProcessorConfigurator.getAccountContactStatusSelector(), node);
		buff.append("AccountContactAssociation Status: "+ status + '\n');
		accContactAss.setStatus(status);
		
		String startDate = evaluate(associationProcessorConfigurator.getAccountContactStartDateSelector(), node);
		buff.append("AccountContactAssociation StartDate: "+ startDate + '\n');
		accContactAss.setStartDate(DateTimeUtil.stringToDate(startDate, associationProcessorConfigurator.getDateFormat()));
		
		String accountMRID = evaluate(associationProcessorConfigurator.getAccountIdSelector(), node);
		buff.append("AccountContactAssociation AccountMRID: "+ accountMRID + '\n');
		accContactAss.setAccountMRID(accountMRID);
		
		String contactMRID = evaluate(associationProcessorConfigurator.getContactIdSelector(), node);
		buff.append("AccountContactAssociation SDPMRID: "+ contactMRID + '\n');
		accContactAss.setContactMRID(contactMRID);

		logger.debug("[New AccountContactAssociation:]\n" + buff.toString());
		
		return accContactAss;
	}
	
	protected String evaluate(String expression, Node node){
		if(expression == null || expression.isEmpty()){
			return "";
		}
		
		XPathExpression expr = null;
		
		try {
			expr = XPathExpressionFactory.createXPathExpression(expression, getNamespaces().getNamespaces());
		} catch (XPathParseException e) {
			logger.error(e.getMessage());
			return null;
		}
		
		try {
			return expr.evaluateAsString(node);
		} catch (XPathException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public NamespaceHandler getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(NamespaceHandler namespaces) {
		this.namespaces = namespaces;
	}


	public AssociationProcessorConfigurator getAssociationProcessorConfigurator() {
		return associationProcessorConfigurator;
	}


	public void setAssociationProcessorConfigurator(
			AssociationProcessorConfigurator associationProcessorConfigurator) {
		this.associationProcessorConfigurator = associationProcessorConfigurator;
	}
	
}
