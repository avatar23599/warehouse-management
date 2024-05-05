package com.demo.controllers.use;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.entities.Account;

import com.demo.helpers.FileHelper;
import com.demo.services.AccountService;
import com.demo.services.RoleService;

@Controller
@RequestMapping({"user/home","home"})
public class HomeUserController {
	
	@Autowired
	private AccountService accountService;

	@RequestMapping(value = { "", "dashboard" }, method = RequestMethod.GET)
	public String dashboard(ModelMap modelMap) {
		modelMap.put("accounts", accountService.findAll());
		return "user/home/dashboard";
	}

	@RequestMapping(value = "details/{id}", method = RequestMethod.GET)
	public String details(@PathVariable("id") int id,ModelMap modelMap) {
		modelMap.put("account", accountService.find(id));
		return "user/home/details";
	}
	

}
