# Annotation-driven
- POJO에 어노테이션을 사용하여 양 피어간에 웹소켓 라이프사이클 이벤트를 처리할 수 있도록 함

## Usage
- 어노테이션방식은 웹소켓 요청을 받아들일 endpoint를 POJO 지향적으로 작성하고, 이를 endpoint로 지정하기 위해 @ServerEndpoint 어노테이션 사용 -> 해당 어노테이션의 value 속성으로 웹소켓의 경로를 기술, 경로에 인자를 포함시킬 수 있음

```java
	@ServerEndpoint("/example/hello/{user}")
	public class MyEndpointPathParams{
		@OnMessage
		public void process( @PathParam("user") String user){
//............
}
}
```

- 웹소켓 연결을 초기화할수 있는 클라이언트 endpoint는 POJO에 @ClientEndPoint를 사용하여 작성, 이 어노테이션은 경로 매핑을 위한 value 속성을 갖지 않는데, 이는 클라이언트 endpoint에서 들어오는 요청을 대기할 필요가 없기 때문
 
 ```java
 	@ClientEndpoint
	public class MyClientEndpoint{}
 ```

- 어노테이션 방식으로 웹소켓 통신을 초기화하기 위한 클라이언트측 코드

```java
	javax.websocket.WebSocketContainer container =
		javax.websocket.ContainerProvider.getWebSocketContainer();
	container.conntectToServer(MyClientEndpoint.class,
		new URI("ws://host_name/example/hello"));
```


- 웹소켓 연결이 성립되면 @CilentEndPoint와 @ServerEndPoint 클래스들 간의 각 endpoint를 호출할 수 있게 됨

- 연결 수립 후 클라이언트와 서버의 endpoint간의 유일한 연결, 즉 session이 생성되고 @OnOpen 어노테이션을 가진 endpoint 메소드가 호출되는 event-driven 방식이 사용

- session 인스턴스는 웹소켓이 close 될 때까지만 유효한 객체로, Session 클래스는 웹소켓 연결에 대한 정보를 조회할 수 있는 여러가지 메서드 포함

- @OnMessage 어노테이션을 갖는 메서드를 통해 메시지를 수신, 수신 메서드는 Session, @PathParam 파라미터들, 텍스트 메시지 수신을 위한 String 파라미터나 , 바이너리 메시지 수신을 위한 ByteBuffer 혹은 byte[]등의 파라미터를 가질 수 있음

```java
	@OnMessage
	public void myOnMessage(String txt){
	System.out.println("WebSocket received message: "+txt);
	}
```

- 만일 OnMessage 핸들러에 리턴타입이 존재한다면, 웹소켓 컨테이너는 리턴값 자체를 다른 피어로 전송, 웹소켓은 메시지 전송시 chuck 단위로 분리하여 수신되기 때문에 메시지 수신 메서드를 통해 수신된 메시지가 전체 메시지 페이로드 중 일부에 해당한다면 일부 메시지만 반대편 피어로 전송되는 경우가 발생할 수 있기 때문에, 가능하면 메시지 수신 메서드의 리턴 타입은 void로 설정, 에코가 필요하다면 remoteEndpoint를 통해 직접 처리하거나 스트립 전역변수를 선언하여 핸들링하는 편이 안전

```java
	RemoteEndpoint.Basic remoteBasic;
	
	@OnOpen
	public void myOnOpen(Session session){
		remoteBasic = session.getBasicRemote();
	}
	
	Writer writer;
	
	@OnMessage
	public void myOnMessage(String txt, boolean last) throws IOException{
		if(writer==null){
			writer = session.getBasicRemote().getSendWriter();
		}
		writer.write(txt.toUpperCase());
		if(last){
			writer.close();
			writer = null;
		}
	// ---- 혹은 -------------
	remoteBasic.sendText(txt.toUpperCase(), last);
	}
```

- 웹소켓 핸드쉐이크가 형성된 후, open 이벤트 핸들러 내부에서 웹소켓 연결의 반대편 피어에 대한 정보를 가진 RemoteEndpoint 구현을 미리 확보해두면 필요한 코드 부분에서 적절히 사용할 수 있음

- 웹소켓 통신이 종료되면 @OnClose 어노테이션을 가진 메서드가 호출

```java
	@OnClose
	public void myOnClose(CloseReason reason){
		System.out.println("Closing a WebSocket due to"+reason.getReasonPhrase());
	}

```

- 이외에 @OnError 어노테이션을 통해 에러가 발생한 경우나 에러 메시지가 수신된 경우에 대한 이벤트 처리도 가능

```java
	@OnError
	public void myOnError(Session session, Throwable e){
		System.out.println(session.getId()+”, “+e.getMessage());
	}
```











