package it.univpm.traianubertinivisi.meteoapi;

import java.util.concurrent.Executor;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * La classe MeteoapiApplication è il punto di ingresso del nostro micro-servizio.
 * Lo scopo del presente applicativo è quello di ottenere i valori di visibilità e pressione per le città scelte, attraverso delle chiamate all’API del servizio on-line www.openweathermap.org.
 * Da una parte, i dati ottenuti dal suddetto sito sono rigirati all’utente finale sottoforma di dati JSON, dall’altra, i dati vengono immagazzinati nel database del micro-servizio, ogni tot tempo, per poter ottenere dati statistici riguardanti la visibilità e pressione per le città scelte. Anche se la richiesta del progetto prevedeva la raccolta ogni cinque ore, abbiamo scelto di implementare un metodo flessibile che include anche l’obiettivo richiesto.
 * Il micro-servizio è scritto completamente col linguaggio Java, usando il framework Spring con l’ausilio di Spring Boot, quest’ultimo permettendo di accelerare il processo di sviluppo.
 * Per la persistenza dei dati è stato utilizzato il RDBMS MySQL (nella variante opensource MariaDb).
 */
@SpringBootApplication
@ComponentScan({ "it.univpm.traianubertinivisi" })
@EntityScan("it.univpm.traianubertinivisi")
@EnableJpaRepositories("it.univpm.traianubertinivisi")
@EnableAsync
public class MeteoapiApplication {

	
	/** 
	 * Il metodo main viene invocato automaticamente dal framework Spring
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(MeteoapiApplication.class, args);
	}

	
	/** 
	 * Mantiene traccia dei thread aperti con le richieste ad openweather
	 * Viene caricato automaticamente da springboot all'inizio dell'applicazione
	 * 
	 * @return Executor
	 */
	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("OpenWeatherLookup-");
		executor.initialize();
		return executor;
	}

	
	/** 
	 * Crea le tabelle se non esistono.
	 * Viene caricato automaticamente da springboot all'inizio dell'applicazione
	 * 
	 * @param dataSource
	 * @return DataSourceInitializer
	 */
	@Bean
	public DataSourceInitializer dataSourceInitializer(@Qualifier("dataSource") final DataSource dataSource) {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource("/static/meteoapi/schema.sql"));
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
		return dataSourceInitializer;
	}
}
