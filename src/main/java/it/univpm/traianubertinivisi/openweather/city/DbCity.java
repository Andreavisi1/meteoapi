package it.univpm.traianubertinivisi.openweather.city;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name="cities")
@JsonIgnoreProperties(ignoreUnknown=true)
public class DbCity {

	public DbCity() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String city_id;
	private String name;
	private String state;
	private String country;
	private String longitude;
	private String latitude;
	
	public DbCity(City city) {
		this.city_id = city.getId().toString();
		this.name = city.getName();
		this.state = city.getState();
		this.country = city.getCountry();
		Float f = city.getCoord().getLon();
		this.longitude = f.toString();
		f = city.getCoord().getLat();
		this.latitude = f.toString();
	}
	
	
	/** 
	 * @return Integer
	 */
	public Integer getId() {
		return id;
	}
	
	/** 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/** 
	 * @return String
	 */
	public String getCity_id() {
		return city_id;
	}
	
	/** 
	 * @param city_id
	 */
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	
	/** 
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
	/** 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/** 
	 * @return String
	 */
	public String getState() {
		return state;
	}
	
	/** 
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}
	
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
	
	/** 
	 * @return String
	 */
	public String getLongitude() {
		return longitude;
	}
	
	/** 
	 * @param longitude
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	/** 
	 * @return String
	 */
	public String getLatitude() {
		return latitude;
	}
	
	/** 
	 * @param latitude
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
