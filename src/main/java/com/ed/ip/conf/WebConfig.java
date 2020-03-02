package com.ed.ip.conf;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
    ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
    arrayHttpMessageConverter.setSupportedMediaTypes(getSupportedMediaTypes());
    return arrayHttpMessageConverter;
  }

  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(byteArrayHttpMessageConverter());
  }

  private List<MediaType> getSupportedMediaTypes() {
    List<MediaType> list = new ArrayList<>();
    list.add(MediaType.IMAGE_JPEG);
    list.add(MediaType.IMAGE_PNG);
    list.add(MediaType.APPLICATION_OCTET_STREAM);
    return list;
  }
}
