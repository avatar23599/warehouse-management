package com.demo.controllers.use;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.Validate;

//import com.demo.validators.AccountValidator;
import com.demo.entities.Account;
import com.demo.helpers.FileHelper;
import com.demo.helpers.SecurityCodeHelper;
import com.demo.services.AccountService;
import com.demo.services.MailService;
import com.demo.services.RoleService;

import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;


@Controller
@RequestMapping("user/account")
public class AccountUserController {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private MailService mailService;
//	@Autowired
//	private AccountValidator accountValidator;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error, ModelMap modelMap,
			@RequestParam(value = "logout", required = false) String logout	) {
		if(error != null) {
			modelMap.put("msg", "The account is not valid");
		}
		if(logout != null) {
			modelMap.put("msg", "Logout successfully");
		}
		return "user/account/login";
	}
	
	@RequestMapping(value = "process-login", method = RequestMethod.POST)
	public String login(@RequestParam("username") String username, @RequestParam("password") String password,
	                    HttpSession session, RedirectAttributes redirectAttributes) {

	    try {
	        String hashedPasswordFromDatabase = accountService.getHashedPassword(username);

	        if (hashedPasswordFromDatabase == null || !encoder.matches(password, hashedPasswordFromDatabase)) {
	            // If authentication failed, redirect to the login page with a message
	            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
	            return "redirect:/user/account/login";
	        }

	        // If authentication successful, store the authenticated account in session
	        Account authenticatedAccount = accountService.findByUsername(username);
	        session.setAttribute("authenticatedAccount", authenticatedAccount);

	        // Redirect to the dashboard
	        return "redirect:/user/home/dashboard";
	    } catch (Exception e) {
	        // Log the exception and handle it appropriately (e.g., show a generic error message)
	        redirectAttributes.addFlashAttribute("error", "An error occurred during authentication");
	        return "redirect:/user/account/login";
	    }
	}
	
	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String register(ModelMap modelMap) {
		modelMap.put("account", new Account()); 
		modelMap.put("roles", roleService.findAll());
		modelMap.put("roles", roleService.limit12());
		//modelMap.put("roles", roleService.findAll());		
		return "user/account/register";
	}
	
	@RequestMapping(value = "register", method = RequestMethod.POST) 
	public String register(@ModelAttribute("account") @Validated Account account,
	                       RedirectAttributes redirectAttributes,
	                       @RequestParam("file") MultipartFile file, BindingResult bindingResult
	                      ) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (account.getUsername().equals(authentication.getName())==false) {	
	        try {	 	        	
	            // Xử lý upload file và các logic khác
	            if (file != null && file.getSize() > 0) {
	                File folderImage = new File(new ClassPathResource(".").getFile().getPath() + "/static/images");
	                String fileName = FileHelper.generateFileName(file.getOriginalFilename());
	                Path path = Paths.get(folderImage.getAbsolutePath() + File.separator + fileName);
	                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	                account.setPhoto(fileName);
	            } else {
	                account.setPhoto("no-image.jpg");
	            }
	          
	            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    		System.out.println("dob: " + simpleDateFormat.format(account.getDob()));
	            account.setStatus(false);
	            String securityCode = SecurityCodeHelper.generate();
	    		account.setSecurityCode(securityCode);	    		
	            account.setPassword(encoder.encode(account.getPassword()));
	            if (accountService.save(account)) {       	
	            	String content = "Nhấn vào <a href='http://localhost:8083/user/account/verify?code=" + securityCode +" &username="+ account.getUsername() +"'>đây</a> để kích hoạt";
	            	mailService.send(environment.getProperty("spring.mail.username"), account.getEmail(), "Verify email", content);
	                return "redirect:/user/account/login";
	            } else {
	                redirectAttributes.addFlashAttribute("msg", "Failed");
	                return "redirect:/user/account/register";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            redirectAttributes.addFlashAttribute("msg", e.getMessage());
	        }
	        return "redirect:/user/account/login";
	    }
	    return "redirect:/user/account/register";
	}
	
	@RequestMapping(value = "verify", method = RequestMethod.GET)
	public String verify(@RequestParam("username") String username, @RequestParam("code") String code,
	        RedirectAttributes redirectAttributes) {
	    Account account = accountService.findByUsername(username);
	    if (account == null) {
	        redirectAttributes.addFlashAttribute("msg", "Username does not exist");
	    } else {
	        if (code != null && !code.trim().isEmpty() && code.equals(account.getSecurityCode())) {
	            account.setStatus(true);
	            if (accountService.save(account)) {
	                redirectAttributes.addFlashAttribute("msg", "Successful activation");
	            } else {
	                redirectAttributes.addFlashAttribute("msg", "Activation failed");
	            }
	        } else {
	            redirectAttributes.addFlashAttribute("msg", "Invalid verification code");
	        }
	    }
	    return "redirect:/user/account/login";
	}
	
	@RequestMapping(value = "forgetpassword", method = RequestMethod.GET)
	public String forgetpassword() {
		return "user/account/forgetpassword";
	}
	
	@RequestMapping(value = "forgetpassword", method = RequestMethod.POST)
	public String forgetpassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
		Account account = accountService.findByEmail(email);
		if (account == null) {
			redirectAttributes.addFlashAttribute("msg", "Email not found");
			return "redirect:/user/account/forgetpassword";
		} else {
			String securityCode = SecurityCodeHelper.generate();
			account.setSecurityCode(securityCode);
			if (accountService.save(account)) {

				//String content = "Nhấn vào <a href='http://localhost:8083/admin/account/verify?code=" + securityCode +" &username="+ account.getUsername() +"'>đây</a> để kích hoạt";
				String content = "Nhấn vào <a href='http://localhost:8083/user/account/updatepassword?code=" + securityCode + "&username=" + account.getUsername() + "'>đây</a> để cập nhật mật khẩu";
				mailService.send(environment.getProperty("spring.mail.username"), account.getEmail(), "Update Password",
						content);
			}
			return "redirect:/user/account/login";
		}
	}
	
	@RequestMapping(value = "updatepassword", method = RequestMethod.GET)
	public String updatepassword(@RequestParam("username") String username, @RequestParam("code") String code,
	        RedirectAttributes redirectAttributes, ModelMap modelMap) {
		Account account = accountService.findByUsername(username);
	    if (account == null) {
	        redirectAttributes.addFlashAttribute("msg", "Username not found");
	    } else {
	        if (code.equals(account.getSecurityCode())) {
	        	modelMap.put("account", account);
	            redirectAttributes.addFlashAttribute("username", username);
	            return "user/account/updatepassword";
	        } else {
	            redirectAttributes.addFlashAttribute("msg", "Security Code is Invalid");
	        }
	    }
	    return "redirect:/user/account/login";
	}
	
	
	
	@RequestMapping(value = "updatepassword/{username}", method = RequestMethod.POST)
	public String updatepassword1(@ModelAttribute("account") Account account,
			@RequestParam("newpassword") String newpassword, RedirectAttributes redirectAttributes) {
		Account new_account = accountService.findByUsername(account.getUsername());
		if (account == null) {
			redirectAttributes.addFlashAttribute("msg", "Username not found");
		} else {
			new_account.setPassword(encoder.encode(newpassword));
			if (accountService.save(new_account)) {
				redirectAttributes.addFlashAttribute("msg", "Update password success");
			} else {
				redirectAttributes.addFlashAttribute("msg", "Update password failed");
			}
		}
		return "redirect:/user/account/login";
	}
	
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {	   
	    session.invalidate();	    	
	    SecurityContextHolder.clearContext();	    	  
	    return "redirect:/user/account/login";
	}

}
