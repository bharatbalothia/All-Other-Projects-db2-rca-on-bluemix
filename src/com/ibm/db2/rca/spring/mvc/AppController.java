package com.ibm.db2.rca.spring.mvc;

import java.util.Iterator;
import java.util.Map;





import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("customerSession")
public class AppController 
{
	@Autowired
	private CustomerSession customerSession = null;

    public void setCustomerSession(CustomerSession customerSession) {
		this.customerSession = customerSession;
	}

	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
    public String login(@RequestParam Map params, ModelMap model) 
    {
		
		Iterator <String> itr  =  params.keySet().iterator();
		
		String param;
		
		while(itr.hasNext())
		{
			param = itr.next();
			
			System.out.println("Param Name : " + param + ", Param Value : " + params.get(param));
		}

        
        //@ModelAttribute("customerSession") CustomerSession customerSession, 
        return "login";
    }
    
    @RequestMapping(value = "/accessdenied", method = { RequestMethod.GET, RequestMethod.POST })
    public String loginerror(ModelMap model)
    {
        model.addAttribute("error", "true");
        return "denied";
    }
 
    @RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
    public String logout(ModelMap model, HttpServletRequest request) 
    {
    	request.getSession().invalidate();
        return "logout";
    }
    
    @RequestMapping(value = "/signup", method = { RequestMethod.GET, RequestMethod.POST })
    public String signup(ModelMap model, HttpServletRequest request) 
    {    	
        return "signup";
    }
}


