<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript"
src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/0.3.4/sockjs.min.js"></script>
<script type="text/javascript">
	function writeMessage(message){
		var msgArea = document.getElementById("result");
		var pTag = document.createElement("p");
		pTag.innerHTML=message;
		msgArea.appendChild(pTag);
		msgArea.style
		msgArea.scrollTop = msgArea.scrollHeight;
	}

	var ws;
	function echoTest() {
		// 1. Window 객체나 WorkerUtils 객체의 WebSocket API 지원 여부 확인
		if (window.WebSocket) {
			console.log("WebSocket API 를 지원하는 브라우저")
			// 2. ws 프로토콜을 이용하여 서버와의 연결을 개방(테스트용 에코서버).
			ws = new WebSocket(document.getElementById("urlField").value);
// 			ws = new SockJS("${pageContext.request.contextPath}/spring/textToEcho");
			// 3. open/close/error/message 등의 이벤트에 대한 핸들러 작성
			ws.onopen = function(event) {
				// open 이벤트를 발생시킨 WebSocket 객체 확보
				var webSocket = event.target;
				writeMessage(webSocket.url + "과 연결 수립");
				document.getElementById("conBtn").disabled = true;
				document.getElementById("disconBtn").disabled = false;
				document.getElementById("sendBtn").disabled = false;
			};
			ws.onclose = function(closeEvt) {
				// CloseEvent.code : 종료코드, CloseEvent.reason : 종료 사유
				writeMessage("연결 종료, 종료코드 : " + closeEvt.code + ", 종료사유 : "
						+ closeEvt.reason);
				document.getElementById("conBtn").disabled = false;
				document.getElementById("disconBtn").disabled = true;
				document.getElementById("sendBtn").disabled = true;
			};
			ws.onerror = function(errorEvt) {
				console.log("에러발생, 에러코드는 종료 후 종료코드로 확인");
				console.log(errorEvt);
			};
			ws.onmessage = function(messageEvt) {
				// MessageEvent.data : 서버로부터 수신한 메시지
				var message = messageEvt.data;
				console.log("수신메시지 타입 : " + typeof message);
				writeMessage("수신 메시지 : " + message);
				document.getElementById("message").value = "";
			};
		
		} else {
			console.log("WebSocket API를 지원하지 않는 브라우저");
		}
	}
	function disconn() {
		if (ws) {
			ws.close();
		}
	}
	function sendMsg() {
		var msgTag = document.getElementById("message");
		if (ws) {
			var message = msgTag.value;
			writeMessage("발신 메시지 : " + message);
			ws.send(message);
			msgTag.value = "";
			msgTag.focus();
		}
	}
</script>
</head>
<body>

	<input type="text" value="ws://echo.websocket.org" id="urlField" />
	<input type="button" id="conBtn" value="연결" onclick="echoTest(); "/>
	<input type="button" id="disconBtn" value="종료" onclick="discon(); "/>
	<input type="text" id="message" />
	<button id="sendBtn" onclick="sendMsg();">전송</button>
	<div id="result"
		style="background-color: #A3CCA3; border: 1px solid black; height: 500px; overflow: auto;">
	</div>


</body>
</html>