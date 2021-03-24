package jpa.project.model.dto.delivery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryRegisterRequestDto {
    private String company;
    private String trackingNumber;
}
