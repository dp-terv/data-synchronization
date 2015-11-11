package hu.bme.mit.v37zen.prepayment.rating;

import hu.bme.mit.v37zen.prepayment.datasync.configurators.IntervalReadingConfigurator;
import hu.bme.mit.v37zen.prepayment.util.xml.DOMNodeToString;
import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.prepayment.util.xml.XPathUtil;
import hu.bme.mit.v37zen.sm.jpa.datamodel.meterreading.IntervalReading;
import hu.bme.mit.v37zen.sm.jpa.repositories.IntervalReadingRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.w3c.dom.Node;

public class MeterDataProcessor implements Runnable, ApplicationContextAware {

	public static Logger logger = LoggerFactory.getLogger(MeterDataProcessor.class);
	
	private Node xmlNode;
	
	private NamespaceHandler namespaces;
	
	private IntervalReadingConfigurator intervalReadingConfigurator;
	
	private ApplicationContext applicationContext;

	
	public MeterDataProcessor(NamespaceHandler namespaceHandler){
		
		this.namespaces = namespaceHandler;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void run() {
		
		logger.debug((new DOMNodeToString()).nodeToString(xmlNode));
		
		if(this.xmlNode == null){
			logger.warn("Meter reading message is null!"); 
			return;
		}
		
		logger.info("Meter reading processing has started."); 
		
		try {
			List<IntervalReading> intervalReadings = new ArrayList<IntervalReading>();
			
			List<Node> meterReadings = evaluateAsNodeList(
					intervalReadingConfigurator.getMeterReadingSelector(), xmlNode);
			
			for (Node node : meterReadings) {
				String meterId = evaluateAsString(
						intervalReadingConfigurator.getMeterIdSelector(), node);
				String meterIdType = evaluateAsString(
						intervalReadingConfigurator.getMeterIdTypeSelector(), node);
				String meterIdNamespace = evaluateAsString(
						intervalReadingConfigurator.getMeterIdNamespaceSelector(), node);
				
				List<Node> intervalBlocks = evaluateAsNodeList(
						intervalReadingConfigurator.getIntervalBlockSelector(), node);
				
				for (Node intervalBlock : intervalBlocks) {
					UUID readingBlock = UUID.randomUUID();
					String readingTypeId = evaluateAsString(
							intervalReadingConfigurator.getReadingTypeIdSelector(), intervalBlock);
					Integer intervalLength = evaluateAsInteger(
							intervalReadingConfigurator.getIntervalLengthSelector(), intervalBlock); 
				
					
					List<Node> readings = evaluateAsNodeList(
							intervalReadingConfigurator.getiReadingSelector(), intervalBlock);
					
					for (Node reading : readings) {
						
						Double value = evaluateAsDouble(
								intervalReadingConfigurator.getValueSelector(), reading);
						
						Date endTime = evaluateAsDate(
								intervalReadingConfigurator.getEndTimeSelector(), reading);
						
						IntervalReading ir = new IntervalReading(null, 
								meterId, meterIdType, meterIdNamespace, readingTypeId, value, 
								null, null, null, intervalLength, readingBlock, endTime);
						
						
						intervalReadings.add(ir);
					}
				}
			}
			
			IntervalReadingRepository irr = applicationContext.getBean(IntervalReadingRepository.class);
			
			irr.save(intervalReadings);
			
			logger.info("Meter reading processing has finished."); 
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.info("Meter reading processing has failed."); 
		}		
	}
	
	protected List<Node> evaluateAsNodeList(String expression, Node node){
		return XPathUtil.evaluateAsNodeList(expression, node, namespaces.getNamespaces());
	}
	
	protected String evaluateAsString(String expression, Node node){
		return XPathUtil.evaluateAsString(expression, node, namespaces.getNamespaces());
	}
	
	protected Integer evaluateAsInteger(String expression, Node node){
		return XPathUtil.evaluateAsInteger(expression, node, namespaces.getNamespaces());
	}
	
	protected Double evaluateAsDouble(String expression, Node node){
		return XPathUtil.evaluateAsDouble(expression, node, namespaces.getNamespaces());
	}	
	protected Boolean evaluateAsBoolean(String expression, Node node){
		return XPathUtil.evaluateAsBoolean(expression, node, namespaces.getNamespaces());
	}
	protected Date evaluateAsDate(String expression, Node node){
		return XPathUtil.evaluateAsDate(expression, intervalReadingConfigurator.getDateFormat(),
				node, namespaces.getNamespaces());
	}

	public Node getXmlNode() {
		return xmlNode;
	}

	public void setXmlNode(Node xmlNode) {
		this.xmlNode = xmlNode;
	}

	public NamespaceHandler getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(NamespaceHandler namespaces) {
		this.namespaces = namespaces;
	}

	public IntervalReadingConfigurator getIntervalReadingConfigurator() {
		return intervalReadingConfigurator;
	}

	public void setIntervalReadingConfigurator(
			IntervalReadingConfigurator intervalReadingConfigurator) {
		this.intervalReadingConfigurator = intervalReadingConfigurator;
	}
	
}
