package kr.or.ddit.pushExample.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.pushExample.service.PushMsgService;

@Controller
@RequestMapping("push")
public class PushMsgController {
	
	@Inject
	private PushMsgService service;

	@RequestMapping("form")
	public String pushForm() {
		
		return "pushTestForm";
	}
	
	@RequestMapping("test")
	public String pushTest() {

		String test = service.pushTest();
		
		String toGo = "redirect:/push/form";
		return toGo;
	}
	
}
