SELECT count(*) as records, min(forecast_date) as min_date, max(forecast_date) as max_date FROM meteoapi.forecasts;

select country, city_id, name, forecast_date, 
visibility, pressure 
FROM meteoapi.forecasts 
where (forecast_date between  '2020-03-18 10:00:00' and '2021-03-18 23:59:59') and name like "Bologna" and city_id = "378660" 
order by name asc, city_id asc, forecast_date desc ;

-- CREATE Table statistics AS 
SET @cnt = 0;
select (@cnt := @cnt + 1) AS id, count(city_id) as row_n, country, city_id, name,
min(visibility) as min_visibility, max(visibility) as max_visibility, avg(visibility) as avg_visibility, (max(visibility) - min(visibility)) as var_visibility,
min(pressure) as min_pressure, max(pressure) as max_pressure, avg(pressure) as avg_pressure, (max(pressure) - min(pressure)) as var_pressure
FROM meteoapi.forecasts
where (forecast_date between  '2021-01-20 00:00:00' and '2021-01-21 23:59:59') 
and (country like "it")
and (name like "Rome"
or name like "Anco%")
group by city_id
order by name asc, city_id asc, forecast_date desc;

select country, city_id, name,
visibility, 
pressure 
FROM meteoapi.forecasts 
where (forecast_date between  '2021-01-01 00:00:00' and '2021-01-31 23:59:59') 
and country like "it"
and city_id = "378658"
order by name asc, city_id asc, forecast_date desc ;
