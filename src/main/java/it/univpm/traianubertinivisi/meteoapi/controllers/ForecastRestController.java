package it.univpm.traianubertinivisi.meteoapi.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import it.univpm.traianubertinivisi.meteoapi.services.ForecastService;
import it.univpm.traianubertinivisi.openweather.forecast.DbForecast;
import it.univpm.traianubertinivisi.openweather.forecast.statistics.ForecastStatistics;

/**
 * "/forecast" rappresenta la radice delle operazioni con le previsioni meteo
 * è stato aggiunto per facilitare la deffinizione dei vari segmenti
 * a questo si aggiungono diversi segmenti in base alle necessità
 */
@RestController
@RequestMapping("/forecast")
public class ForecastRestController {
	
	@Value("${meteoapi.pwd}")
	private String pwd;
	
	@Autowired
	ForecastService forecastService; 
	
	
	/** 
	 * Richiede al sito openweathermap.org  le previsioni generiche per le città che rispettano i parametri dati.
	 * Nella successiva funzione chiamata si usa l'elenco delle città per estrarre il codice città delle città richieste.
	 * Usando il codice città si ottengono risultati più accurati.
	 * 
	 * @return List<DbForecast>
	 * @throws Exception
	 */
	@GetMapping("")
	// è identico al /forecast
	public List<DbForecast> getForecastFor(
			@RequestParam(name="city", required=false) String cityOrNull,
			@RequestParam(name="country", required=false, defaultValue="*") String country
			) throws Exception {
		return this.forecastService.getForecastFor(cityOrNull, country);
	}

	
	/** 
 	* Con quuesta chiamata si fa partire un thread che raccoglie le previzioni in base ai parametri dati
	* pwd - rappresenta la stringa password
	* city - rappresenta la città o le città per cui si fa la ricerca
	* country - restringe la ricerca della città al paese indicato
	* 
	* per questi due parametri si possono usare i seguenti caratteri sostitutivi:
	* ! - sostituisce un carratere nella posizione corrente
	* * - sostituisce uno o più carrateri cominciando dalla posizione corrente.
	* 
	* Si possono indicare più città o paesi divisi da virgole, senza spazi.
	* 
	* sleep - è il valore per il segmento di tempo in cui il thread si ferma (da compito 5 ore).
	* type - rappresenta il tipo di valore sleep che può essere: milliseconds, seconds, minutes, hours, days
	* 
	 * @return String
	 */
	@GetMapping("/lookup/{pwd}")
	public String startForecastAutoLookup(
			@PathVariable(name = "pwd", required = true) String pwd,
			@RequestParam(name="city", required=false) String cityOrNull,
			@RequestParam(name="country", required=false, defaultValue="*") String country,
			@RequestParam(name="sleep", required=false, defaultValue="5") Integer sleepInterval,
			// possible values for type: milliseconds, seconds, minutes, hours, days
			@RequestParam(name="type", required=false, defaultValue="hours") String sleepIntervalType 
			) {
		// cityOrNull, country, sleepInterval, sleepIntervalType
		if (null == this.pwd || !this.pwd.equals(pwd)) return "Cannot load the forecast!";
		this.forecastService.startAutolookupForecastFor(cityOrNull, country, sleepInterval, sleepIntervalType);
		return "Forecast autolookup started...";
	}
	
	
	
	/** 
	 * Ferma il thred della chiamata sopraindicata se si fornisce la password pwd.
	 * 
	 * @return String
	 */
	@GetMapping("/lookup/{pwd}/stop")
	public String stopForecastAutoLookup(
			@PathVariable(name = "pwd", required = true) String pwd
			) {
		if (null == this.pwd || !this.pwd.equals(pwd)) return "Cannot stop loading the forecast!";
		this.forecastService.stopAutolookupForecastFor();
		return "Forecast autolookup stopped...";
	}
	
	
	
	/** 
	* Fa la medesima cosa della chiamata /lookup/{pwd} per scopi di test, creado dei dati fake per il forecast
	* Questo per salvaguardare il numero limitato di chiamate a  openweathermap.org  	
	* @return String
	*/
	@GetMapping("/seed/{pwd}")
	public String startForecastSeeding(
			@PathVariable(name = "pwd", required = true) String pwd,
			@RequestParam(name="city", required=false) String cityOrNull,
			@RequestParam(name="country", required=false, defaultValue="*") String country,
			@RequestParam(name="sleep", required=false, defaultValue="5") Integer sleepInterval,
			// possible values for type: milliseconds, seconds, minutes, hours, days
			@RequestParam(name="type", required=false, defaultValue="hours") String sleepIntervalType 
			) {
		
		//  cityOrNull, country, sleepInterval, sleepIntervalType
		if (null == this.pwd || !this.pwd.equals(pwd)) return "Cannot seed the forecasts table!";
		this.forecastService.startSeedingForecastFor(cityOrNull, country, sleepInterval, sleepIntervalType);
		return "Forecast seeding started...";
	}
	
	
	/** 
	 * ferma la chiamata sopraindicata.
	 * 
	 * @return String
	 */
	@GetMapping("/seed/{pwd}/stop")
	public String stopForecastSeeding(
			@PathVariable(name = "pwd", required = true) String pwd
			) {
		if (null == this.pwd || !this.pwd.equals(pwd)) return "Cannot stop seeding the forecasts table!";
		this.forecastService.stopSeedingForecastFor();
		return "Forecast seeding stopped...";
	}
	
	
	/** 
	* questa chiamata fornisce dati statistici in base ai parametri indicati
	* I parametri start ed end rappresentano la data d'inizio rispettivamente la data della fine per il segmento di ricerca
	* Devono essere indicati sotto forma "YYYY-MM-DD HH:mm:ss"
	* se non saranno indicati verranno impostati sulla data odierna dalla mezzanotte a mezzanotte
	* @return List<ForecastStatistics>
	* @throws Exception
	*/
	@GetMapping("/statistics")
	public List<ForecastStatistics> getStatisticsFor(
			@RequestParam(name="city", required=false) String cityOrNull,
			@RequestParam(name="country", required=false, defaultValue="*") String country,
			@RequestParam(name="start", required=false) String start_date,
			@RequestParam(name="end", required=false) String end_date
			) throws Exception {
		if (null == start_date) start_date = this.GetNowString("start");
		if (null == end_date) end_date = this.GetNowString("end");
		
		return this.forecastService.getStatisticsFor(cityOrNull, country,start_date, end_date);
	}

	
	/** 
	 * Restituisce una stringa con forma della data "yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param timeSpan
	 * @return String
	 */
	private String GetNowString(String timeSpan) {
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(new Date()) + (timeSpan.equals("start") ? " 00:00:00":" 23:59:59");
	}
	
	
}
