package com.ibm.db2.rca.spring.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomePageController 
{
		@RequestMapping(value="/app/home", method = RequestMethod.GET)
		public String helloWorld()
		{			
			return "index";
		}
}
