package Dolphin.ShoppingCart.domain.course.enums;

public enum TeachType {
    ENGAGED_LEARNING("EL+"),
    ENGLISH("영어"),
    MIXED("영한혼합"),
    KOREAN("한국어"),
    NONE("미기재");

    private final String description;

    TeachType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}