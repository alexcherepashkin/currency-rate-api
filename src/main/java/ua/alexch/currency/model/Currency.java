package ua.alexch.currency.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Currency {
    UAH("980"),
    EUR("978"),
    USD("840"),
    RUB("643"),
    GBP("826"),
    PLN("985"),
    BTC("BTC");

    private final String code;

    private static final Map<String, Currency> nameToValue = new HashMap<>();
    private static final Map<String, Currency> codeToValue = new HashMap<>();

    static {
        for (Currency value : EnumSet.allOf(Currency.class)) {
            nameToValue.put(value.name(), value);
            codeToValue.put(value.code, value);
        }
    }

    Currency(String code) {
        this.code = code;
    }

    public static Currency forName(String name) {
        Currency currency = nameToValue.get(name.toUpperCase());

        if (currency == null) {
            throw new IllegalArgumentException(String.format("Currency not found by name='%s'", name));
        }
        return currency;
    }

    public static Currency forCode(String code) {
        Currency currency = codeToValue.get(code.toUpperCase());

        if (currency == null) {
            throw new IllegalArgumentException(String.format("Currency not found by code='%s'", code));
        }
        return currency;
    }

    public String getCode() {
        return code;
    }
}
