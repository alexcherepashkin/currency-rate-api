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

public class PrivatbankRateDeserializer extends StdDeserializer<Rate> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PrivatbankRateDeserializer.class);

    private static final String 
            CURR_FIELD = "ccy",
            BASE_CURR_FIELD = "base_ccy",
            BUY_FIELD = "buy",
            SALE_FIELD = "sale";
    // PrivatBank case!
    private static final String OLD_RUB_CODE = "RUR";

    public PrivatbankRateDeserializer() {
        this(null);
    }

    protected PrivatbankRateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Rate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        try {
            if (hasSpecifiedFields(node)) {
                String currName = node.get(CURR_FIELD).asText();
                String baseCurrName = node.get(BASE_CURR_FIELD).asText();
                String buy = node.get(BUY_FIELD).asText();
                String sale = node.get(SALE_FIELD).asText();
                // PrivatBank case!
                currName = currName.equalsIgnoreCase(OLD_RUB_CODE) ? Currency.RUB.toString() : currName;

                return new Rate(Currency.forName(currName), Currency.forName(baseCurrName), buy, sale,
                        SourceName.PRIVATBANK.name());
            }
        } catch (RuntimeException e) {
            LOGGER.warn("Failed to deserialize MINFIN_JSON content: '{}'", node.toPrettyString(), e);
        }
        return null;
    }

    private boolean hasSpecifiedFields(JsonNode node) {
        return node.hasNonNull(CURR_FIELD)
                && node.hasNonNull(BASE_CURR_FIELD)
                && node.hasNonNull(BUY_FIELD)
                && node.hasNonNull(SALE_FIELD);
    }
}
