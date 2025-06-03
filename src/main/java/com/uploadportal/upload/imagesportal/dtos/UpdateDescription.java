package com.uploadportal.upload.imagesportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDescription {
    private long id;
    private String imageName;
    private String description;
}
