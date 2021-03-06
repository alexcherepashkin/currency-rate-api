package ua.alexch.currency.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.alexch.currency.exception.SourceNotFoundException;
import ua.alexch.currency.model.Rate;
import ua.alexch.currency.model.SourceName;
import ua.alexch.currency.repository.RateDataRepository;
import ua.alexch.currency.util.DateTimeUtil;

@Service
@Transactional(readOnly = true)
public class RateDataService {
    private final RateDataRepository rateRepository;

    @Autowired
    public RateDataService(RateDataRepository repository) {
        this.rateRepository = repository;
    }

    public List<Rate> findAllForSource(String source) {
        String sourceName = getSourceName(source);
        return rateRepository.findAllBySource(sourceName);
    }

    public List<Rate> findAllForSourceByPeriod(String source, LocalDateTime dateFrom, LocalDateTime dateTo) {
        String sourceName = getSourceName(source);
        DateTimeUtil.validateDatesOrder(dateFrom, dateTo);

        return rateRepository.findAllBySourceAndDatePeriod(sourceName, dateFrom, dateTo);
    }

    @Transactional
    public void saveAll(List<Rate> rates) {
        if (notEmpty(rates)) {
            rateRepository.saveAll(rates);
        }
    }

    private String getSourceName(String abbr) {
        try {
            return SourceName.forAbbreviation(abbr);
        } catch (IllegalArgumentException ex) {
            throw new SourceNotFoundException(abbr, ex);
        }
    }

    private boolean notEmpty(List<Rate> rates) {
        return rates != null && !rates.isEmpty();
    }
}
