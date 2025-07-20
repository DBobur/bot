package uz.app.service.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    private long id;
    private long createdByUserId;
    private String name;
    private String description;
}
