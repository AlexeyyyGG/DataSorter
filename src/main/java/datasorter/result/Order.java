package datasorter.result;

public enum Order {
    ASC("asc"),
    DESC("desc");
    private final String value;
    private static final String UNKNOWN_ORDER_MESSAGE = "Unknown Order";

    Order(String value) {
        this.value = value;
    }

    public static Order fromString(String string) {
        for (Order order : values()) {
            if (order.value.equalsIgnoreCase(string)) {
                return order;
            }
        }
        throw new IllegalArgumentException(UNKNOWN_ORDER_MESSAGE);
    }
}
