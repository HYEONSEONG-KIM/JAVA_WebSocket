<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4="
  crossorigin="anonymous"></script>
</head>
<!-- <body onload="init(event);"> -->
<body>
<input type = "button" value = "1번채팅방" class = "chatRoomBtn" data-room = "1"/>
<input type = "button" value = "2번채팅방" class = "chatRoomBtn" data-room = "2"/>
<input type="text"id="message" onchange="messageSend(event);"/>
<input type="button" value="종료" onclick="disconnect(event);">
	<div id="messagesArea"></div>
	
<script type="text/javascript">
	let client = null;
	let headers = {}
	let messageArea = document.querySelector("#messagesArea");
	let chatRoom = null;
	
	let SUB_ID=null; 
	function init(roomNo){
	<!-- // stomp-endpoint로 양방향 통신 연결 수립  -->
		var sockJS=new SockJS("${pageContext.request.contextPath}/stomp/echo");
			// sockJS 연결 기반하에 Stomp client 객체 생성 
			client=Stomp.over(sockJS); 
			// Stomp CONNECTframe 전송  
			client.connect(headers, function(connectFrame){
				
				// CONNECTED frame 을 받은 후, echo 메시지 프레임을 수신을 위한
				//SUBSCRIBE frame에서 사용할 구독아이디를 생성하기 위해
				// 구독 요청 핸들러 쪽으로 전송되는 SUBSCRIBE frame
				// 단 한번의 응답만을수신함.
				client.subscribe("/app/handledEcho/" + roomNo, function(messageFrame){
					console.log(messageFrame.headers)
					SUB_ID=messageFrame.body;
					headers.sub_id=SUB_ID; 
				// Simple Message Broker 로 부터 브로드캐스팅 되는 에코메시지를 구독하기 위한 SUBSCRIBE frame 전송
				
				client.subscribe("/topic/echoed/"+ roomNo, function(messageFrame){
					let body=JSON.parse(messageFrame.body); 
					let msgTag=document.createElement("p");
					
				if(body.sender==SUB_ID)
					msgTag.classList.add("my");
				
					msgTag.innerHTML=body.message+ "["+body.sender+"]";
					messageArea.appendChild(msgTag);
					
					}, {id:SUB_ID});
				});
				
				let msgTag=document.createElement( "p");
				msgTag.innerHTML="연결수립";
			
				messageArea.appendChild(msgTag);
				
				
			}, function(error){
				console.log(error);
				alert(error.headers.message);
			});
		}
	
	
	
	
	function messageSend(event){
		if(! client || ! client.connected) throw "stomp연결 수립 전";
		let body={ sender : SUB_ID, message :event.target.value}
// 서버사이드의 메시지 처리 없이 에코되는 메시지전송
// client.send("/topic/echoed", headers, JSON.stringify(body));
// 서버사이드의 메시지 핸들러에서 처리될 메시지 전송
		client.send("/app/handledEcho/"+chatRoom, headers, JSON.stringify(body));
		event.target.value="";
		event.target.focus();
	}

	function disconnect(event){
		if(! client || ! client.connected) throw "stomp 연결 수립 전";
			client.disconnect();
			let msgTag=document.createElement("p");
			msgTag.innerHTML="연결종료";
			messageArea.appendChild(msgTag);
		}
	
	
	$('.chatRoomBtn').on('click',function(){
		chatRoom = $(this).data('room');
		let roomBtn = $('.chatRoomBtn');
		$(roomBtn).each(function(){
			$(this).attr("disabled", "false");
		})
		
		init(chatRoom);
	})

</script>
</body>
</html>