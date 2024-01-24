package ru.alishev.springcourse.FirstRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alishev.springcourse.FirstRestApp.models.Measurement;
import ru.alishev.springcourse.FirstRestApp.repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementsService {

    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;

    @Autowired
    public MeasurementsService(MeasurementRepository measurementRepository, SensorService sensorService) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
    }

    public List<Measurement> findAll() { return measurementRepository.findAll(); }

    @Transactional
    public void addMeasurement(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementRepository.save(measurement);
    }

    public void enrichMeasurement(Measurement measurement){
        //я должен сам найти сенсор из бд по имени и вставить объект из hibernate persistence context'a
        measurement.setSensor(sensorService.findByName(measurement.getSensor().getName()).get());

        measurement.setMeasureDateTime(LocalDateTime.now());
    }
}
