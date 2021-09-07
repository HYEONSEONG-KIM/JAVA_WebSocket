# Interface-driven
- Endpoint 인터페이스의 구현체를 통해 고정된 시그니처의 메서드로 웹소켓 이벤트를 처리할 수 있음
- 어노테이션 지향 방식과 다르게 고정적인 프로그래밍 방법론에 따라, 고정된 시그니처의 메서드들을 가진 인터페이스(Endpoint)를 상속하여 구현

```java
	public class MyOwnEndpint extends javax.websocket.Endpoint{
		public void onOpen(Session session, EndpointConfig config) {}
		public void onClose(Session session, CloseReason closeReason) {}
		public void onError(Session session, Throwable throwable) {}
	}
```

- 수신된 메시지에 대한 핸들러는 MessageHandler.Partial<T> 나 MessageHandler.Whole<T> 구현체로 작성할 수 있고, 처리할수 있는 메시지의 종류는 각 구현체의 generic type으로 결정되며, 메시지 핸들러들은 웹소켓 연결직후 open 이벤트 핸들러에서 해당 세션에 등록하는 과정을 거쳐야만 함

```java
	public void onOpen(Session session, EndpointConfig config){
		session.addMessageHandler(new MessageHandler(){...});
	}
```

- 수신메시지는 각 메시지 타입을 직접 핸들링하거나, 메시지 타입 복부호화를 위한 Encoder, Decoder 등이 제공되는 등 java WebSocket API 등의 여러가지 지원요소들을 통해 다양한 형태의 메시지 송수신이 가능

- 가장 기본적인 메시지 전송 형태는 텍스트 기반 메시지, 바이너리 메시지, pong 메시지등이 있는데, 이러한 메시지들은 인터페이스 지향 방식을 사용하는 경우, 각기 다른 타입의 MessageHandler를 등록하여 처리할 수 있고, 어노테이션 지향 방식을 사용하는 경우, OnMessage 메서드의 파라미터 타입으로 각기 다른 메시지 핸들러를 작성할 수 있음