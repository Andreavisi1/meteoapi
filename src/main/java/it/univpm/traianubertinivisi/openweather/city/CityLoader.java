package it.univpm.traianubertinivisi.openweather.city;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CityLoader {
	
	@Autowired
	private CityRepository cityRepository;
	
	@Value("classpath:static/openweather/city.list.json")
//	@Value("classpath:static/openweather/cities.json")
	private Resource resourceFile;
	

	
	
	/** 
	 * @return Resource
	 */
	public Resource getResourceFile() {
		return this.resourceFile;
	}
	
	
	/** 
	 * @param resourceFile
	 */
	public void setResourceFile(Resource resourceFile) {
		this.resourceFile = resourceFile;
	}
	
	
	/** 
	 * @return CityRepository
	 */
	public CityRepository getCityRepository() {
		return this.cityRepository;
	}
	
	
	/** 
	 * @param cityRepository
	 */
	public void setCityRepository(CityRepository cityRepository) {
		this.cityRepository = cityRepository;
	}
	
	public void loadCities() {
		if (null != this.cityRepository && this.cityRepository.count() > 0) {
			System.out.println("Cities allready loaded or loading!");
			return;
		}
		System.out.println("Loading cities: begin");
		JSONArray a = new JSONArray( this.getResourceAsString());
		List<DbCity> l = new ArrayList<DbCity>();
		a.forEach(c -> {
			DbCity ct = this.CreateCity(c.toString());
			if (null != ct) l.add(ct) ;	
		});
		this.cityRepository.saveAll(l);
		System.out.println("Loading cities: end");
	}
	
	
	/** 
	 * @param jsonCity
	 * @return DbCity
	 */
	private DbCity CreateCity(String jsonCity) {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<City> typeReference = new TypeReference<City>() {}; 
//		City city = new City();
		try {
			City city = mapper.readValue(jsonCity, typeReference);
//			this.cityRepository.save(new CityToSave(city));
//			System.out.println(city.toString());
			return new DbCity(city);
		}catch(IOException e) {
			System.out.println("Unable to get user: " + e.getMessage());
			return null;
		}
		
	}
	
	
	/** 
	 * @return String
	 */
	private String getResourceAsString() {
        try (Reader reader = new InputStreamReader(this.resourceFile.getInputStream())) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
