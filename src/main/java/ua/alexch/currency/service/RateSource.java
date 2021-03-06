package ua.alexch.currency.service;

import java.util.List;

import ua.alexch.currency.model.Rate;

public interface RateSource {

    List<Rate> provideCurrencyRates();
}
