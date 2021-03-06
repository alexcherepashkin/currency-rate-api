package ua.alexch.currency.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import ua.alexch.currency.config.SpringConfig;
import ua.alexch.currency.model.Currency;
import ua.alexch.currency.model.Rate;
import ua.alexch.currency.util.DateTimeUtil;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfig.class)
class MinfinSourceTest {
    private static final String TEST_URL = "https://api.minfin.com.ua/";
    private static final String FALSE_JSON = "[]";
    private static final String MINFIN_JSON = "["
            + "{\"id\": \"100754\",\"pointDate\": \"2021-02-10 11:18:00\",\"date\": \"2021-02-10 11:00:00\",\"ask\": \"27.7650\",\"bid\": \"27.7500\","
            + "\"trendAsk\": \"0.0250\",\"trendBid\": \"0.0300\",\"currency\": \"usd\"},"
            + "{\"id\": \"100746\",\"pointDate\": \"2021-02-10 11:17:50\",\"date\": \"2021-02-10 11:00:00\",\"ask\": \"33.7011\",\"bid\": \"33.6857\","
            + "\"trendAsk\": \"0.0498\",\"trendBid\": \"0.0586\",\"currency\": \"eur\"}]";

    private final Rate usdRate = new Rate(Currency.USD, Currency.UAH, "27.7500", "27.7650", DateTimeUtil.parseDateTime("2021-02-10 11:18:00"), "MINFIN");
    private final Rate eurRate = new Rate(Currency.EUR, Currency.UAH, "33.6857", "33.7011", DateTimeUtil.parseDateTime("2021-02-10 11:17:50"), "MINFIN");
    private final List<Rate> expectedRates = Arrays.asList(usdRate, eurRate);

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private MinfinSource source;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        source = new MinfinSource(restTemplate, TEST_URL);
    }

    @Test
    void shouldReturnExtractedRateData() {

        mockServer.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TEST_URL))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(MINFIN_JSON));

        List<Rate> actualRates = source.provideCurrencyRates();

        mockServer.verify();
        assertEquals(expectedRates, actualRates);
    }

    @Test
    void shouldReturnEmptyListIfFalseResponseJson() {

        mockServer.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(TEST_URL))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(FALSE_JSON));

        List<Rate> actualRates = source.provideCurrencyRates();

        mockServer.verify();
        assertEquals(Collections.EMPTY_LIST, actualRates);
    }
}
