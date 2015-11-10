package hu.bme.mit.v37zen.prepayment.datasync.nodemappers;

import hu.bme.mit.v37zen.prepayment.datasync.configurators.AssociationProcessorConfigurator;
import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.SdpServiceLocationAssociation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathException;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.springframework.xml.xpath.XPathParseException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class SdpServiceLocationAssociationNodeMapper implements NodeMapper<SdpServiceLocationAssociation> {
	
	public static Logger logger = LoggerFactory.getLogger(SdpServiceLocationAssociationNodeMapper.class);
	
	private NamespaceHandler namespaces;

	private AssociationProcessorConfigurator associationProcessorConfigurator;
	
	public SdpServiceLocationAssociationNodeMapper(
			AssociationProcessorConfigurator associationProcessorConfigurator, NamespaceHandler namespaces) {
		super();
		this.namespaces = namespaces;
		this.associationProcessorConfigurator = associationProcessorConfigurator;
	}

	public SdpServiceLocationAssociation mapNode(Node node, int nodeNum)
			throws DOMException {
		
		SdpServiceLocationAssociation sdpSLAss = new SdpServiceLocationAssociation();
		StringBuffer buff = new StringBuffer();
		
		String status = evaluate(associationProcessorConfigurator.getSdpServiceLocationStatusSelector(), node);
		buff.append("SdpServiceLocationAssociation Status: "+ status + '\n');
		sdpSLAss.setStatus(status);
		
		String startDate = evaluate(associationProcessorConfigurator.getSdpServiceLocationStartDateSelector(), node);
		buff.append("SdpServiceLocationAssociation StartDate: "+ startDate + '\n');
		sdpSLAss.setStartDate(startDate);
		
		String serviceLocationMRID = evaluate(associationProcessorConfigurator.getServiceLocationIdSelector(), node);
		buff.append("SdpServiceLocationAssociation MeterAssetMRID: "+ serviceLocationMRID + '\n');
		sdpSLAss.setServiceLocationMRID(serviceLocationMRID);
		
		String sdpMRID = evaluate(associationProcessorConfigurator.getSdpIdSelector(), node);
		buff.append("SdpServiceLocationAssociation SDPMRID: "+ sdpMRID + '\n');
		sdpSLAss.setSdpMRID(sdpMRID);

		logger.debug("[New SdpServiceLocationAssociation:]\n" + buff.toString());
		
		return sdpSLAss;
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
