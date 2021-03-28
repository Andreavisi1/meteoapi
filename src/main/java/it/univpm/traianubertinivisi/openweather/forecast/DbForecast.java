package it.univpm.traianubertinivisi.openweather.forecast;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name="forecasts")
@JsonIgnoreProperties(ignoreUnknown=true)
public class DbForecast {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String city_id;
	
	private String name;
	
	private String country;
	
//  @DateTimeFormat(pattern = "Y-m-d H:i:s") // yyyy-MM-dd	
	@Column(name="forecast_date", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
    private Date forecast_date;
	
	private Integer visibility;
	
	private Integer pressure;
	
	
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
	 * @param city_id
	 */
	public void setCity_id(Integer city_id) {
		this.city_id = city_id.toString();
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
	 * @return Date
	 */
	public Date getForecast_date() {
		return this.forecast_date;
	}
	
	/** 
	 * @param forecast_date
	 */
	public void setForecast_date(Date forecast_date) {
		this.forecast_date = forecast_date;
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
