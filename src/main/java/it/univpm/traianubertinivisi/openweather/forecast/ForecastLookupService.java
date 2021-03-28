package it.univpm.traianubertinivisi.openweather.forecast;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import it.univpm.traianubertinivisi.openweather.city.DbCity;

@Async
@Service
public class ForecastLookupService {
	
	  
      
	  private final RestTemplate restTemplate;
	  
	  @Value("${open_weather.api_key}") 
	  private String apiKey;


	  public ForecastLookupService(RestTemplateBuilder restTemplateBuilder) {
	    this.restTemplate = restTemplateBuilder.build();
	  }
	  
	  
	  /** 
	   * @param city
	   * @return CompletableFuture<Forecast>
	   * @throws InterruptedException
	   */
	  @Async
	  public CompletableFuture<Forecast> getForecastForCity(DbCity city) throws InterruptedException {
		String url = String.format("https://api.openweathermap.org/data/2.5/weather?id=%s&units=metric&appid=%s", city.getCity_id(), this.apiKey);
//		System.out.println(url);
	    Forecast results = restTemplate.getForObject(url, Forecast.class);
	    return CompletableFuture.completedFuture(results);
	  }
}
