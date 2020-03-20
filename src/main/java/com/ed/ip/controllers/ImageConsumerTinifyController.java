package com.ed.ip.controllers;

import com.tinify.Options;
import com.tinify.Source;
import com.tinify.Tinify;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
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
@RequestMapping("api/consumer3")
public class ImageConsumerTinifyController {
  @Autowired
  private RestTemplate restTemplate;

  @RequestMapping(value = "/tinify-resizing", method = RequestMethod.GET)
  public ResponseEntity<byte[]> getImageOriginal() throws IOException {
    log.info("Consuming endpoint of image");
    HttpHeaders headers = new HttpHeaders();
    byte[] imgBytes =
        restTemplate.getForObject(
            "http://localhost:8080/api/publisher/image-response-entity", byte[].class);
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());

    Tinify.setKey("LXfY1f6hwxZpLlFtXfqpw8bsFzsnZnNG");
    Options opt = new Options()
        .with("method", "fit")
        .with("width", 1080)
        .with("height", 1080);
    Source img = Tinify.fromBuffer(imgBytes);
    Source resized = img.resize(opt);
    byte[] imgOut = resized.toBuffer();
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(imgOut, headers, HttpStatus.OK);
    return responseEntity;
  }

}
