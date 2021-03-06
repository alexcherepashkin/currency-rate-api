package ua.alexch.currency.model;

public enum SourceName {
    MONOBANK("MB"),
    PRIVATBANK("PB"),
    MINFIN("MF");

    private final String abbreviation;

    private static final SourceName[] values = values();

    private SourceName(String abbr) {
        this.abbreviation = abbr;
    }

    public static String forAbbreviation(String abbr) {
        for (SourceName value : values) {
            if (value.abbreviation.equalsIgnoreCase(abbr)) {
                return value.name();
            }
        }
        throw new IllegalArgumentException(String.format("Source not found by abbreviation='%s'", abbr));
    }

    public String toAbbreviation() {
        return abbreviation;
    }
}
