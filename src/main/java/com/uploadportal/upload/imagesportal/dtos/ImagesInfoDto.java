package com.uploadportal.upload.imagesportal.dtos;

import com.uploadportal.upload.imagesportal.entities.ProductCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagesInfoDto {

  private String imageName;
  private String description;
  private String productCategory;

}
