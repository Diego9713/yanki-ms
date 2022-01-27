package bootcamp.com.yankims.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaEventListener {

  @KafkaListener(topics = "${kafka.topic}")
  public void listen(Message<String> message) {
    String payload = message.getPayload();
    log.info(payload);

  }
}
