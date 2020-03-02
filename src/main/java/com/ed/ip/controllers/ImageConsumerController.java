package com.ed.ip.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api/consumer")
public class ImageConsumerController {
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private ServletContext servletContext;

  @RequestMapping(value = "/image-response-entity", method = RequestMethod.GET)
  public ResponseEntity<byte[]> getImageAsResponseEntity() throws IOException {
    log.info("Consuming endpoint of image");
    HttpHeaders headers = new HttpHeaders();
    byte[] imgBytes =
        restTemplate.getForObject(
            "http://localhost:8080/api/publisher/image-response-entity", byte[].class);
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    InputStream in = new ByteArrayInputStream(imgBytes);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Thumbnails.of(in).scale(0.50).toOutputStream(outputStream);
    byte[] imgOut = outputStream.toByteArray();
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(imgOut, headers, HttpStatus.OK);
    return responseEntity;
  }
}
