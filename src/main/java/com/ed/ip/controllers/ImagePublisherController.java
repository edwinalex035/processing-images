package com.ed.ip.controllers;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/publisher")
public class ImagePublisherController {
  @Autowired
  private ServletContext servletContext;

  @RequestMapping(value = "/image-manual-response", method = RequestMethod.GET)
  public void getImageAsByteArray(HttpServletResponse response) throws IOException {
    InputStream in = servletContext.getResourceAsStream("/WEB-INF/images/image-example.jpg");
    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    IOUtils.copy(in, response.getOutputStream());
  }

  @RequestMapping(value = "/image-byte-array", method = RequestMethod.GET)
  public @ResponseBody
  byte[] getImageAsByteArray() throws IOException {
    InputStream in = servletContext.getResourceAsStream("/WEB-INF/images/image-example.jpg");
    return IOUtils.toByteArray(in);
  }

  @RequestMapping(value = "/image-response-entity", method = RequestMethod.GET)
  public ResponseEntity<byte[]> getImageAsResponseEntity() throws IOException {
    log.info("Generating image from publisher");
    HttpHeaders headers = new HttpHeaders();
    InputStream in = servletContext.getResourceAsStream("/WEB-INF/images/image-example.jpg");
    byte[] media = IOUtils.toByteArray(in);
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());

    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
    return responseEntity;
  }

}
