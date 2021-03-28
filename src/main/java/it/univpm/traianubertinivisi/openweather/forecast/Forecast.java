package it.univpm.traianubertinivisi.openweather.forecast;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name="forecasts")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Forecast {
	
	@Id
	Long id;
	String name;
	Integer visibility;
	@Embedded
	ForecastMain main;
	@Embedded
	ForecastSys sys;
	
	
	
	/** 
	 * @return Long
	 */
	public Long getId() {
		return id;
	}
	
	/** 
	 * @param id
	 */
	public void setId(Long id) {
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
	 * @return Integer
	 */
	public Integer getVisibility() {
		return visibility;
	}
	
	/** 
	 * @param visibility
	 */
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}
	
	/** 
	 * @return ForecastMain
	 */
	public ForecastMain getMain() {
		return main;
	}
	
	/** 
	 * @param main
	 */
	public void setMain(ForecastMain main) {
		this.main = main;
	}
	
	/** 
	 * @return ForecastSys
	 */
	public ForecastSys getSys() {
		return sys;
	}
	
	/** 
	 * @param sys
	 */
	public void setSys(ForecastSys sys) {
		this.sys = sys;
	}
	
}
