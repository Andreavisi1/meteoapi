package it.univpm.traianubertinivisi.openweather.forecast;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Embeddable
@JsonIgnoreProperties(ignoreUnknown=true)
public class ForecastSys {
	
	String country;

	
	/** 
	 * @return String
	 */
	public String getCountry() {
		return country;
	}

	
	/** 
	 * @param country
	 */
	public void setCountry(String country) {
		this.country = country;
	}
}
