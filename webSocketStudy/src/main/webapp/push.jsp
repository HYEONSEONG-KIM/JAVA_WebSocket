<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4="
  crossorigin="anonymous"></script>
<title>Insert title here</title>
</head>
<body>
<div id = "pushMsg">
	
</div>
</body>

<script type="text/javascript">
	let client = null;
	let headers = {}
	let pushMsg = $('#pushMsg');
	
	let SUB_ID=null; 
	function init(){
	<!-- // stomp-endpoint로 양방향 통신 연결 수립  -->
		var sockJS=new SockJS("${pageContext.request.contextPath}/stomp/push");
			
			client=Stomp.over(sockJS); 
			client.connect(headers, function(connectFrame){
				
				client.subscribe("/app/pushMsg", function(messageFrame){
						SUB_ID=messageFrame.body;
						headers.sub_id=SUB_ID; 
					
						client.subscribe("/topic/pushMsg", function(messageFrame){
							let body = messageFrame.body;
							let pTag = $('<p>').text(body);
							pushMsg.append(pTag)
							
						}, {id:SUB_ID});
				
				});
				
			}, function(error){
				console.log(error);
				alert(error.headers.message);
			});
		}
	
	
	
	function disconnect(event){
		if(! client || ! client.connected) throw "stomp 연결 수립 전";
			client.disconnect();
			let msgTag=document.createElement("p");
			msgTag.innerHTML="연결종료";
			messageArea.appendChild(msgTag);
			
			$(roomBtn).each(function(){
				$(this).attr("disabled", false);
			})
		}
	
	
	
		init();


</script>

</html>