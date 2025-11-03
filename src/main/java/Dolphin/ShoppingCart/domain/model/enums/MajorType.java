package Dolphin.ShoppingCart.domain.model.enums;

public enum MajorType {
    MAJOR_REQUIRED("전공필수"),
    MAJOR_ELECTIVE("전공선택"),
    GENERAL_EDUCATION("교양"),
    LIBERAL_ARTS("교양선택"),
    FREE_ELECTIVE("일반선택");

    private final String description;

    MajorType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}