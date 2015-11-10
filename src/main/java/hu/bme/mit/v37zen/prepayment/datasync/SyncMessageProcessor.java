package hu.bme.mit.v37zen.prepayment.datasync;

import hu.bme.mit.v37zen.prepayment.datasync.configurators.AccountProcessorConfigurator;
import hu.bme.mit.v37zen.prepayment.datasync.configurators.AssociationProcessorConfigurator;
import hu.bme.mit.v37zen.prepayment.datasync.configurators.ContactProcessorConfigurator;
import hu.bme.mit.v37zen.prepayment.datasync.configurators.MeterProcessorConfirugarator;
import hu.bme.mit.v37zen.prepayment.datasync.configurators.SdpProcessorConfigurator;
import hu.bme.mit.v37zen.prepayment.datasync.configurators.ServiceLocationProcessorConfigurator;
import hu.bme.mit.v37zen.prepayment.datasync.nodemappers.AccountContactAssociationNodeMapper;
import hu.bme.mit.v37zen.prepayment.datasync.nodemappers.AccountNodeMapper;
import hu.bme.mit.v37zen.prepayment.datasync.nodemappers.AccountSdpAssociationNodeMapper;
import hu.bme.mit.v37zen.prepayment.datasync.nodemappers.ContactNodeMapper;
import hu.bme.mit.v37zen.prepayment.datasync.nodemappers.MeterAssetNodeMapper;
import hu.bme.mit.v37zen.prepayment.datasync.nodemappers.SdpMeterAssociationNodeMapper;
import hu.bme.mit.v37zen.prepayment.datasync.nodemappers.SdpNodeMapper;
import hu.bme.mit.v37zen.prepayment.datasync.nodemappers.SdpServiceLocationAssociationNodeMapper;
import hu.bme.mit.v37zen.prepayment.datasync.nodemappers.ServiceLocationNodeMapper;
import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;
import hu.bme.mit.v37zen.sm.jpa.datamodel.Account;
import hu.bme.mit.v37zen.sm.jpa.datamodel.AccountContactAssociation;
import hu.bme.mit.v37zen.sm.jpa.datamodel.AccountSDPAssociation;
import hu.bme.mit.v37zen.sm.jpa.datamodel.Contact;
import hu.bme.mit.v37zen.sm.jpa.datamodel.MeterAsset;
import hu.bme.mit.v37zen.sm.jpa.datamodel.SdpMeterAssociation;
import hu.bme.mit.v37zen.sm.jpa.datamodel.SdpServiceLocationAssociation;
import hu.bme.mit.v37zen.sm.jpa.datamodel.ServiceDeliveryPoint;
import hu.bme.mit.v37zen.sm.jpa.datamodel.ServiceLocation;
import hu.bme.mit.v37zen.sm.jpa.repositories.AccountContactAssociationRepository;
import hu.bme.mit.v37zen.sm.jpa.repositories.AccountRepository;
import hu.bme.mit.v37zen.sm.jpa.repositories.AccountSDPAssociationRepository;
import hu.bme.mit.v37zen.sm.jpa.repositories.ContactRepository;
import hu.bme.mit.v37zen.sm.jpa.repositories.MeterAssetRepository;
import hu.bme.mit.v37zen.sm.jpa.repositories.SdpMeterAssociationRepository;
import hu.bme.mit.v37zen.sm.jpa.repositories.SdpServiceLocationAssociationRepository;
import hu.bme.mit.v37zen.sm.jpa.repositories.ServiceDeliveryPointRepository;
import hu.bme.mit.v37zen.sm.jpa.repositories.ServiceLocationRepository;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.xml.xpath.XPathException;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.springframework.xml.xpath.XPathParseException;
import org.w3c.dom.Node;

/**
 * 
 * @author Kiss Dániel
 *
 */
public class SyncMessageProcessor implements Runnable, ApplicationContextAware {
	
	public static Logger logger = LoggerFactory.getLogger(SyncMessageProcessor.class);
	
	private ApplicationContext applicationContext;
	
	private Node xmlNode;
	
	private NamespaceHandler namespaces;
	
	private AccountProcessorConfigurator accountProcessorConfigurator;
	private SdpProcessorConfigurator sdpProcessorConfigurator;
	private AssociationProcessorConfigurator associationProcessorConfigurator;
	private MeterProcessorConfirugarator meterProcessorConfirugarator;
	private ContactProcessorConfigurator contactProcessorConfigurator;
	private ServiceLocationProcessorConfigurator serviceLocationProcessorConfigurator;
	
	public SyncMessageProcessor(NamespaceHandler namespaces, 
			AccountProcessorConfigurator accountProcessorConfigurator,
			SdpProcessorConfigurator sdpProcessorConfigurator,
			AssociationProcessorConfigurator associationProcessorConfigurator,
			MeterProcessorConfirugarator meterProcessorConfirugarator,
			ContactProcessorConfigurator contactProcessorConfigurator,
			ServiceLocationProcessorConfigurator serviceLocationProcessorConfigurator) 
	{
		super();
		this.namespaces = namespaces;
		this.accountProcessorConfigurator = accountProcessorConfigurator;
		this.sdpProcessorConfigurator = sdpProcessorConfigurator;
		this.associationProcessorConfigurator = associationProcessorConfigurator;
		this.meterProcessorConfirugarator = meterProcessorConfirugarator;
		this.contactProcessorConfigurator = contactProcessorConfigurator;
		this.serviceLocationProcessorConfigurator = serviceLocationProcessorConfigurator;
		
	}



	public void run() {
		if(this.xmlNode == null){
			logger.warn("Sync message is null!"); 
			return;
		}
		
		logger.info("Sync message processing has started."); 
		
		
		this.processAccounts(xmlNode);
		this.processSDP(xmlNode);
		this.processMeterAssets(xmlNode);
		this.processContacts(xmlNode);
		this.processServiceLocations(xmlNode);
		this.processSDPMeterAssetAssociations(xmlNode);
		this.processAccountSDPAssociations(xmlNode);
		this.processSDPServiceLocationAssociations(xmlNode);
		this.processAccountContactAssociations(xmlNode);
		
		
		logger.info("Sync message processing has finished."); 
		
	}
	
	
	protected XPathExpression createXPathExpression(String expression, Map<String,String> namespaces){
		if(expression == null || expression.isEmpty()){
			return null;
		}		
		try{	
			return XPathExpressionFactory.createXPathExpression(expression, namespaces);
		} catch (XPathParseException e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	protected void processAccounts(Node node){
		XPathExpression expr = createXPathExpression(this.accountProcessorConfigurator.getAccountSelector(), namespaces.getNamespaces());
		if (expr == null){
			return;
		}
		List<Account> accountList;		
		try {
			accountList = expr.evaluate(node, new AccountNodeMapper(accountProcessorConfigurator, namespaces));
			
		} catch (XPathException e) {
			logger.error(e.getMessage());
			return;
		}		
		AccountRepository accountRepository = applicationContext.getBean(AccountRepository.class);
		for (Account account : accountList) {	
			accountRepository.save(account);			
		}
		
	}
	
	private void processSDP(Node node) {
		XPathExpression expr = createXPathExpression(this.sdpProcessorConfigurator.getSdpSelector(), namespaces.getNamespaces());
		if (expr == null){
			return;
		}
		List<ServiceDeliveryPoint> sdpList;
		try {
			sdpList = expr.evaluate(node, new SdpNodeMapper(sdpProcessorConfigurator, namespaces));
		} catch (XPathException e) {
			logger.error(e.getMessage());
			return;
		}
		ServiceDeliveryPointRepository sdpRepo = applicationContext.getBean(ServiceDeliveryPointRepository.class);
		for (ServiceDeliveryPoint sdp : sdpList) {		
			sdpRepo.save(sdp);			
		}
	}

	protected void processAccountSDPAssociations(Node node){
		XPathExpression expr = createXPathExpression(this.associationProcessorConfigurator.getAccountSdpAssociationSelector(), namespaces.getNamespaces());
		if (expr == null){
			return;
		}
		List<AccountSDPAssociation> accSdpAssList;
		try {
			accSdpAssList = expr.evaluate(node, new AccountSdpAssociationNodeMapper(associationProcessorConfigurator, namespaces));
		} catch (XPathException e) {
			logger.error(e.getMessage());
			return;
		}
		AccountSDPAssociationRepository accSdpAssRepo = applicationContext.getBean(AccountSDPAssociationRepository.class);
		for (AccountSDPAssociation accSdpAss : accSdpAssList) {			
			accSdpAssRepo.save(accSdpAss);			
		}
	}
	
	protected void processMeterAssets(Node node){
		XPathExpression expr = createXPathExpression(this.meterProcessorConfirugarator.getMeterAssetSelector(), namespaces.getNamespaces());
		if (expr == null){
			return;
		}
		List<MeterAsset> meterList;
		try {
			meterList = expr.evaluate(node, new MeterAssetNodeMapper(this.meterProcessorConfirugarator, namespaces));
		} catch (XPathException e) {
			logger.error(e.getMessage());
			return;
		}
		MeterAssetRepository meterRepo = applicationContext.getBean(MeterAssetRepository.class);
		for (MeterAsset meter : meterList) {
			meterRepo.save(meter);
		}
	}
	
	protected void processSDPMeterAssetAssociations(Node node){
		XPathExpression expr = createXPathExpression(this.associationProcessorConfigurator.getSdpMeterAssociationSelector(), namespaces.getNamespaces());
		if (expr == null){
			return;
		}
		List<SdpMeterAssociation> sdpMeterAssList;
		try {
			sdpMeterAssList = expr.evaluate(node, new SdpMeterAssociationNodeMapper(this.associationProcessorConfigurator, namespaces));
		} catch (XPathException e) {
			logger.error(e.getMessage());
			return;
		}
		SdpMeterAssociationRepository sdpMeterAssRepo = applicationContext.getBean(SdpMeterAssociationRepository.class);
		for (SdpMeterAssociation sdpMeterAss : sdpMeterAssList) {
			sdpMeterAssRepo.save(sdpMeterAss);
		}
	}
	
	protected void processContacts(Node node){
		XPathExpression expr = createXPathExpression(this.contactProcessorConfigurator.getContactSelector(), namespaces.getNamespaces());
		if (expr == null){
			return;
		}
		List<Contact> contactList;
		try {
			contactList = expr.evaluate(node, new ContactNodeMapper(contactProcessorConfigurator, namespaces));
		} catch (XPathException e) {
			logger.error(e.getMessage());
			return;
		}
		ContactRepository contactRepo = applicationContext.getBean(ContactRepository.class);
		for (Contact contact : contactList) {
			contactRepo.save(contact);
		}
	}
	
	protected void processServiceLocations(Node node){
		XPathExpression expr = createXPathExpression(this.serviceLocationProcessorConfigurator.getServiceLocationSelector(), namespaces.getNamespaces());
		if (expr == null){
			return;
		}
		List<ServiceLocation> slList;
		try {
			slList = expr.evaluate(node, new ServiceLocationNodeMapper(serviceLocationProcessorConfigurator, namespaces));
		} catch (XPathException e) {
			logger.error(e.getMessage());
			return;
		}
		ServiceLocationRepository slRepo = applicationContext.getBean(ServiceLocationRepository.class);
		for (ServiceLocation sl : slList) {
			slRepo.save(sl);
		}
	}
	
	protected void processSDPServiceLocationAssociations(Node node){
		XPathExpression expr = createXPathExpression(this.associationProcessorConfigurator.getSdpServiceLocationAssociationSelector(), namespaces.getNamespaces());
		if (expr == null){
			return;
		}
		List<SdpServiceLocationAssociation> sdpSlList;
		try {
			sdpSlList = expr.evaluate(node, 
					new SdpServiceLocationAssociationNodeMapper(associationProcessorConfigurator, namespaces));
		} catch (XPathException e) {
			logger.error(e.getMessage());
			return;
		}
		SdpServiceLocationAssociationRepository slRepo = applicationContext.getBean(SdpServiceLocationAssociationRepository.class);
		for (SdpServiceLocationAssociation sdpSl : sdpSlList) {
			slRepo.save(sdpSl);
		}
	}

	protected void processAccountContactAssociations(Node node){
		XPathExpression expr = createXPathExpression(this.associationProcessorConfigurator.getAccountContactAssociationSelector(), namespaces.getNamespaces());
		if (expr == null){
			return;
		}
		List<AccountContactAssociation> accContAssList;
		try {
			accContAssList = expr.evaluate(node, 
					new AccountContactAssociationNodeMapper(associationProcessorConfigurator, namespaces));
		} catch (XPathException e) {
			logger.error(e.getMessage());
			return;
		}
		AccountContactAssociationRepository slRepo = applicationContext.getBean(AccountContactAssociationRepository.class);
		for (AccountContactAssociation accContAss : accContAssList) {
			slRepo.save(accContAss);
		}
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

	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		
		this.applicationContext = applicationContext;		
	}
	

	public AccountProcessorConfigurator getSyncMessageProcessorConfigurator() {
		return accountProcessorConfigurator;
	}


	public void setSyncMessageProcessorConfigurator(
			AccountProcessorConfigurator accountProcessorConfigurator) {
		this.accountProcessorConfigurator = accountProcessorConfigurator;
	}



	public SdpProcessorConfigurator getSyncMessageSDPProcessorConfigurator() {
		return sdpProcessorConfigurator;
	}



	public void setSyncMessageSDPProcessorConfigurator(
			SdpProcessorConfigurator sdpProcessorConfigurator) {
		this.sdpProcessorConfigurator = sdpProcessorConfigurator;
	}



	/**
	 * @return the associationProcessorConfigurator
	 */
	public AssociationProcessorConfigurator getAssociationProcessorConfigurator() {
		return associationProcessorConfigurator;
	}



	/**
	 * @param associationProcessorConfigurator the associationProcessorConfigurator to set
	 */
	public void setAssociationProcessorConfigurator(
			AssociationProcessorConfigurator associationProcessorConfigurator) {
		this.associationProcessorConfigurator = associationProcessorConfigurator;
	}



	public MeterProcessorConfirugarator getMeterProcessorConfirugarator() {
		return meterProcessorConfirugarator;
	}



	public void setMeterProcessorConfirugarator(
			MeterProcessorConfirugarator meterProcessorConfirugarator) {
		this.meterProcessorConfirugarator = meterProcessorConfirugarator;
	}



	public ContactProcessorConfigurator getContactProcessorConfigurator() {
		return contactProcessorConfigurator;
	}



	public void setContactProcessorConfigurator(
			ContactProcessorConfigurator contactProcessorConfigurator) {
		this.contactProcessorConfigurator = contactProcessorConfigurator;
	}



	public ServiceLocationProcessorConfigurator getServiceLocationProcessorConfigurator() {
		return serviceLocationProcessorConfigurator;
	}



	public void setServiceLocationProcessorConfigurator(
			ServiceLocationProcessorConfigurator serviceLocationProcessorConfigurator) {
		this.serviceLocationProcessorConfigurator = serviceLocationProcessorConfigurator;
	}

	
	

}
