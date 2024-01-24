package ru.alishev.springcourse.FirstRestApp.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class SensorDTO {
    @NotEmpty(message = "Sensor name should not be empty")
    @Size(min = 3, max = 30, message = "Sensor name should be 3 to 30 characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
