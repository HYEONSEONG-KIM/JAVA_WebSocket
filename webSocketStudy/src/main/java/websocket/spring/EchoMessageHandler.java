package websocket.spring;

import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class EchoMessageHandler {

	@Data
	public static class MessageVO{
		private String sender;
		private String message;
	}
	
	@Inject
	private SimpMessagingTemplate messagingTemplate;
	
	
	@MessageMapping("/handleds/{test}")
//	@SendTo("/topic/echoed")
	public void handler(@DestinationVariable String test, @Payload MessageVO messageVO, @Header("sub_id") String id )
	throws Exception{
		log.info("id header : {}", id);
		log.info("sender : {}, message : {}", messageVO.getSender(), messageVO.getMessage());
//		return messageVO;
		messagingTemplate.convertAndSend("/topic/echoed/" + test, messageVO);
	}
	
	@MessageMapping("/handledEcho2/{test}")
//	@SendTo("/topic/echoed")
	public void handler2(@DestinationVariable String test)
	throws Exception{
//		return messageVO;
		messagingTemplate.convertAndSend("/topic/test/" + test, "test");
	}
	
	
	@SubscribeMapping("/handledEcho/{roomNo}")
	public String subscribeHandler(
				@DestinationVariable String roomNo,
				@Headers Map<String, Object> headers
			) {
		log.info("headers : {}", headers);
		log.info("roomNo : {}", roomNo);
		// subscription id 를 생성함.
		String sub_id = "test,홍길동";
		return sub_id;
	}
}
