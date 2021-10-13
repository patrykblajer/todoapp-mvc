package io.github.patrykblajer.todo.weatherwidget.dtos;

import lombok.Getter;

import java.util.List;

@Getter
public class WeatherDto {
    private String name;
    private WeatherMainDto main;
    private List<WeatherWeatherDto> weather;
}