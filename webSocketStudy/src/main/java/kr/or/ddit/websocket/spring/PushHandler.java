package kr.or.ddit.websocket.spring;

import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PushHandler {

	@SubscribeMapping("/pushMsg")
	public String subscribeHandler(
				@Headers Map<String, Object> headers
			) {
		log.info("headers : {}", headers);
		// subscription id 를 생성함.
		String sub_id = UUID.randomUUID().toString();
		return sub_id;
	}
}
