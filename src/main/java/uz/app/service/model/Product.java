package uz.app.service.model;

import lombok.*;
import uz.app.service.model.enums.QuantityUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private long id;
    private long userId;
    private long categoryId;
    private String name;
    private String description;
    private String image;
    private Double price;
    private QuantityUnit quantityUnit;
    private Double quantity;
}
