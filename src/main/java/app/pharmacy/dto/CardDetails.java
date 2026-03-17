package app.pharmacy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDetails {
    private String number;
    private String expiry;
    private String cvv;
    private String holder;
    private boolean save;
}