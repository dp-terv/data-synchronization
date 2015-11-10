package hu.bme.mit.v37zen.sm.jpa.datamodel.meterreading;

import javax.persistence.Entity;

@Entity
public class MeterReference extends BaseEntity{

	private static final long serialVersionUID = -2357691182210822084L;

	private String referenceId;
	private String referenceIdType;
	private String referenceIdNamepsace;
	
	public MeterReference(){
		
	}

	public MeterReference(String referenceId, String referenceIdType,
			String referenceIdNamepsace) {
		super();
		this.referenceId = referenceId;
		this.referenceIdType = referenceIdType;
		this.referenceIdNamepsace = referenceIdNamepsace;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceIdType() {
		return referenceIdType;
	}

	public void setReferenceIdType(String referenceIdType) {
		this.referenceIdType = referenceIdType;
	}

	public String getReferenceIdNamepsace() {
		return referenceIdNamepsace;
	}

	public void setReferenceIdNamepsace(String referenceIdNamepsace) {
		this.referenceIdNamepsace = referenceIdNamepsace;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((referenceId == null) ? 0 : referenceId.hashCode());
		result = prime
				* result
				+ ((referenceIdNamepsace == null) ? 0 : referenceIdNamepsace
						.hashCode());
		result = prime * result
				+ ((referenceIdType == null) ? 0 : referenceIdType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeterReference other = (MeterReference) obj;
		if (referenceId == null) {
			if (other.referenceId != null)
				return false;
		} else if (!referenceId.equals(other.referenceId))
			return false;
		if (referenceIdNamepsace == null) {
			if (other.referenceIdNamepsace != null)
				return false;
		} else if (!referenceIdNamepsace.equals(other.referenceIdNamepsace))
			return false;
		if (referenceIdType == null) {
			if (other.referenceIdType != null)
				return false;
		} else if (!referenceIdType.equals(other.referenceIdType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MeterReference [referenceId=" + referenceId
				+ ", referenceIdType=" + referenceIdType
				+ ", referenceIdNamepsace=" + referenceIdNamepsace + "]";
	}
	
}
