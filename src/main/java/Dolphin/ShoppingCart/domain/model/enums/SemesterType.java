package Dolphin.ShoppingCart.domain.model.enums;

public enum SemesterType {
    FIRST("1학기"),
    SECOND("2학기"),
    SUMMER("하계 계절학기"),
    WINTER("동계 계절학기");

    private final String description;

    SemesterType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}