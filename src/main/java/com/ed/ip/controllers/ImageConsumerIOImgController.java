package com.ed.ip.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("api/consumer2")
public class ImageConsumerIOImgController {
  @Autowired
  private RestTemplate restTemplate;

  @RequestMapping(value = "/io-image", method = RequestMethod.GET)
  public ResponseEntity<byte[]> getImageOriginal() throws IOException {
    log.info("Consuming endpoint of image");
    HttpHeaders headers = new HttpHeaders();
    byte[] imgBytes =
        restTemplate.getForObject(
            "http://localhost:8080/api/publisher/image-response-entity", byte[].class);
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    InputStream in = new ByteArrayInputStream(imgBytes);
    BufferedImage image = ImageIO.read(in);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
    ImageWriter writer = (ImageWriter) writers.next();
    ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
    writer.setOutput(ios);
    ImageWriteParam param = writer.getDefaultWriteParam();

    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    param.setCompressionQuality(0.5f);
    writer.write(null, new IIOImage(image, null, null), param);
    byte[] imgOut = outputStream.toByteArray();
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(imgOut, headers, HttpStatus.OK);
    return responseEntity;
  }

}
