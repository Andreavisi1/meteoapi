package it.univpm.traianubertinivisi.openweather.city;

import org.springframework.core.io.Resource;

public class CityLoaderThread implements Runnable {
	private boolean doStop = false;
	
	private Resource resourceFile;
	private CityRepository cityRepository;
	
	public CityLoaderThread(CityLoader cityLoader) {
//		Resource resourceFile, CityRepository cityRepository
		this.resourceFile = cityLoader.getResourceFile();
		this.cityRepository = cityLoader.getCityRepository();
	}

    public synchronized void doStop() {
        this.doStop = true;
        System.out.println("thread stopped...");
    }

    
	/** 
	 * @return boolean
	 */
	private synchronized boolean keepRunning() {
        return this.doStop == false;
    }
	
	@Override
	public void run() {
		if (this.cityRepository.count() > 0) {
			System.out.println("Cities list already loaded!");
			this.doStop();
			return;
		}
		CityLoader cl = new CityLoader();
		cl.setResourceFile(this.resourceFile);
		cl.setCityRepository(this.cityRepository);
		cl.loadCities();
		int i = 0;
		while(this.keepRunning()) {
			System.out.println("i = " + i);
			i++;
			this.doStop();
			try {
                Thread.sleep(3L * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
	}
}
