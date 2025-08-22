package datasorter.result;

public enum SortBy {
    NAME("name"),
    SALARY("salary");
    private final String value;
    private static final String UNKNOWN_SORT_OPTION_MESSAGE = "Unknown sort option";

    SortBy(String value) {
        this.value = value;
    }

    public static SortBy fromString(String string) {
        for (SortBy sortBy : values()) {
            if (sortBy.value.equalsIgnoreCase(string)) {
                return sortBy;
            }
        }
        throw new IllegalArgumentException(UNKNOWN_SORT_OPTION_MESSAGE);
    }
}
