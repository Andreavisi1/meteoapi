package it.univpm.traianubertinivisi.meteoapi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import it.univpm.traianubertinivisi.meteoapi.controllers.ForecastRestController;
import it.univpm.traianubertinivisi.meteoapi.controllers.HomeRestController;
import it.univpm.traianubertinivisi.meteoapi.services.*;
import it.univpm.traianubertinivisi.openweather.city.*;

@SpringBootTest
class MeteoapiApplicationTests {

	@Autowired
	HomeRestController homeRestController;
	
	@Autowired
	CityService cityService;
	
	@Autowired
	ForecastRestController forecastRestController;

	@Test
	void nameTest (){
		City test = new City(); 
		 test.setName("Macerata");
		assertEquals("Macerata", test.toString());
	}

	@Test
	void homeTest() {
		assertTrue(this.homeRestController.home() instanceof String);
	}

	@Test
	void citiesLoadTest() {
		assertTrue(this.homeRestController.citiesLoad("00980018-f515-4390-9961-d865c629bcf9") instanceof String);
		assertTrue(this.homeRestController.citiesStopLoad("00980018-f515-4390-9961-d865c629bcf9") instanceof String);
	}

	@Test
	void citiesTest() throws Exception {
		assertTrue(this.homeRestController.cities("Macerata","It") instanceof List<?>);
	}

	@Test
	void getForecastForTest () throws Exception{
		assertTrue(this.forecastRestController.getForecastFor("Macerata", "It") instanceof List<?>);
	}

	@Test
	void startForecastAutoLookupTest() {
		assertTrue(this.forecastRestController.startForecastAutoLookup("00980018-f515-4390-9961-d865c629bcf9", "Macerata", "It", 2, "Minutes") instanceof String);
	}

	@Test 
	void stopForecastAutoLookupTest() {
		assertTrue(this.forecastRestController.stopForecastAutoLookup("00980018-f515-4390-9961-d865c629bcf9") instanceof String);
	}

	@Test
	void startForecastSeedingTest() {
		assertTrue(this.forecastRestController.startForecastSeeding("00980018-f515-4390-9961-d865c629bcf9", "Macerata", "It", 2, "Minutes") instanceof String);
	}

	@Test 
	void stopForecastSeeding() {
		assertTrue(this.forecastRestController.stopForecastSeeding("00980018-f515-4390-9961-d865c629bcf9") instanceof String);
	}

	@Test
	void getStatisticsForTest() throws Exception {

		assertTrue(this.forecastRestController.getStatisticsFor("Macerata", "It", "2021-03-27 00:00:00", "2021-03-28 00:00:00") instanceof List<?>);
	}

	
  }
