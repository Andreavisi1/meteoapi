package it.univpm.traianubertinivisi.openweather.forecast;
import org.springframework.data.repository.CrudRepository;

public interface ForecastAutolookupRepository extends CrudRepository<DbForecast, Integer>{

}
