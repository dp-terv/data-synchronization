package hu.bme.mit.v37zen.sm.jpa.datamodel;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class MeterAsset extends BaseEntity {

	private static final long serialVersionUID = -8764900526122408786L;
	
	private String manufacturedDate;
	
	private String status;
	
	private String virtualInd;
	
	private String serviceType;
	
	private String serialNumber;
	
	private String installDate;
	
	private String firstRead;
	
	private String lastRead;

	@OneToMany(cascade={CascadeType.ALL})
	List<Parameter> parameters = new ArrayList<Parameter>();
	
	public MeterAsset(){
		super();
	}

	public MeterAsset(String mRID){
		super(mRID);
	}
		

	public MeterAsset(String mRID, String manufacturedDate, String status,
			String virtualInd, String serviceType, Parameter parameter) {
		super(mRID);
		this.manufacturedDate = manufacturedDate;
		this.status = status;
		this.virtualInd = virtualInd;
		this.serviceType = serviceType;
		this.parameters.add(parameter);
	}

	public String getManufacturedDate() {
		return manufacturedDate;
	}

	public void setManufacturedDate(String manufacturedDate) {
		this.manufacturedDate = manufacturedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVirtualInd() {
		return virtualInd;
	}

	public void setVirtualInd(String virtualInd) {
		this.virtualInd = virtualInd;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public void addParameter(Parameter parameter){
		this.parameters.add(parameter);
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getInstallDate() {
		return installDate;
	}

	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}

	public String getFirstRead() {
		return firstRead;
	}

	public void setFirstRead(String firstRead) {
		this.firstRead = firstRead;
	}

	public String getLastRead() {
		return lastRead;
	}

	public void setLastRead(String lastRead) {
		this.lastRead = lastRead;
	}

	@Override
	public String toString() {
		return "MeterAsset [mRID=" + mRID 
				+ "manufacturedDate=" + manufacturedDate + ", status="
				+ status + ", virtualInd=" + virtualInd + ", serviceType="
				+ serviceType + ", serialNumber=" + serialNumber
				+ ", installDate=" + installDate + ", firstRead=" + firstRead
				+ ", lastRead=" + lastRead + ", parameters=" + parameters
				+  "]";
	}
	
	
	
}
