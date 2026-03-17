package app.pharmacy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryAddress {
    private String street;
    private String house;
    private String apartment;
    private String comment;
    private double lat;
    private double lng;
}