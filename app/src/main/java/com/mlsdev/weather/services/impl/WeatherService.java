package com.mlsdev.weather.services.impl;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.mlsdev.weather.dao.DaoManager;
import com.mlsdev.weather.model.Weather;
import com.mlsdev.weather.services.IWeatherService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by romakukhar on 31.01.15.
 */
public class WeatherService implements IWeatherService {


    @Override
    public void saveWeather(Weather weather) {
        DaoManager.getWeatherRuntimeDao().create(weather);
    }

    @Override
    public void createOrUpdateWeather(Weather weather) {
        DaoManager.getWeatherRuntimeDao().createOrUpdate(weather);
    }

    @Override
    public void deleteWeather(Weather weather) {
        DaoManager.getWeatherRuntimeDao().delete(weather);
    }

    @Override
    public void deleteAllWeathers() {
        DaoManager.getWeatherRuntimeDao().delete(getAllWeathers());
    }

    @Override
    public void deleteWeatherList(List<Weather> list) {
        DaoManager.getWeatherRuntimeDao().delete(list);
    }

    @Override
    public void updateWeathers(List<Weather> weathers) {
        for (Weather weather : weathers) {
            DaoManager.getWeatherRuntimeDao().createOrUpdate(weather);
        }
    }

    @Override
    public List<Weather> getAllWeathers() {
        return DaoManager.getWeatherRuntimeDao().queryForAll();
    }

    @Override
    public Weather getWeatherByCityName(String cityName) {
        Weather weather = null;
        try {
            QueryBuilder<Weather, Integer> queryBuilder = DaoManager.getWeatherRuntimeDao().queryBuilder();
            queryBuilder.where().eq("city", cityName);
            weather = (Weather) DaoManager.getWeatherRuntimeDao().queryRaw(queryBuilder.prepareStatementString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weather;
    }

    @Override
    public Weather getWeatherById(int id) {
        Weather weather = null;
        try {
            QueryBuilder<Weather, Integer> queryBuilder = DaoManager.getWeatherRuntimeDao().queryBuilder();
            queryBuilder.where().eq("id", id);
            weather = (Weather) DaoManager.getWeatherRuntimeDao().queryRaw(queryBuilder.prepareStatementString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weather;
    }

    @Override
    public List<String> getCitiesId() {
        List<String> resultList = new ArrayList<>();
        try {
            QueryBuilder<Weather, Integer> queryBuilder = DaoManager.getWeatherRuntimeDao().queryBuilder();
            queryBuilder.selectColumns("id");
            GenericRawResults<String[]> rawResults = DaoManager.getWeatherRuntimeDao().queryRaw(queryBuilder.prepareStatementString());
            for (String[] IdItem : rawResults) {
                resultList.add(IdItem[0]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
