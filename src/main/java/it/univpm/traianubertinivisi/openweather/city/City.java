package it.univpm.traianubertinivisi.openweather.city;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name="cities")
@JsonIgnoreProperties(ignoreUnknown=true)
public class City {
	
	@Id
	private Integer id;
	
	private String name;
	private String state;
	private String country;
	
	@Embedded
	private Coord coord;
	
	
	
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
	 * @return Coord
	 */
	public Coord getCoord() {
		return coord;
	}
	
	/** 
	 * @param coord
	 */
	public void setCoord(Coord coord) {
		this.coord = coord;
	}
	
	
	/** 
	 * @return String
	 */
	public String toString() {
		return "" + this.name;
	}
}
