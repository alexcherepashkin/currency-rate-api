package ua.alexch.currency.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ua.alexch.currency.exception.ApiError;
import ua.alexch.currency.model.Rate;
import ua.alexch.currency.service.RateDataService;
import ua.alexch.currency.util.DateTimeUtil;

@ApiResponses(value = {
        @ApiResponse(code = 400, response = ApiError.class, message = ""),
        @ApiResponse(code = 404, response = ApiError.class, message = ""),
        @ApiResponse(code = 405, response = ApiError.class, message = ""),
        @ApiResponse(code = 500, response = ApiError.class, message = "")})
@RestController
@RequestMapping(path = "/api/rates", produces = MediaType.APPLICATION_JSON_VALUE)
public class RateDataController {
    private final RateDataService rateService;

    @Autowired
    public RateDataController(RateDataService service) {
        this.rateService = service;
    }

    @ApiOperation(value = "${op.get-all.summary}")
    @GetMapping("/{source}")
    public ResponseEntity<List<Rate>> getAllRatesForSource(
            @ApiParam(value = "${param.src.description}", allowableValues = "mb, pb, mf")
            @PathVariable("source") String source) {

        List<Rate> rates = rateService.findAllForSource(source);
        return ResponseEntity.ok(rates);
    }

    @ApiOperation(value = "${op.get-all-by-period.summary}")
    @GetMapping("/{source}/date")
    public ResponseEntity<List<Rate>> getAllRatesForSourceByPeriod(
            @ApiParam(value = "${param.src.description}", allowableValues = "mb, pb, mf")
            @PathVariable("source") String source,

            @ApiParam(
                    value = "${param.date-from.description}",
                    type = "string",
                    format = "date",
                    example = "02-02-2020")
            @RequestParam("from") String dateFrom,

            @ApiParam(
                    value = "${param.date-to.description}",
                    type = "string",
                    format = "date",
                    example = "03-03-3030")
            @RequestParam("to") String dateTo) {

        LocalDateTime dateTimeFrom = DateTimeUtil.parseDateStartDay(dateFrom);
        LocalDateTime dateTimeTo = DateTimeUtil.parseDateEndDay(dateTo);

        List<Rate> rates = rateService.findAllForSourceByPeriod(source, dateTimeFrom, dateTimeTo);
        return ResponseEntity.ok(rates);
    }
}
