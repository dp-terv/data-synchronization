package hu.bme.mit.v37zen.prepayment.datasync.configurators;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.jmx.export.annotation.ManagedAttribute;

public class BaseConfigurator {
	
	private String dateFormatPattern;
	private DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");
	
	@ManagedAttribute
	public String getDateFormatPattern() {
		return dateFormatPattern;
	}

	@ManagedAttribute
	public void setDateFormatPattern(String dateFormatPattern) {
		this.dateFormatPattern = dateFormatPattern;
		this.dateFormat = new SimpleDateFormat(dateFormatPattern);
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}
}
