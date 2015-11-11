package hu.bme.mit.v37zen.prepayment.datasync.nodemappers;

import hu.bme.mit.v37zen.prepayment.datasync.configurators.AssociationProcessorConfigurator;
import hu.bme.mit.v37zen.prepayment.util.datetime.DateTimeUtil;
import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.SdpMeterAssociation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathException;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.springframework.xml.xpath.XPathParseException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class SdpMeterAssociationNodeMapper implements NodeMapper<SdpMeterAssociation> {
	
public static Logger logger = LoggerFactory.getLogger(SdpMeterAssociationNodeMapper.class);
	
	private NamespaceHandler namespaces;
	private AssociationProcessorConfigurator associationProcessorConfigurator;

	
	
	public SdpMeterAssociationNodeMapper(
			AssociationProcessorConfigurator associationProcessorConfigurator,
			NamespaceHandler namespaces) 
	{
		super();
		this.namespaces = namespaces;
		this.associationProcessorConfigurator = associationProcessorConfigurator;
	}

	public SdpMeterAssociation mapNode(Node node, int nodeNum) throws DOMException {
		
		SdpMeterAssociation sdpMeterAss = new SdpMeterAssociation();
		StringBuffer buff = new StringBuffer();
		
		String status = evaluate(associationProcessorConfigurator.getSdpMeterStatusSelector(), node);
		buff.append("SdpMeterAssociation Status: "+ status + '\n');
		sdpMeterAss.setStatus(status);
		
		String startDate = evaluate(associationProcessorConfigurator.getSdpMeterStartDateSelector(), node);
		buff.append("SdpMeterAssociation StartDate: "+ startDate + '\n');
		sdpMeterAss.setStartDate(DateTimeUtil.stringToDate(startDate, associationProcessorConfigurator.getDateFormat()));
		
		String meterAssetMRID = evaluate(associationProcessorConfigurator.getMeterIdSelector(), node);
		buff.append("SdpMeterAssociation MeterAssetMRID: "+ meterAssetMRID + '\n');
		sdpMeterAss.setMeterAssetMRID(meterAssetMRID);
		
		String sdpMRID = evaluate(associationProcessorConfigurator.getSdpIdSelector(), node);
		buff.append("SdpMeterAssociation SDPMRID: "+ sdpMRID + '\n');
		sdpMeterAss.setSdpMRID(sdpMRID);

		logger.debug("[New SdpMeterAssociation:]\n" + buff.toString());
		
		return sdpMeterAss;
		
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
