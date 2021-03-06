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

public class MinfinRateDeserializer extends StdDeserializer<Rate> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(MinfinRateDeserializer.class);

    private static final String 
            CURR_FIELD = "currency",
            BUY_FIELD = "bid",
            SALE_FIELD = "ask",
            DATE_FIELD = "pointDate";

    public MinfinRateDeserializer() {
        this(null);
    }

    protected MinfinRateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Rate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        Rate rate = null;

        JsonNode node = parser.getCodec().readTree(parser);
        try {
            if (hasSpecifiedFields(node)) {
                String currName = node.get(CURR_FIELD).asText();
                String buy = node.get(BUY_FIELD).asText();
                String sale = node.get(SALE_FIELD).asText();
                String dateString = node.get(DATE_FIELD).asText();

                rate = new Rate(Currency.forName(currName), buy, sale, DateTimeUtil.parseDateTime(dateString),
                        SourceName.MINFIN.name());
            }
        } catch (RuntimeException e) {
            LOGGER.warn("Failed to deserialize MINFIN_JSON content: '{}'", node.toPrettyString(), e);
        }
        return rate;
    }

    private boolean hasSpecifiedFields(JsonNode node) {
        return node.hasNonNull(CURR_FIELD)
                && node.hasNonNull(BUY_FIELD)
                && node.hasNonNull(SALE_FIELD)
                && node.hasNonNull(DATE_FIELD);
    }
}
