/**
 * 
 */
package hu.bme.mit.v37zen.prepayment.datasync;

import hu.bme.mit.v37zen.prepayment.util.xml.DOMNodeToString;
import hu.bme.mit.v37zen.prepayment.util.xml.NamespaceHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.w3c.dom.Document;

/**
 * @author Dániel Kiss
 *
 */
public class SyncMessageHandler implements ApplicationContextAware {
	
	public final static Logger logger = LoggerFactory.getLogger(SyncMessageHandler.class);
	
	
	private ThreadPoolTaskExecutor taskExecutor;
	
	private NamespaceHandler namespaceHandler;

	private ApplicationContext applicationContext;
	
	
	public SyncMessageHandler(ThreadPoolTaskExecutor taskExecutorPool) {
		
		this.taskExecutor = taskExecutorPool;
	}
	
	public void process(Document document) {
		
		String xml = (new DOMNodeToString()).nodeToString(document);
		logger.info("Sync message has arrived."); 
		logger.debug("[Sync message:]" + '\n' + xml);
		
		SyncMessageProcessor messageProccesor = applicationContext.getBean(SyncMessageProcessor.class);
		messageProccesor.setXmlNode(document);
		taskExecutor.execute(messageProccesor);		
		
	}


	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}


	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public NamespaceHandler getNamespaceHandler() {
		return namespaceHandler;
	}

	public void setNamespaceHandler(NamespaceHandler namespaceHandler) {
		this.namespaceHandler = namespaceHandler;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		
		this.applicationContext = applicationContext;
				
	}
}
