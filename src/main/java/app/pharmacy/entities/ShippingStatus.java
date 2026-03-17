package app.pharmacy.entities;

import lombok.Getter;

public enum ShippingStatus {
    CREATED("создан"),
    CONFIRMED("в обработке"),
    SHIPPED("отправлен"),
    DELIVERED("доставлен"),
    CANCELED("отменен");

    @Getter
    private final String title;

    ShippingStatus(String title) {
        this.title = title;
    }

}
