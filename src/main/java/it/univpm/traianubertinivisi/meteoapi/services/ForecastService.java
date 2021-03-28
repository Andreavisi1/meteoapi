package it.univpm.traianubertinivisi.meteoapi.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.univpm.traianubertinivisi.openweather.city.DbCity;
import it.univpm.traianubertinivisi.openweather.city.JpaCityRepository;
import it.univpm.traianubertinivisi.openweather.forecast.DbForecast;
import it.univpm.traianubertinivisi.openweather.forecast.Forecast;
import it.univpm.traianubertinivisi.openweather.forecast.ForecastAutolookupRepository;
import it.univpm.traianubertinivisi.openweather.forecast.ForecastLoaderThread;
import it.univpm.traianubertinivisi.openweather.forecast.ForecastLookupService;
import it.univpm.traianubertinivisi.openweather.forecast.statistics.ForecastStatistics;

@Service
public class ForecastService {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	JpaCityRepository jpaCityRepository;
	
	@Autowired
    private ForecastAutolookupRepository forecastAutolookupRepository;
	
	@Autowired(required = true)
	private ForecastLookupService forecastLookupService;
	
	private ForecastLoaderThread forecastLoaderThread;

	public ForecastService(ForecastLookupService forecastLookupService) {
		this.forecastLookupService = forecastLookupService;
	}
		
	
	/** 
	 * Restituisce le previsioni (pressione e visibilità) per le città indicate nei parametri.
	 * 
	 * @param cityOrNull
	 * @param country
	 * @return List<DbForecast>
	 * @throws Exception
	 */
	public List<DbForecast> getForecastFor(
			String cityOrNull,
			String country
			) throws Exception {
		 return this.getForecastFor(cityOrNull, country, false, null);
	}
	
	
	/** 
	 * Restituisce le previsioni (pressione e visibilità) per le città indicate nei parametri.
	 * Questa funzione viene appellata sia per i dati reali che per quelli fittizi nonché 
	 * dalle chiamate via URL e del generatore automatico.
	 * 
	 * @param cityOrNull
	 * @param country
	 * @param seed
	 * @param seedingDate
	 * @return List<DbForecast>
	 * @throws Exception
	 */
	public List<DbForecast> getForecastFor(
			String cityOrNull,
			String country,
			Boolean seed,
			Date seedingDate
			) throws Exception {
		try {
			if(null == cityOrNull) throw new Exception("Must give at least a city name.");
			
			String[] criteria = {cityOrNull, country};
			
			if (null == seed || false == seed) {
				return this.GetForecast(this.jpaCityRepository.findByName(criteria));
			}else {
				if(null == seedingDate) throw new Exception("Must provide a starting seeding date.");
				return this.GetSeededForecast(this.jpaCityRepository.findByName(criteria), seedingDate);
			}
		} catch(Exception e) {
			return null;
		}
	}

	
	/** 
	 * Restituisce le previsioni (pressione e visibilità) ricavate da remoto per le città indicate nei parametri.
	 * Questa funzione viene appellata dalle chiamate via URL e del generatore automatico.
	 * 
	 * @param cities
	 * @return List<DbForecast>
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private List<DbForecast> GetForecast(List<DbCity> cities) throws InterruptedException, ExecutionException{
		List<DbForecast> fcl = new ArrayList<DbForecast>();

		List<CompletableFuture<Forecast>> cfl = new ArrayList<CompletableFuture<Forecast>>();
		
		for(DbCity city : cities) {
			CompletableFuture<Forecast> f = forecastLookupService.getForecastForCity(city);
//			f.get();
			cfl.add(f);
		}
		
		DbForecast fc; 
		Forecast fcg;	
		for(CompletableFuture<Forecast> f : cfl) {
			CompletableFuture.anyOf(f).join();
			Date d = new Date();
			fcg = f.get();
			
			fc = new DbForecast();
			fc.setCity_id(fcg.getId().toString());
			fc.setName(fcg.getName());
			fc.setCountry(fcg.getSys().getCountry());
			fc.setForecast_date(d);
			fc.setVisibility(fcg.getVisibility());
			fc.setPressure(fcg.getMain().getPressure());
			
			fcl.add(fc);
		}
	
		return fcl;
	}
	
	
	/** 
	 * Genera le previsioni fittizie (pressione e visibilità) per poter testare l’applicativo in fase di sviluppo.
	 * 
	 * @param cities
	 * @param seedingDate
	 * @return List<DbForecast>
	 */
	private List<DbForecast> GetSeededForecast(List<DbCity> cities, Date seedingDate) {
		List<DbForecast> fcl = new ArrayList<DbForecast>();
		DbForecast fc;
		for(DbCity city : cities) {
			fc= new DbForecast();
			fc.setCity_id(city.getId().toString());
			fc.setName(city.getName());
			fc.setCountry(city.getCountry());
			fc.setForecast_date(seedingDate);
			fc.setVisibility(ThreadLocalRandom.current().nextInt(500, 1000));
			fc.setPressure(ThreadLocalRandom.current().nextInt(920, 1067));
			
			fcl.add(fc);
		}
		return fcl;
	}
	
	
	/** 
	 * Fa partire il processo automatico di recupero dei dati dal sito www.openweathermap.org
	 * 
	 * @param cityOrNull
	 * @param country
	 * @param sleepInterval
	 * @param sleepIntervalType
	 */
	public void startAutolookupForecastFor(
			String cityOrNull, String country, Integer sleepInterval, String sleepIntervalType
			) {
		this.stopAutolookupForecastFor();
		this.forecastLoaderThread = new ForecastLoaderThread(this, cityOrNull, country, sleepInterval, sleepIntervalType, false);
		Thread t = new Thread(this.forecastLoaderThread);
		t.start();
		System.out.println("Begin autolookup forecast...");
	}
	 /**
	  * Ferma il processo automatico di recupero dei dati dal sito www.openweathermap.org
	  */
	public void stopAutolookupForecastFor() {
		try {
			if (null != this.forecastLoaderThread) this.forecastLoaderThread.doStop();
			System.out.println("End autolookup forecast...");	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** 
	 * Restituisce le previsioni fittizie (pressione e visibilità) per poter testare l’applicativo in fase di sviluppo.
	 * Questa funzione viene appellata dal generatore automatico.
	 * 
	 * @param cityOrNull
	 * @param country
	 * @param sleepInterval
	 * @param sleepIntervalType
	 */
	public void startSeedingForecastFor(
			String cityOrNull, String country, Integer sleepInterval, String sleepIntervalType
			) {
		this.stopAutolookupForecastFor();
		this.forecastLoaderThread = new ForecastLoaderThread(this, cityOrNull, country, sleepInterval, sleepIntervalType, true);
		Thread t = new Thread(this.forecastLoaderThread);
		t.start();
		System.out.println("Begin seeding forecasts table...");
	}
	
	/**
	 * Ferma il generatore automatico di dati fittizi.
	 */
	public void stopSeedingForecastFor() {
		try {
			if (null != this.forecastLoaderThread) this.forecastLoaderThread.doStop();
			System.out.println("End seeding forecasts table...");	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** 
	 * Salva sia i dati reali che fittizi nel database
	 * 
	 * @param dbForecastList
	 */
	public void saveDbForecastList(List<DbForecast> dbForecastList) {
		// System.out.println(dbForecastList);
		this.forecastAutolookupRepository.saveAll(dbForecastList);
		System.out.println("Forecasts list successfully saved!");
	}
	
	
	/** 
	 * Restituisce la lista con i dati statistici per le città indicati 
	 * 
	 * @param cityOrNull
	 * @param country
	 * @param start_date
	 * @param end_date
	 * @return List<ForecastStatistics>
	 * @throws Exception
	 */
	public List<ForecastStatistics> getStatisticsFor(
			String cityOrNull,
			String country,
			String start_date,
			String end_date
			) throws Exception {
		if(null == cityOrNull) throw new Exception("Must give at least a city name.");
		String[] criteria = {cityOrNull, country, start_date, end_date};
		return this.generate(criteria);
	}
	
	
	/** 
	 * Restituisce la lista con i dati statistici per le città indicati dal database.
	 * 
	 * @param criteria
	 * @return List<ForecastStatistics>
	 */
	private List<ForecastStatistics> generate(String[] criteria) {
		// String[] criteria = {cityOrNull, country, start_date, end_date};
		String cityOrNull = criteria[0];
		String countryIdOrNull = criteria[1];
		String start_date = criteria[2];
		String end_date = criteria[3];
		
		cityOrNull = "" + cityOrNull.replace("!", "_").replace("*", "%");
		
		if (null == countryIdOrNull) countryIdOrNull = "*";
		
		countryIdOrNull= "" + countryIdOrNull.replace("!", "_").replace("*", "%");
		
		
		String cityWhere = "";
		
		String countryWhere = "";
		
		String[] cs = cityOrNull.split(",");
		for(int i = 0; i < cs.length; i++){
			cityWhere +=  ((cityWhere.length() > 0) ? " OR " : "") + " c.name like :name" + i;
		}
		
		String[] cis = countryIdOrNull.split(",");
		
		for(int i = 0; i < cis.length; i++){
			countryWhere +=  ((countryWhere.length() > 0) ? " OR " : "") + " c.country like :country" + i;
			
		}
			
		if (cityWhere.length() > 0) cityWhere = " and (" + cityWhere + " ) ";
		
		if (countryWhere.length() > 0) countryWhere = " and ( " + countryWhere + " ) ";
		
		String whereCondition = countryWhere + cityWhere; 
		

		
		StringBuilder sql = new StringBuilder("");
		String nl = "\n";
		sql.append(" select id, count(city_id) as row_n, country, city_id, name, " + nl);
		sql.append(" '"+ start_date +"' as start, '" + end_date + "' as end, " + nl);
		sql.append(" min(visibility) as min_visibility, max(visibility) as max_visibility, avg(visibility) as avg_visibility, (max(visibility) - min(visibility)) as var_visibility, " + nl);
		sql.append(" min(pressure) as min_pressure, max(pressure) as max_pressure, avg(pressure) as avg_pressure, (max(pressure) - min(pressure)) as var_pressure " + nl);
		sql.append(" FROM forecasts as c " + nl);
		sql.append(" where (c.forecast_date BETWEEN '"+ start_date +"' AND '" + end_date + "')  " + nl);
		sql.append( whereCondition  + nl);
		sql.append(" group by c.city_id " + nl);
		sql.append(" order by c.name asc, c.city_id asc, c.forecast_date desc " + nl);
		
		System.out.println(sql.toString());
		
		Query query = em.createNativeQuery(sql.toString());
		
		for(int i = 0; i < cis.length; i++){
			query.setParameter("country"+i, cis[i]);
		}
		
		for(int i = 0; i < cs.length; i++){
			query.setParameter("name"+i, cs[i]);
		}
		
		@SuppressWarnings("unchecked")
		List<Object[]> obj = query.getResultList();
		
		List<ForecastStatistics> fcs = new ArrayList<ForecastStatistics>();
		
		ForecastStatistics fs;
		for (Object [] o : obj) {
			fs = new ForecastStatistics();
			fs.setId((Integer) o[0]);
			fs.setRow_n((BigInteger) o[1]);
			fs.setCountry((String) o[2]);
			fs.setCity_id((String) o[3]);
			fs.setName((String) o[4]);
			fs.setStart((String) o[5]);
			fs.setEnd((String) o[6]);
			fs.setMin_visibility((Integer) o[7]);
			fs.setMax_visibility((Integer) o[8]);
			fs.setAvg_visibility((BigDecimal) o[9]);
			fs.setVar_visibility((BigInteger) o[10]);
			fs.setMin_pressure((Integer) o[11]);
			fs.setMax_pressure((Integer) o[12]);
			fs.setAvg_pressure((BigDecimal) o[13]);
			fs.setVar_pressure((BigInteger) o[14]);
			fcs.add(fs);
		}
		
		return fcs;
//		return null;
	}

	
}
