package kr.or.ddit.pushExample.service;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import kr.or.ddit.listener.PushMsgCustomEvent;

@Service
public class PushMsgServiceimpl implements PushMsgService{

	/*
	 * 
	// 1. service에서 직접 브로드 캐스팅
	@Inject
	private SimpMessagingTemplate messagingTemplate;
	
	*/
	
	/*
	// 2. CustomEvent 만들어 해당 클래스 실행시 이벤트 발생
	@Inject
	private ApplicationEventPublisher publisher;
	*/
	@Override
	public String pushTest() {
		
		// 직접 라우팅키와 메세지 작성 -> 중복발생
//		messagingTemplate.convertAndSend("/topic/pushMsg", "일반 푸쉬알림 테스트");
		System.out.println("test");
		//여전히 메세지는 직접 입력 -> 또한 service단에서 많은 중복 발생
//		publisher.publishEvent(new PushMsgCustomEvent(this, "customEvent 푸쉬알림 테스트!!"));
		return "OK";
	}

	
}
