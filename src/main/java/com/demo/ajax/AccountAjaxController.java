package com.demo.ajax;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.entities.Account;
import com.demo.services.AccountService;


@RestController
@RequestMapping("ajax/superadmin")
public class AccountAjaxController {

	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/editStatus", method = RequestMethod.POST)
	public String editStatus(@RequestParam("id") String id, @RequestParam("status") boolean status, RedirectAttributes redirectAttributes) {
		var accountId = Integer.parseInt(id);
		System.out.println(status);
	  var account = accountService.find(accountId);
	  account.setStatus((status) ? false : true);

	  if (accountService.save(account)) {
	    return "redirect:/superadmin/home/index"; // Redirect to success page
	  } else {
	    redirectAttributes.addFlashAttribute("msg", "Failed");
	    return "redirect:/superadmin/home/edit"; // Redirect to error page
	  }
	}
	
		
}
