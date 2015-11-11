package hu.bme.mit.v37zen.prepayment.datasync.nodemappers;

import hu.bme.mit.v37zen.prepayment.datasync.configurators.AssociationProcessorConfigurator;
import hu.bme.mit.v37zen.prepayment.util.datetime.DateTimeUtil;
import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.AccountSDPAssociation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathException;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.springframework.xml.xpath.XPathParseException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class AccountSdpAssociationNodeMapper implements NodeMapper<AccountSDPAssociation> {
	
	public static Logger logger = LoggerFactory.getLogger(AccountNodeMapper.class);
	
	private NamespaceHandler namespaces;
	private AssociationProcessorConfigurator associationProcessorConfigurator;

	
	
	public AccountSdpAssociationNodeMapper(
			AssociationProcessorConfigurator associationProcessorConfigurator,
			NamespaceHandler namespaces) 
	{
		super();
		this.namespaces = namespaces;
		this.associationProcessorConfigurator = associationProcessorConfigurator;
	}



	public AccountSDPAssociation mapNode(Node node, int nodeNum)
			throws DOMException {
		
		
		AccountSDPAssociation accSdpAss = new AccountSDPAssociation();
		StringBuffer buff = new StringBuffer();
		
		String status = evaluate(associationProcessorConfigurator.getAccountSdpStatusSelector(), node);
		buff.append("AccountSDPAssociation Status: "+ status + '\n');
		accSdpAss.setStatus(status);
		
		String startDate = evaluate(associationProcessorConfigurator.getAccountSdpStartDateSelector(), node);
		buff.append("AccountSDPAssociation StartDate: "+ startDate + '\n');
		accSdpAss.setStartDate(DateTimeUtil.stringToDate(startDate, associationProcessorConfigurator.getDateFormat()));
		
		String accountMRID = evaluate(associationProcessorConfigurator.getAccountIdSelector(), node);
		buff.append("AccountSDPAssociation AccountMRID: "+ accountMRID + '\n');
		accSdpAss.setAccountMRID(accountMRID);
		
		String sdpMRID = evaluate(associationProcessorConfigurator.getSdpIdSelector(), node);
		buff.append("AccountSDPAssociation SDPMRID: "+ sdpMRID + '\n');
		accSdpAss.setSdpMRID(sdpMRID);

		logger.debug("[New AccountSDPAssociation:]\n" + buff.toString());
		
		return accSdpAss;
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
