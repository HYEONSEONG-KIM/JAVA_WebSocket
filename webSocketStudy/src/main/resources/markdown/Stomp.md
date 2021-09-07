# STOMP(Simple Text Oriented Message Protocol)

## STOMP frame 구조
- stomp는 스크립트 언어를 위해 만들어진 문자 기반의 프로토콜
- WebSocket은 기존의 http와는 다르게 Line Header Body의 구조가 없이 메세지만 전달되는 구조이므로 전달시 규약을 개발자가 구현해야 함 
- stomp는 command subscribe send의 전송 구조를 갖기떄문에 개발자가 규약을 정할 필요가 없으며, 전송에 관련된 양 피어가 독립적으로 동작할 수 있는 구조를 가짐
- subscribe => 보내는 사람은 발행자, 받는 사람이 구독자의 개념 => 카테고리를 결정한다는 것은 어떤 구독자에게 보낼것인가!!!(채팅방의 개념으로 접근)


## frame 종류
- CONNECT : STOMP 클라이언트가 연결을 수립하고 스트림을 초기화할 때 사용.
- CONNECTED : 클라이언트의 연결 시도를 서버가 수락하면 사용되는 프레임.
- SUBSCRIBE : destination 을 대상으로 전송되는 메시지를 청취하기 위해 사용.
- UNSUBSCRIBE : destionation 을 대상으로 한 구독을 취소하기 위해 사용.
- SEND (body) : 클라이언트가 destination 을 대상으로 메시지를 전송하기 위해 사용.
- BEGIN (transaction): 트랜잭션의 시작을 의미하는 프레임
- COMMIT (transaction) : 트랜잭션 커밋 프레임
- ABORT (transaction) : 트랜잭션 롤백 프레임
- DISCONNECT : 연결 종료 프레임.
- MESSAGE (body) : 브로커에서 릴레이된 구독 메시지가 전송될때 사용되는 프레임.
- RECEIPT : 클라이언트가 보낸 프레임을 서버가 처리 완료하면, 전송되는 프레임.
- ERROR (body) : 에러 발생시 전송되는 프레임

## 메세지 중계 구조
- 스프링 빌트인 메세지 브로커를 사용하는 3개의 메세지 채널
	- clientInboundChannel : 웹소켓 클라이언트로부터 수신할 메시지 채널
	- clientOutboundChannel : 웹소켓 클라이언트에게 메시지를 송신할 채널
	- brokerChannel : 어플리케이션 내에서 메시지 브로커에게 메시지를 송신할 채널
1. STOMP 메세지 브로커로 연결하기 위해 클라이언트는 CONNECT 프레임을 전송하고, 수락되면 브로커에서 클라이언트로 CONNECTED 프레임이 전송
(연결 종료를 위해 DISCONNETCT 프레임이 전송되면, 서버에서는 이를 확인했다는 의미로 RECEIPT 프레임이 전송되고 소켓은 종료)
2. 클라이언트는 SUBSCRIBE 프레임으로 구독하고자 하는 메세지의 카테고리를 결정하는데, 이때 하나의 구독에 대해 id가 부여되고, 구독 메세지의 카테고리는 destination헤더로 표현
3. 클라이언트는 SEND 프레임을 사용하여 메세지를 보내고, 서버에서는 MESSAGE 프레임으로 모든 구독자들에게 브로드캐스팅 할 수 있음(이때 서버는 subscription 헤더를 통해 하나의 구독자를 표현하고, desination 헤더로 메세지의 카테고리를 표현)

	