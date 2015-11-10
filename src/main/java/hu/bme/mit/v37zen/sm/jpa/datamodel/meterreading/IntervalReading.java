package hu.bme.mit.v37zen.sm.jpa.datamodel.meterreading;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class IntervalReading extends BaseEntity {

	private static final long serialVersionUID = -2471771834985064822L;
    
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="meter_reference_id")
    private MeterReference meterReference;
    
	
	
    
}
