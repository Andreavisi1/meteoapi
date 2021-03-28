package it.univpm.traianubertinivisi.openweather.city;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Embeddable
@JsonIgnoreProperties(ignoreUnknown=true)
public class Coord {

	private float lon;
	
	private float lat;

	
	/** 
	 * @return float
	 */
	public float getLon() {
		return lon;
	}

	
	/** 
	 * @param lon
	 */
	public void setLon(float lon) {
		this.lon = lon;
	}

	
	/** 
	 * @return float
	 */
	public float getLat() {
		return lat;
	}

	
	/** 
	 * @param lat
	 */
	public void setLat(float lat) {
		this.lat = lat;
	}
	
	
	
}
