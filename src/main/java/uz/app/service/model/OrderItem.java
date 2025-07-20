package uz.app.service.model;

import lombok.*;
import uz.app.service.model.enums.QuantityUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private long id;
    private long orderId;
    private long productId;
    private long quantity;
    private QuantityUnit quantityUnit;
}
