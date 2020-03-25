package com.ed.ip.controllers;

import com.ed.ip.repositories.RedisImageRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import javax.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
  @Autowired
  private RedisImageRepository imageRepository;

  @RequestMapping(value = "/image-original", method = RequestMethod.GET)
  public ResponseEntity<byte[]> getImageOriginal() throws IOException {
    log.info("Consuming endpoint of image");
    HttpHeaders headers = new HttpHeaders();
    byte[] imgBytes =
        restTemplate.getForObject(
            "http://localhost:8080/api/publisher/image-response-entity", byte[].class);
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    InputStream in = new ByteArrayInputStream(imgBytes);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Thumbnails
        .of(in)
        .scale(1)
        .toOutputStream(outputStream);
    byte[] imgOut = outputStream.toByteArray();
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(imgOut, headers, HttpStatus.OK);
    return responseEntity;
  }

  @RequestMapping(value = "/image-original/{id}", method = RequestMethod.GET)
  public byte[] getImageOriginal(@PathVariable final int id) throws IOException {
    log.info("Consuming endpoint of image with caching");
    byte[] imgOut = null;
    HttpHeaders headers = new HttpHeaders();
    imgOut = imageRepository.findById(id);
    if (Objects.isNull(imgOut)) {
      byte[] imgBytes =
          restTemplate.getForObject(
              "http://localhost:8080/api/publisher/image-response-entity", byte[].class);

      InputStream in = new ByteArrayInputStream(imgBytes);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      Thumbnails.of(in).scale(1).toOutputStream(outputStream);
      imgOut = outputStream.toByteArray();
      imageRepository.save(imgOut, id);
    }
    return imgOut;
  }

  @RequestMapping(value = "/image-scale", method = RequestMethod.GET)
  public ResponseEntity<byte[]> getImageScale() throws IOException {
    log.info("Consuming endpoint of image");
    HttpHeaders headers = new HttpHeaders();
    byte[] imgBytes =
        restTemplate.getForObject(
            "http://localhost:8080/api/publisher/image-response-entity", byte[].class);
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    InputStream in = new ByteArrayInputStream(imgBytes);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Thumbnails
        .of(in)
        .scale(0.50)
        .toOutputStream(outputStream);
    byte[] imgOut = outputStream.toByteArray();
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(imgOut, headers, HttpStatus.OK);
    return responseEntity;
  }

  @RequestMapping(value = "/image-scale-compress", method = RequestMethod.GET)
  public ResponseEntity<byte[]> getImageScaleAndCompress() throws IOException {
    log.info("Consuming endpoint of image");
    HttpHeaders headers = new HttpHeaders();
    byte[] imgBytes =
        restTemplate.getForObject(
            "http://localhost:8080/api/publisher/image-response-entity", byte[].class);
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    InputStream in = new ByteArrayInputStream(imgBytes);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Thumbnails
        .of(in)
        .scale(0.50)
        .outputQuality(0.50)
        .toOutputStream(outputStream);
    byte[] imgOut = outputStream.toByteArray();
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(imgOut, headers, HttpStatus.OK);
    return responseEntity;
  }

  @RequestMapping(value = "/image-compress-scale/{id}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> getImageCompressAndScale(@PathVariable final int id) throws IOException {
    log.info("Consuming endpoint of image");
    byte[] imgOut = null;
    HttpHeaders headers = new HttpHeaders();
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());

    imgOut = imageRepository.findById(id);
    if (Objects.isNull(imgOut)) {
      byte[] imgBytes =
          restTemplate.getForObject(
              "http://localhost:8080/api/publisher/image-response-entity", byte[].class);
      InputStream in = new ByteArrayInputStream(imgBytes);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      Thumbnails.of(in).outputQuality(0.50).scale(0.50).toOutputStream(outputStream);
      imgOut = outputStream.toByteArray();
      imageRepository.save(imgOut, id);
      log.info("Image saved successfully in redis");
    }
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(imgOut, headers, HttpStatus.OK);
    return responseEntity;
  }

  @RequestMapping(value = "/image-custom-size", method = RequestMethod.GET, params = { "width", "height"})
  public ResponseEntity<byte[]> getImageCustomSize(@RequestParam("width") final int width,
      @RequestParam("height") final int height) throws IOException {
    log.info("Consuming endpoint of image");
    HttpHeaders headers = new HttpHeaders();
    byte[] imgBytes =
        restTemplate.getForObject(
            "http://localhost:8080/api/publisher/image-response-entity", byte[].class);
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    InputStream in = new ByteArrayInputStream(imgBytes);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Thumbnails
        .of(in)
        .outputQuality(0.50)
        .size(width, height)
        .toOutputStream(outputStream);
    byte[] imgOut = outputStream.toByteArray();
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(imgOut, headers, HttpStatus.OK);
    return responseEntity;
  }

  @RequestMapping(value = "/image-custom-size2", method = RequestMethod.GET, params = { "width", "height"})
  public ResponseEntity<byte[]> getImageCustomSize2(@RequestParam("width") final int width,
      @RequestParam("height") final int height) throws IOException {
    log.info("Consuming endpoint of image");
    HttpHeaders headers = new HttpHeaders();
    byte[] imgBytes =
        restTemplate.getForObject(
            "http://localhost:8080/api/publisher/image-response-entity", byte[].class);
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    InputStream in = new ByteArrayInputStream(imgBytes);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Thumbnails
        .of(in)
        .size(width, height)
        .toOutputStream(outputStream);
    byte[] imgOut = outputStream.toByteArray();
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(imgOut, headers, HttpStatus.OK);
    return responseEntity;
  }
}
