package io.github.patrykblajer.todo.weatherwidget;

import io.github.patrykblajer.todo.weatherwidget.dtos.WeatherDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    public static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String API_KEY = "0853f564dbfb9d6697a843ca1214fe89";
    private final RestTemplate restTemplate = new RestTemplate();

    private WeatherDto getWeatherDtoForCity(String city) {
        return restTemplate.getForObject(WEATHER_URL + "weather?q={city}&appid={API_KEY}&units=metric&lang=pl",
                WeatherDto.class, city, API_KEY);
    }

    public Weather getWeatherForCity(String city) {
        var weatherDto = getWeatherDtoForCity(city);
        return Weather.builder()
                .name(weatherDto.getName())
                .temperature((int) Math.round(weatherDto.getMain().getTemp()))
                .description(weatherDto.getWeather().get(0).getDescription())
                .build();
    }
}