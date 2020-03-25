package com.ed.ip.repositories;

import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Slf4j
@Repository
public class RedisImageRepository {
  private ValueOperations valueOperations;

  private RedisTemplate redisTemplate;

  public RedisImageRepository(RedisTemplate redisTemplate){
    this.redisTemplate = redisTemplate;
    this.valueOperations = this.redisTemplate.opsForValue();
  }

  public void save(final byte[] imageBytes, final int id) {
    log.info("Saving image into redis with id: {}", id);
    BASE64Encoder encoder = new BASE64Encoder();
    String imageString = encoder.encode(imageBytes);
    valueOperations.set(String.valueOf(id), imageString);
  }

  public byte[] findById(int id) throws IOException {
    log.info("Getting image from redis with id: {}", id);
    String imageString = (String)valueOperations.get(String.valueOf(id));
    if(Objects.isNull(imageString)) return null;
    BASE64Decoder decoder = new BASE64Decoder();
    byte[] imageByte = decoder.decodeBuffer(imageString);
    return imageByte;
  }



}
