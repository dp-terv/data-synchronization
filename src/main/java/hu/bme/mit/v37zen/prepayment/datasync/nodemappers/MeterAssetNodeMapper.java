package hu.bme.mit.v37zen.prepayment.datasync.nodemappers;

import java.util.List;

import hu.bme.mit.v37zen.prepayment.datasync.configurators.MeterProcessorConfirugarator;
import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.MeterAsset;
import hu.bme.mit.v37zen.sm.jpa.datamodel.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathException;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.springframework.xml.xpath.XPathParseException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class MeterAssetNodeMapper implements NodeMapper<MeterAsset> {
	
	public static Logger logger = LoggerFactory.getLogger(MeterAssetNodeMapper.class);
	
	private NamespaceHandler namespaces;
	private MeterProcessorConfirugarator meterProcessorConfirugarator;
	
	
	public MeterAssetNodeMapper(MeterProcessorConfirugarator meterProcessorConfirugarator, NamespaceHandler namespaces) {
		super();
		this.namespaces = namespaces;
		this.meterProcessorConfirugarator = meterProcessorConfirugarator;
	}

	public MeterAsset mapNode(Node node, int nodeNum) throws DOMException {
		
		MeterAsset meterAsset = new MeterAsset();
		StringBuffer buff = new StringBuffer();
		
		String mRID = evaluate(meterProcessorConfirugarator.getMridSelector(), node);
		buff.append("MeterAsset MRID: "+ mRID + '\n');
		meterAsset.setMRID(mRID);

		String firstRead = evaluate(meterProcessorConfirugarator.getFirstReadSelector(), node);
		buff.append("MeterAsset FirstRead: "+ firstRead + '\n');
		meterAsset.setFirstRead(firstRead);

		String installDate = evaluate(meterProcessorConfirugarator.getInstallDateSelector(), node);
		buff.append("MeterAsset InstallDate: "+ installDate + '\n');
		meterAsset.setInstallDate(installDate);
		
		String lastRead = evaluate(meterProcessorConfirugarator.getLastReadSelector(), node);
		buff.append("MeterAsset LastRead: "+ lastRead + '\n');
		meterAsset.setLastRead(lastRead);
		
		String manufacturedDate = evaluate(meterProcessorConfirugarator.getManufacturedDateSelector(), node);
		buff.append("MeterAsset ManufacturedDate: "+ manufacturedDate + '\n');
		meterAsset.setManufacturedDate(manufacturedDate);
		
		String serialNumber = evaluate(meterProcessorConfirugarator.getSerialNumberSelector(), node);
		buff.append("MeterAsset SerialNumber: "+ serialNumber + '\n');
		meterAsset.setSerialNumber(serialNumber);
		
		String serviceType = evaluate(meterProcessorConfirugarator.getServiceTypeSelector(), node);
		buff.append("MeterAsset ServiceType: "+ serviceType + '\n');
		meterAsset.setServiceType(serviceType);
		
		String status = evaluate(meterProcessorConfirugarator.getStatusSelector(), node);
		buff.append("MeterAsset Status: "+ status + '\n');
		meterAsset.setStatus(status);
		
		String virtualInd = evaluate(meterProcessorConfirugarator.getVirtualIndSelector(), node);
		buff.append("MeterAsset VirtualInd: "+ virtualInd + '\n');
		meterAsset.setVirtualInd(virtualInd);
		
		try {
			XPathExpression expr = XPathExpressionFactory.createXPathExpression(
					".//" + meterProcessorConfirugarator.getParameterNamespace() + ":parameter",
					namespaces.getNamespaces());		
			List<Parameter>	paramList = expr.evaluate(node, new ParameterNodeMapper(namespaces.getNamespaces(),
					meterProcessorConfirugarator.getParameterNamespace()));
			for (Parameter parameter : paramList) {
				meterAsset.addParameter(parameter);
			}
			buff.append("SDP Parameters: "+ paramList.toString() + '\n');			
		} catch (XPathParseException e) {
			logger.error(e.getMessage());
		} catch (XPathException e) {
			logger.error(e.getMessage());
		}
		
		logger.debug("[New MeterAsset:]\n" + buff.toString());
		
		return meterAsset;
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

	public MeterProcessorConfirugarator getMeterProcessorConfirugarator() {
		return meterProcessorConfirugarator;
	}

	public void setMeterProcessorConfirugarator(
			MeterProcessorConfirugarator meterProcessorConfirugarator) {
		this.meterProcessorConfirugarator = meterProcessorConfirugarator;
	}


}
