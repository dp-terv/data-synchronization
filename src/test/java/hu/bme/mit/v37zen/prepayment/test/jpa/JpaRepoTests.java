package hu.bme.mit.v37zen.prepayment.test.jpa;

import hu.bme.mit.v37zen.sm.jpa.datamodel.Account;
import hu.bme.mit.v37zen.sm.jpa.datamodel.Parameter;
import hu.bme.mit.v37zen.sm.jpa.datamodel.ServiceDeliveryPoint;
import hu.bme.mit.v37zen.sm.jpa.repositories.AccountRepository;
import hu.bme.mit.v37zen.sm.jpa.repositories.ServiceDeliveryPointRepository;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:/META-INF/spring/test-context.xml", "classpath:/META-INF/spring/jdbc-jpa.xml"})
public class JpaRepoTests {
	
	@Resource
	private Environment environment;

	@Autowired
	private AccountRepository accRepo;
	
	@Autowired
	private ServiceDeliveryPointRepository sdpRepo;
	
	@AfterClass
	public static void sleepFor5Min(){
		
		try {
			Thread.sleep(300000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void accRepoTest(){
		
		String mRID = "Acc01";
		String name = "Account 001";
		String status = "active";
		String type = "Customer";
		
		Account account = new Account(mRID);
		account.setName(name);
		account.setStatus(status);
		account.setAccountType(type);
		
		accRepo.saveAndFlush(account);
		
		Assert.assertEquals(account, accRepo.findByMRID(mRID).get(0));
		Assert.assertEquals(1, accRepo.findByMRID(mRID).size());
		
		Assert.assertTrue(accRepo.findByName(name).contains(account));
		
		Assert.assertTrue(accRepo.findByStatus(status).contains(account));
		
		Assert.assertTrue(accRepo.findByAccountType(type).contains(account));
		
		Assert.assertTrue(accRepo.findAll().contains(account));
		
	}
	
	@Test
	public void sdpRepoTest(){
		
		String mRID = "SDP01";
		String virtualInd = "sdp01-virtInd";
		String serviceType = "GASS";
		String premiseId = "premise01";
		Parameter parameter = new Parameter("param1", "value1", "active", (new Date()).toString());
		
		
		ServiceDeliveryPoint sdp = new ServiceDeliveryPoint(mRID, virtualInd, serviceType, premiseId, parameter);	
		sdpRepo.saveAndFlush(sdp);
		
		Assert.assertEquals(sdp, sdpRepo.findByMRID(mRID).get(0));
		Assert.assertEquals(1, sdpRepo.findByMRID(mRID).size());
		
		Assert.assertTrue(sdpRepo.findByServiceType(serviceType).contains(sdp));
		
		Assert.assertTrue(sdpRepo.findByVirtualInd(virtualInd).contains(sdp));
		
		Assert.assertTrue(sdpRepo.findAll().contains(sdp));
		
	}

	
}
