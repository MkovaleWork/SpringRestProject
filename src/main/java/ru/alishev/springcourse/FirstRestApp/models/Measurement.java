package ru.alishev.springcourse.FirstRestApp.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "measurement")
public class Measurement {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value")
    @NotNull
    @Min(-100)
    @Max(100)
    private Double value;

    @Column(name = "raining")
    @NotNull
    private Boolean raining;

    @Column(name = "measure_date_time")
    @NotNull
    private LocalDateTime measureDateTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sensor", referencedColumnName = "name")
    private Sensor sensor;

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Double getValue() {return value;}

    public void setValue(Double value) {this.value = value;}

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public LocalDateTime getMeasureDateTime() {return measureDateTime;}

    public void setMeasureDateTime(LocalDateTime measureDateTime) {this.measureDateTime = measureDateTime;}

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
