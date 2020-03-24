package com.ed.ip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ImageProcessingApplication {

  public static void main(String[] args) {
    SpringApplication.run(ImageProcessingApplication.class);
  }

}
