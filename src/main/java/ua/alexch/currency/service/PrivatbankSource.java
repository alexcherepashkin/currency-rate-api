package ua.alexch.currency.service;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import ua.alexch.currency.model.Rate;
import ua.alexch.currency.util.JacksonMapperFactory;
import ua.alexch.currency.util.PrivatbankRateDeserializer;

@Service
public class PrivatbankSource extends AbstractRateSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrivatbankSource.class);

    @Autowired
    public PrivatbankSource(RestTemplate restTemplate, @Value("${source.privatbank.url}") String url) {
        super.restTemplate = restTemplate;
        super.mapper = JacksonMapperFactory.createMapper(Rate.class, new PrivatbankRateDeserializer());
        super.url = url;
    }

    @Override
    public List<Rate> provideCurrencyRates() {
        try {
            return super.retrieveRateData();

        } catch (RestClientException e) {
            LOGGER.error("Failed to retrieve data from url={}", url, e);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to process given MINFIN_JSON string: ", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Failed to validate result data: ", e);
        }

        return Collections.emptyList();
    }
}
