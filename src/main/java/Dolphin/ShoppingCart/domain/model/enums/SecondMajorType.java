package Dolphin.ShoppingCart.domain.model.enums;

public enum SecondMajorType {
    DOUBLE_MAJOR("복수전공"),
    MINOR("부전공"),
    TEACHER_CERTIFICATION("교직과정"),
    NONE("해당없음");

    private final String description;

    SecondMajorType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}