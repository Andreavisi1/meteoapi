package it.univpm.traianubertinivisi.meteoapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.univpm.traianubertinivisi.openweather.city.CityLoader;
import it.univpm.traianubertinivisi.openweather.city.CityLoaderThread;
import it.univpm.traianubertinivisi.openweather.city.DbCity;
import it.univpm.traianubertinivisi.openweather.city.JpaCityRepository;

@Service
public class CityService {
	
	/**
	 * Repositori per gestire i dati relativi alle città
	 * Viene caricato automaticamente da Spring Boot
	 */
	@Autowired
	JpaCityRepository jpaCityRepository;
	
	/**
	 * Thread necessario per il caricamento dei dati relativi alle città 
	 * contenute nel file city.list.json
	 */
	private CityLoaderThread cityLoaderThread;
	

	/**
	 * Servizio per la gestione dei dati relativi alle città
	 * Viene caricato automaticamente da Spring Boot
	 */
	@Autowired
	private CityLoader cityLoader;
	
	
	/** 
	 * Restituisce il numero delle città presenti nel database
	 * 
	 * @return Long
	 */
	public Long getCityCount() {
		return this.cityLoader.getCityRepository().count();
	}
	
	
	/** 
	 * Restituisce l’elenco delle città che sodisfano i criteri indicati nei parametri.
	 * 
	 * @param cityOrNull
	 * @param country
	 * @return List<DbCity>
	 * @throws Exception
	 */
	public List<DbCity> getCityList(String cityOrNull, String country) throws Exception {
		try {
			if(null == cityOrNull) throw new Exception("Must give at least a city name.");
			
			String[] criteria = {cityOrNull, country};
			
			return this.jpaCityRepository.findByName(criteria);	
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * Avvia il caricamento automatico delle informazioni riguardanti le città dal file city.list.json nel database.
	 */
	public void startLoadingCities() {
		this.cityLoaderThread = new CityLoaderThread(this.cityLoader);
		
		Thread t = new Thread(this.cityLoaderThread);
		t.start();
		System.out.println("Loading cities...");
		
	}
	
	/**
	 * Ferma il caricamento automatico delle informazioni riguardanti le città dal file city.list.json nel database.
	 */
	public void stopLoadingCities() {
		try {
			this.cityLoaderThread.doStop();
			System.out.println("Stop loading cities...");	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
