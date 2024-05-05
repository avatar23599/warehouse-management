package com.demo.controllers.superadmin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

import com.demo.Repository.AccountRepository;
import com.demo.entities.Account;

import com.demo.helpers.FileHelper;
import com.demo.helpers.SecurityCodeHelper;
import com.demo.services.AccountService;
import com.demo.services.MailService;
import com.demo.services.ProductService;
import com.demo.services.RoleService;

@Controller
@RequestMapping("superadmin/home")
public class HomeSuperAdminController {
	@Autowired
	private AccountService accountService;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ProductService productService;

	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private Environment environment;

	@RequestMapping(value = { "", "index" }, method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		modelMap.put("accounts", accountService.findAll());
		modelMap.put("roles", roleService.findAll());
		
		return "superadmin/home/index";
	}
	
	
	
	@RequestMapping(value = { "", "add" }, method = RequestMethod.GET)
	public String add(@ModelAttribute("account") Account account,ModelMap modelMap) {
		modelMap.put("accounts", accountService.findAll());
		modelMap.put("roles", roleService.findAll());
		return "superadmin/home/add";
	}
	
	

	@RequestMapping(value = { "", "add" }, method = RequestMethod.POST)
	public String add(@ModelAttribute("account") @Validated Account account, RedirectAttributes redirectAttributes,
	        @RequestParam("file") MultipartFile file, @RequestParam("password") String password, BindingResult bindingResult) {
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
	            	String content = "Nhấn vào <a href='http://localhost:8083/superadmin/home/verify?code=" + securityCode +" &username="+ account.getUsername() +"'>đây</a> để kích hoạt";
	            	mailService.send(environment.getProperty("spring.mail.username"), account.getEmail(), "Verify email", content);
	                return "redirect:/superadmin/home/index";
	            } else {
	                redirectAttributes.addFlashAttribute("msg", "Failed");
	                return "redirect:/superadmin/home/add";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            redirectAttributes.addFlashAttribute("msg", e.getMessage());
	        }
	        return "redirect:/superadmin/home/index";
	    }
	    return "redirect:/superadmin/home/add";
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
	    return "redirect:/superadmin/home/index";
	}
	


	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String edit(@ModelAttribute("account") @Validated Account account, RedirectAttributes redirectAttributes,
	        @RequestParam("file") MultipartFile file, @RequestParam("password") String password, @RequestParam("securityCode") String securityCode, BindingResult bindingResult) {
	    try {
	        // Xử lý upload file và các logic khác
	        if (file != null && file.getSize() > 0) {
	            File folderImage = new File(new ClassPathResource(".").getFile().getPath() + "/static/images");
	            String fileName = FileHelper.generateFileName(file.getOriginalFilename());
	            Path path = Paths.get(folderImage.getAbsolutePath() + File.separator + fileName);
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            account.setPhoto(fileName);
	            
	        } else {
	            account.setPhoto(account.getPhoto());	                       
	        } 
	        
	        if(!password.isEmpty()) {

	            account.setPassword(encoder.encode(account.getPassword()));
	        }else {
	        	var last_account = accountService.find(account.getId());
	        	account.setPassword(last_account.getPassword());
	        }
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	        simpleDateFormat.format(account.getDob());
	        
	        if (accountService.save(account)) {
	            return "redirect:/superadmin/home/index";
	        } else {
	            redirectAttributes.addFlashAttribute("msg", "Failed");
	            return "redirect:/superadmin/home/edit";
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("msg", e.getMessage());
	        return "redirect:/superadmin/home/details";
	    }
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		if (accountService.delete(id)) {
			redirectAttributes.addFlashAttribute("msg", "Success");
		} else {
			redirectAttributes.addFlashAttribute("msg", "Fails");
		}
		return "redirect:/superadmin/home/index";
	}

	@RequestMapping(value = "details/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") int id, ModelMap modelMap) {
		modelMap.put("account", accountService.find(id));
		modelMap.put("roles", roleService.findAll());
		return "superadmin/home/details";
	}
	
	
	@RequestMapping(value = "searchByKeyword", method = RequestMethod.GET)
	public String searchByKeyword(@RequestParam("keyword") String keyword, ModelMap modelMap) {     
	    List<Account> searchResults = accountService.searchByKeyword(keyword);  
	    modelMap.put("accounts", searchResults);
	    return "superadmin/home/index";
	}
}
	
