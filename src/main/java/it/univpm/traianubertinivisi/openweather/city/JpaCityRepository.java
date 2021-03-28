package it.univpm.traianubertinivisi.openweather.city;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class JpaCityRepository implements CityRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	
	
	/** 
	 * @param criteria
	 * @return List<DbCity>
	 */
	@Override
	public List<DbCity> findByName(String[] criteria) {
		
		String cityOrNull = criteria[0];
		String countryIdOrNull = criteria[1];
		
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
			
		if (cityWhere.length() > 0) cityWhere = " where (" + cityWhere + " ) ";
		
		if (countryWhere.length() > 0) countryWhere = " and ( " + countryWhere + " ) ";
		
		String whereCondition = cityWhere + countryWhere; 
		
//		System.out.println(whereCondition);
		
		TypedQuery<DbCity> query = em.createQuery("select distinct c from DbCity c " + whereCondition + "order by c.name asc", DbCity.class);
		
		for(int i = 0; i < cs.length; i++){
			query.setParameter("name"+i, cs[i]);
		}
		
		
		for(int i = 0; i < cis.length; i++){
			query.setParameter("country"+i, cis[i]);
		}
		
		return query.getResultList();
		
	}


	
	/** 
	 * @param entity
	 * @return S
	 */
	@Override
	public <S extends DbCity> S save(S entity) {
		return null;
	}


	
	/** 
	 * @param entities
	 * @return Iterable<S>
	 */
	@Override
	public <S extends DbCity> Iterable<S> saveAll(Iterable<S> entities) {
		return null;
	}


	
	/** 
	 * @param id
	 * @return Optional<DbCity>
	 */
	@Override
	public Optional<DbCity> findById(Integer id) {
		return null;
	}


	
	/** 
	 * @param id
	 * @return boolean
	 */
	@Override
	public boolean existsById(Integer id) {
		return false;
	}


	
	/** 
	 * @return Iterable<DbCity>
	 */
	@Override
	public Iterable<DbCity> findAll() {
		return null;
	}


	
	/** 
	 * @param ids
	 * @return Iterable<DbCity>
	 */
	@Override
	public Iterable<DbCity> findAllById(Iterable<Integer> ids) {
		return null;
	}


	
	/** 
	 * @return long
	 */
	@Override
	public long count() {
		return 0;
	}


	
	/** 
	 * @param id
	 */
	@Override
	public void deleteById(Integer id) {
	}


	
	/** 
	 * @param entity
	 */
	@Override
	public void delete(DbCity entity) {
	}


	
	/** 
	 * @param entities
	 */
	@Override
	public void deleteAll(Iterable<? extends DbCity> entities) {
	}


	@Override
	public void deleteAll() {
	}

}
