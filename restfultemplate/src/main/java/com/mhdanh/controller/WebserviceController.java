package com.mhdanh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebserviceController {
	
	@RequestMapping("/ws/hello")
	@ResponseBody
	public String hello(){
		return "hello resttemplate";
	}
}
