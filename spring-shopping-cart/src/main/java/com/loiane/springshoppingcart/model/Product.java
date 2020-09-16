package com.loiane.springshoppingcart.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "products")
@Builder
public class Product {

    @Id
    private String id;

    private String name;
    private String description;
    private Double price;
    private String image;
    private String status;
    private String discounted;
    private Double discount;
}
