package kr.or.ddit.websocket.spring;

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
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ChatMessageHandler{

	@Data
	public static class MessageVO{
		private String sender;
		private String message;
	}
	
	
	@Inject
	private SimpMessagingTemplate messagingTemplate;
	
	

	@MessageMapping("/handleds/{chatRoom}")
	public void handler(@DestinationVariable String chatRoom, @Payload MessageVO messageVO, @Header("sub_id") String id )
	throws Exception{
		log.info("id header : {}", id);
		log.info("sender : {}, message : {}", messageVO.getSender(), messageVO.getMessage());
		messagingTemplate.convertAndSend("/topic/echoed/" + chatRoom, messageVO);
	}
	

	
	@SubscribeMapping("/handled/{chatRoom}")
	public String subscribeHandler(
				@DestinationVariable String chatRoom,
				@Headers Map<String, Object> headers
			) {
		log.info("headers : {}", headers);
		log.info("roomNo : {}", chatRoom);
		// subscription id 를 생성함.
		String sub_id = UUID.randomUUID().toString();
		return sub_id;
	}
}
