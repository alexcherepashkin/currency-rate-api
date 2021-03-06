package ua.alexch.currency.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ua.alexch.currency.util.DateTimeUtil;

@ApiModel(value = "ExchangeRate")
@Entity
@Table(name = "curr_rate")
public class Rate extends AbstractEntity {

    @ApiModelProperty(value = "${prop.curr.description}", example = "EUR")
    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ApiModelProperty(value = "${prop.base-curr.description}", example = "UAH")
    @Column(name = "base_currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency baseCurrency;

    @ApiModelProperty(value = "${prop.buy.description}", example = "\"37.100\"")
    @Column(name = "buy", nullable = false)
    private String buy;

    @ApiModelProperty(value = "${prop.sale.description}", example = "\"42.042\"")
    @Column(name = "sale", nullable = false)
    private String sale;

    @ApiModelProperty(value = "${prop.date.description}", example = "02-02-2020 02:00:20")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_TIME_FORMAT)
    @Column(name = "date_time", nullable = false)
    private LocalDateTime date;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "source", nullable = false)
    private String source;

    public Rate() {
    }

    private Rate(Currency currency, String buy, String sale, String source) {
        this.currency = currency;
        this.baseCurrency = Currency.UAH;
        this.buy = buy;
        this.sale = sale;
        this.date = LocalDateTime.now();
        this.source = source;
    }

    public Rate(Currency currency, Currency baseCurrency, String buy, String sale, String source) {
        this(currency, buy, sale, source);
        this.baseCurrency = baseCurrency;
    }

    public Rate(Currency currency, String buy, String sale, LocalDateTime date, String source) {
        this(currency, buy, sale, source);
        this.date = date;
    }

    public Rate(Currency currency, Currency baseCurrency, String buy, String sale, LocalDateTime date, String source) {
        this(currency, buy, sale, source);
        this.baseCurrency = baseCurrency;
        this.date = date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return String.format("Rate [currency=%s, baseCurrency=%s, buy=%s, sale=%s, date=%s, source=%s]", currency,
                baseCurrency, buy, sale, date, source);
    }
}
