package it.univpm.traianubertinivisi.openweather.forecast.statistics;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

//This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
//CRUD refers Create, Read, Update, Delete
public interface ForecastStatisticsRepository extends CrudRepository<ForecastStatistics, Integer> {

	List<ForecastStatistics> findByName(String[] criteria);
}
