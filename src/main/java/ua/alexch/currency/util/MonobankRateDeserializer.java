package ua.alexch.currency.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import ua.alexch.currency.model.Currency;
import ua.alexch.currency.model.Rate;
import ua.alexch.currency.model.SourceName;

public class MonobankRateDeserializer extends StdDeserializer<Rate> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(MonobankRateDeserializer.class);

    private static final String 
            CURR_FIELD = "currencyCodeA",
            BASE_CURR_FIELD = "currencyCodeB",
            BUY_FIELD = "rateBuy",
            SALE_FIELD = "rateSell",
            DATE_FIELD = "date";

    public MonobankRateDeserializer() {
        this(null);
    }

    protected MonobankRateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Rate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        try {
            if (hasNonNullFields(node)) {
                String currCode = node.get(CURR_FIELD).asText();
                String baseCurrCode = node.get(BASE_CURR_FIELD).asText();
                String buy = node.get(BUY_FIELD).asText();
                String sale = node.get(SALE_FIELD).asText();
                long timestamp = node.get(DATE_FIELD).asLong();

                return new Rate(Currency.forCode(currCode), Currency.forCode(baseCurrCode), buy, sale,
                        DateTimeUtil.parseDateTime(timestamp), SourceName.MONOBANK.name());
            }
        } catch (RuntimeException e) {
            LOGGER.warn("Failed to deserialize MINFIN_JSON content: '{}'", node.toPrettyString(), e);
        }
        return null;
    }

    private boolean hasNonNullFields(JsonNode node) {
        return node.hasNonNull(CURR_FIELD)
                && node.hasNonNull(BASE_CURR_FIELD)
                && node.hasNonNull(BUY_FIELD)
                && node.hasNonNull(SALE_FIELD)
                && node.hasNonNull(DATE_FIELD);
    }
}
