package ru.alishev.springcourse.FirstRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstRestApp.dto.SensorDTO;
import ru.alishev.springcourse.FirstRestApp.models.Sensor;
import ru.alishev.springcourse.FirstRestApp.services.SensorService;
import ru.alishev.springcourse.FirstRestApp.util.MeasurementErrorResponse;
import ru.alishev.springcourse.FirstRestApp.util.MeasurementException;
import ru.alishev.springcourse.FirstRestApp.util.SensorValidator;

import javax.validation.Valid;

import static ru.alishev.springcourse.FirstRestApp.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final SensorService sensorService;
    private final ModelMapper modelMapper;

    private final SensorValidator sensorValidator;

    @Autowired
    public SensorsController(SensorService sensorsService, ModelMapper modelMapper, SensorValidator sensorValidator) {
        this.sensorService = sensorsService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult){

        Sensor sensorToAdd = convertToSensor(sensorDTO);

        sensorValidator.validate(sensorToAdd, bindingResult);

        if(bindingResult.hasErrors()){
            returnErrorsToClient(bindingResult);
        }

        sensorService.register(sensorToAdd);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e){
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) { return modelMapper.map(sensorDTO, Sensor.class); }
}
