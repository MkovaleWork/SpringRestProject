package ru.alishev.springcourse.FirstRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstRestApp.dto.MeasurementDTO;
import ru.alishev.springcourse.FirstRestApp.dto.MeasurementResponse;
import ru.alishev.springcourse.FirstRestApp.models.Measurement;
import ru.alishev.springcourse.FirstRestApp.services.MeasurementsService;
import ru.alishev.springcourse.FirstRestApp.util.MeasurementErrorResponse;
import ru.alishev.springcourse.FirstRestApp.util.MeasurementException;
import ru.alishev.springcourse.FirstRestApp.util.MeasurementValidator;


import javax.validation.Valid;

import java.util.stream.Collectors;

import static ru.alishev.springcourse.FirstRestApp.util.ErrorsUtil.returnErrorsToClient;


@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementsService measurementsService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementsController(MeasurementsService measurementsService, MeasurementValidator measurementValidator, ModelMapper modelMapper) {
        this.measurementsService = measurementsService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult){

        Measurement measurementToAdd = convertToMeasurement(measurementDTO);

        measurementValidator.validate(measurementToAdd, bindingResult);

        if(bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        measurementsService.addMeasurement(measurementToAdd);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public MeasurementResponse getMeasurements(){
        //обычно список из элементов оборачивается в один объект для пересылки
        return new MeasurementResponse(measurementsService.findAll().stream().map(this::convertToMeasurementDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/rainyDaysCount")
    public Long getRainyDaysCount() {
        return measurementsService.findAll().stream().filter(Measurement::getRaining).count();
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) { return modelMapper.map(measurementDTO, Measurement.class); }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) { return modelMapper.map(measurement, MeasurementDTO.class); }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e){
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

}
