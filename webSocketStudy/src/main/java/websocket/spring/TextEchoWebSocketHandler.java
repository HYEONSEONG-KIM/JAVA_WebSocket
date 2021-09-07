package websocket.spring;

import java.net.InetSocketAddress;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class TextEchoWebSocketHandler extends TextWebSocketHandler{

	@Override // @OnError 참고
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("error");
	}

	@Override // @OnMessage 참고
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		session.sendMessage(message);
	}

	@Override // @OnOpen 참고
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		InetSocketAddress clientAddr = session.getRemoteAddress();
		System.out.println(clientAddr + " 과의 websocket 연결 성공");
	}

	@Override // @OnClose 참고
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		InetSocketAddress clientAddr = session.getRemoteAddress();
		int code = status.getCode();
		String reason = status.getReason();
		System.out.println(clientAddr + " 과의 websocket 연결 종료, 종료코드 : " + code + ", 종료사유 : " + reason);
	}

	@Override
	public boolean supportsPartialMessages() {
		// 리턴값이 true라면, 메시지를 분할 처리할 수 있음.
		return super.supportsPartialMessages();
	}

}
