package it.univpm.traianubertinivisi.openweather.forecast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.univpm.traianubertinivisi.meteoapi.services.ForecastService;

public class ForecastLoaderThread implements Runnable{
	private boolean doStop = false;
	
	private int timeToSleep = 1000;
	
	private ForecastService forecastService;
	private String cityOrNull;
	private String country;
	private Integer sleepInterval;
	private String sleepIntervalType;
	private Boolean seed;
	
	public ForecastLoaderThread(
			ForecastService forecastService,
			String cityOrNull, String country, Integer sleepInterval, String sleepIntervalType,
			Boolean seed
			) {
		
		this.forecastService = forecastService;
		this.cityOrNull = cityOrNull;
		this.country = country;
		this.sleepInterval = sleepInterval;
		this.sleepIntervalType = sleepIntervalType;
		this.seed = seed;
		
		switch(this.sleepIntervalType) {
			case "milliseconds":
				this.setSleepMilliseconds(this.sleepInterval);
				break;
			case "seconds":
				this.setSleepSeconds(this.sleepInterval);
				break;
			case "minutes":
				this.setSleepMinutes(this.sleepInterval);
				break;
			case "hours":
				this.setSleepHours(this.sleepInterval);
				break;
			case "days":
				this.setSleepDays(this.sleepInterval);
				break;
			default:
				this.setSleepMinutes(this.sleepInterval);
		}
		
		
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
		Date currentDate = new Date();
		// convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        // manipulate date
        c.add(Calendar.YEAR, -1);
        Date seedingDate = c.getTime();
        
		while(this.keepRunning()) {
			try {
				List<DbForecast> fbf;
				if (null == this.seed || false == this.seed) {
					 fbf = this.forecastService.getForecastFor(this.cityOrNull, this.country);
				}else{
					fbf = this.forecastService.getForecastFor(this.cityOrNull, this.country, this.seed, seedingDate);
					c.setTime(seedingDate);
			        // manipulate date
			        c.add(Calendar.HOUR, 5);
			        seedingDate = c.getTime();
			        if (seedingDate.after(currentDate)) this.doStop();
				}
				
				this.forecastService.saveDbForecastList(fbf);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				this.doSleep();	
			}
		}
		
	}
	
	
	/** 
	 * @return int
	 */
	public int getTimeToSleep() {
		return this.timeToSleep;
	}
	
	
	/** 
	 * @param milliseconds
	 * @return int
	 */
	public int setSleepMilliseconds(int milliseconds) {
		this.timeToSleep =  milliseconds;
		return this.timeToSleep;
	}
	
	
	/** 
	 * @param seconds
	 * @return int
	 */
	public int setSleepSeconds(int seconds) {
		this.timeToSleep =  seconds * this.setSleepMilliseconds(1000);
		return this.timeToSleep;
	}
	
	
	/** 
	 * @param minutes
	 * @return int
	 */
	public int setSleepMinutes(int minutes) {
		this.timeToSleep =  minutes * this.setSleepSeconds(60);
		return this.timeToSleep;
	}
	
	
	/** 
	 * @param hours
	 * @return int
	 */
	public int setSleepHours(int hours) {
		this.timeToSleep =  hours * this.setSleepMinutes(60);
		return this.timeToSleep;
	}
	
	
	/** 
	 * @param days
	 * @return int
	 */
	public int setSleepDays(int days) {
		this.timeToSleep =  days * this.setSleepHours(24);
		return this.timeToSleep;
	}
	
//	private functions
	
	private void doSleep() {
		try {
			Thread.sleep(this.timeToSleep);
		} catch (InterruptedException e) {
			this.doStop();
			e.printStackTrace();
		}
	}

	
	
}
