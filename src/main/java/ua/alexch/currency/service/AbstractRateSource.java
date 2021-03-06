package ua.alexch.currency.service;

import java.util.List;
import java.util.Objects;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.alexch.currency.model.Rate;

abstract class AbstractRateSource implements RateSource {

    protected RestTemplate restTemplate;
    protected ObjectMapper mapper;
    protected String url;

    protected List<Rate> retrieveRateData() throws RestClientException, JsonProcessingException {
        String json = restTemplate.getForObject(url, String.class);
        List<Rate> result = mapper.readValue(json, new TypeReference<List<Rate>>() {});

        checkNonNullValues(result);

        return result;
    }

    protected void checkNonNullValues(List<?> list) {
        try {
            list.removeIf(Objects::isNull);
//            list.removeAll(Collections.singleton(null));
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Failed to remove null values in the list", ex);
        }
    }
}
