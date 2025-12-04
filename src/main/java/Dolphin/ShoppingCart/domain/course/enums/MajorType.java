package Dolphin.ShoppingCart.domain.course.enums;

public enum MajorType {
    MAJOR_REQUIRED("전공필수"),
    MAJOR_ELECTIVE("전공선택"),
    MAJOR_FOUNDATION("전공기초");

    private final String description;

    MajorType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}