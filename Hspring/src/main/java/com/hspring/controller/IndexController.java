package com.hspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class IndexController {

	@RequestMapping("/index")
	public String index(Model model)
	{
		System.out.println("mvc successfule");
		model.addAttribute("msg","hi every one");
		return "index/index";
	}
	
}
