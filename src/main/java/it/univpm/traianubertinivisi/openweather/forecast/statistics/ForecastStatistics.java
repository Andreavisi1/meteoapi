package it.univpm.traianubertinivisi.openweather.forecast.statistics;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

//@Table(name="forecasts")
@Data
@AllArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
public class ForecastStatistics {
	
		
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Integer id;
	private BigInteger row_n;
	private String country;
	private String city_id;
	private String name;
	
//	@Column(name="start", columnDefinition="DATETIME")
//	@Temporal(TemporalType.TIMESTAMP)
	private String start;
	
//	@Column(name="end", columnDefinition="DATETIME")
//	@Temporal(TemporalType.TIMESTAMP)
	private String end;
	
	private Integer min_visibility;
	private Integer max_visibility;
	private BigDecimal avg_visibility;
	private BigInteger var_visibility;
	private Integer min_pressure;
	private Integer max_pressure;
	private BigDecimal avg_pressure;
	private BigInteger var_pressure;
	
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
	 * @return BigInteger
	 */
	public BigInteger getRow_n() {
		return row_n;
	}
	
	/** 
	 * @param row_n
	 */
	public void setRow_n(BigInteger row_n) {
		this.row_n = row_n;
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
	public String getStart() {
		return start;
	}
	
	/** 
	 * @param start
	 */
	public void setStart(String start) {
		this.start = start;
	}
	
	/** 
	 * @return String
	 */
	public String getEnd() {
		return end;
	}
	
	/** 
	 * @param end
	 */
	public void setEnd(String end) {
		this.end = end;
	}
	
	/** 
	 * @return Integer
	 */
	public Integer getMin_visibility() {
		return min_visibility;
	}
	
	/** 
	 * @param min_visibility
	 */
	public void setMin_visibility(Integer min_visibility) {
		this.min_visibility = min_visibility;
	}
	
	/** 
	 * @return Integer
	 */
	public Integer getMax_visibility() {
		return max_visibility;
	}
	
	/** 
	 * @param max_visibility
	 */
	public void setMax_visibility(Integer max_visibility) {
		this.max_visibility = max_visibility;
	}
	
	/** 
	 * @return BigDecimal
	 */
	public BigDecimal getAvg_visibility() {
		return avg_visibility;
	}
	
	/** 
	 * @param avg_visibility
	 */
	public void setAvg_visibility(BigDecimal avg_visibility) {
		this.avg_visibility = avg_visibility;
	}
	
	/** 
	 * @return BigInteger
	 */
	public BigInteger getVar_visibility() {
		return var_visibility;
	}
	
	/** 
	 * @param var_visibility
	 */
	public void setVar_visibility(BigInteger var_visibility) {
		this.var_visibility = var_visibility;
	}
	
	/** 
	 * @return Integer
	 */
	public Integer getMin_pressure() {
		return min_pressure;
	}
	
	/** 
	 * @param min_pressure
	 */
	public void setMin_pressure(Integer min_pressure) {
		this.min_pressure = min_pressure;
	}
	
	/** 
	 * @return Integer
	 */
	public Integer getMax_pressure() {
		return max_pressure;
	}
	
	/** 
	 * @param max_pressure
	 */
	public void setMax_pressure(Integer max_pressure) {
		this.max_pressure = max_pressure;
	}
	
	/** 
	 * @return BigDecimal
	 */
	public BigDecimal getAvg_pressure() {
		return avg_pressure;
	}
	
	/** 
	 * @param avg_pressure
	 */
	public void setAvg_pressure(BigDecimal avg_pressure) {
		this.avg_pressure = avg_pressure;
	}
	
	/** 
	 * @return BigInteger
	 */
	public BigInteger getVar_pressure() {
		return var_pressure;
	}
	
	/** 
	 * @param var_pressure
	 */
	public void setVar_pressure(BigInteger var_pressure) {
		this.var_pressure = var_pressure;
	}

	
	
	
	
	
}
