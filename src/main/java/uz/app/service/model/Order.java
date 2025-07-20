package uz.app.service.model;

import lombok.*;
import uz.app.service.model.enums.OrderStatus;
import uz.app.service.model.enums.QuantityUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private long id;
    private long userId;
    private OrderStatus orderStatus;
}
