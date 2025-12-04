package Dolphin.ShoppingCart.domain.course.enums;

public enum SecondMajorType {
    DOUBLE_MAJOR_REQUIRED("복수전공필수"),
    DOUBLE_MAJOR_ELECTIVE("복수전공선택"),
    MINOR("부전공"),
    NONE("해당없음");

    private final String description;

    SecondMajorType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}