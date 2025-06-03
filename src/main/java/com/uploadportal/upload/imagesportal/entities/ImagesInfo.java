package com.uploadportal.upload.imagesportal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagesInfo {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "image_name")
    private String imageName;
    @Column(name = "description")
    private String description;
    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;
    @Enumerated(EnumType.STRING)
    @Column(name = "image_status")
    private ImageStatus imageStatus;
    @Column(name = "priority")
    private int priority;
    @Column(name = "path")
    private String path;

}
