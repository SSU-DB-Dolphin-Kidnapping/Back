package Dolphin.ShoppingCart.domain.model.enums;

public enum TeachType {
    OFFLINE("오프라인"),
    ONLINE("온라인"),
    HYBRID("혼합형"),
    PRACTICE("실습"),
    SEMINAR("세미나");

    private final String description;

    TeachType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}