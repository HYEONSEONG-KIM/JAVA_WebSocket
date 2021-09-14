package kr.or.ddit.advice;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.WebApplicationContext;

import kr.or.ddit.listener.PushMsgCustomEvent;

@Aspect
public class PushMsgAdvice {

	@Inject
	private WebApplicationContext context;
	
	private Map<String, String> pushMsg = new HashMap<>();
	
	@Pointcut("execution(* kr.or.ddit..service.*.*(..))")
	public void pushPointCut() {}
	
	@PostConstruct
	public void init() {
		pushMsg.put("kr.or.ddit.pushExample.service.PushMsgServiceimpl.pushTest", "AOP푸쉬알림 테스트");
		
	}
	
	@Around("pushPointCut()")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable{
		
		
		// 실행되는 클래스 정보 
		Object target = joinPoint.getTarget();
		// 실행되는 메서드 정보
		Signature signature = joinPoint.getSignature();
		
		//--- 호출전 위빙 포인트 ---
		// 클래스 QN + 메서드명
		String targetName = String.format("%s.%s", target.getClass().getName(), signature.getName());
		
		
		Object resultVal = joinPoint.proceed();
		// --- 호출 후 위빙 포인트
		
		// 해당 메서드의 파라미터
		Object[] args = joinPoint.getArgs();
		
		System.out.println("targetName : " + targetName);
		for(String key : pushMsg.keySet()) {
			
			if(key.equals(targetName)) {
				context.publishEvent(new PushMsgCustomEvent(this, pushMsg.get(key)));
			}
		}
		
		return resultVal;
	}
}
