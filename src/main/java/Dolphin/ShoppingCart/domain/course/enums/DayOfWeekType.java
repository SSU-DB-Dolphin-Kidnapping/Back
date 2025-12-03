package Dolphin.ShoppingCart.domain.course.enums;

public enum DayOfWeekType {
    MONDAY("월요일"),
    TUESDAY("화요일"),
    WEDNESDAY("수요일"),
    THURSDAY("목요일"),
    FRIDAY("금요일"),
    SATURDAY("토요일"),
    SUNDAY("일요일");

    private final String description;

    DayOfWeekType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}