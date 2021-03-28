package it.univpm.traianubertinivisi.openweather.forecast;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Embeddable
@JsonIgnoreProperties(ignoreUnknown=true)
public class ForecastMain {
	
	Integer pressure;

	
	/** 
	 * @return Integer
	 */
	public Integer getPressure() {
		return pressure;
	}

	
	/** 
	 * @param pressure
	 */
	public void setPressure(Integer pressure) {
		this.pressure = pressure;
	}
}
