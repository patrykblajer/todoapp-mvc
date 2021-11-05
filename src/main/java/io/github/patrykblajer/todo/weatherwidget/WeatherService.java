package io.github.patrykblajer.todo.weatherwidget;

import io.github.patrykblajer.todo.weatherwidget.dtos.WeatherDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${weather.url}")
    private String weatherUrl;
    @Value("${weather.apikey}")
    private String apiKey;

    private WeatherDto getWeatherDtoForCity(String city) {
        return restTemplate.getForObject(weatherUrl + "weather?q={city}&appid={apiKey}&units=metric&lang=pl",
                WeatherDto.class, city, apiKey);
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