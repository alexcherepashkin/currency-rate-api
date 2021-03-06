package ua.alexch.currency.service.schedule;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.alexch.currency.model.Rate;
import ua.alexch.currency.service.RateDataService;
import ua.alexch.currency.service.RateSource;

public class DownloadTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadTask.class);

    private RateSource rateSource;
    private RateDataService rateService;

    public DownloadTask() {}

    public DownloadTask(RateSource rateSource, RateDataService service) {
        this.rateSource = rateSource;
        this.rateService = service;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Download currency rate data from: '{}'", rateSource.getClass().getSimpleName());

            List<Rate> rates = rateSource.provideCurrencyRates();
            rateService.saveAll(rates);

        } catch (Exception ex) {
            LOGGER.warn("Failed to download data", ex);
        }
    }

    public void setSource(RateSource rateSource) {
        this.rateSource = rateSource;
    }

    public void setService(RateDataService service) {
        this.rateService = service;
    }
}
