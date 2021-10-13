package io.github.patrykblajer.todo.weatherwidget;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Weather {
    private String name;
    private int temperature;
    private String description;
}