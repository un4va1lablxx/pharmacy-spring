package app.pharmacy.entities;

import lombok.Getter;

public enum PaymentMethod {
    CASH("наличными"),
    CARD("картой"),
    ONLINE("онлайн");

    @Getter
    private final String title;

    PaymentMethod(String title) {
        this.title = title;
    }
}
