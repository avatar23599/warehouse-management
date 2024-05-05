package com.demo.controllers.admin;



import java.util.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.dtos.InputDTO;
import com.demo.entities.Inputifo;
import com.demo.entities.Item;
import com.demo.entities.Output;
import com.demo.entities.Outputinfor;
import com.demo.entities.Product;
import com.demo.services.AccountService;
import com.demo.services.CartService;
import com.demo.services.CustomerService;
import com.demo.services.InputInfoService;
import com.demo.services.InputService;
import com.demo.services.OutputInfoService;
import com.demo.services.OutputService;
import com.demo.services.ProductService;
import com.demo.services.SuplierService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping({ "admin/cart", "cart"})
public class CartController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private InputService inputService;
	
	@Autowired
	private InputInfoService  inputInfoService;
	
	@Autowired
	private SuplierService  suplierService;
	
	@Autowired
	private CustomerService  customerService;
	
	@Autowired
	private OutputService  outputService;
	
	@Autowired
	private OutputInfoService  outputInfoService;
	
	@RequestMapping(value = "index/{id}", method = RequestMethod.GET)
	public String index(HttpSession session, ModelMap modelMap, @PathVariable("id") int id) {
		modelMap.put("suplier", suplierService.find(id));
		modelMap.put("customer", customerService.find(id));
		if(session.getAttribute("cart") != null) {
			List<Item> cart = (List<Item>) session.getAttribute("cart");
			modelMap.put("total", cartService.total(cart));
		}
		return "admin/cart/index";
	}
	
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public String uploadExcel(@RequestParam("excelFile") MultipartFile excelFile, HttpSession session) {
     var list =  cartService.uploadExcel(excelFile); // Trả về danh sách dữ liệu của bảng Excel
     List<Product> list_product = new ArrayList<>();
     List<Double> quantities = new ArrayList<>();
     int suplier_id = 0;
     for(int i = 0; i < list.size(); i++) {
    	 if(i == 0) {
    		 System.out.println(list.get(i));
        	 for(int y = 1; y < list.get(i).size(); y++) {
        		 suplier_id = Integer.parseInt(list.get(i).get(y));
        	 }
    	 }else if(i == 1) {
    		 for(int y = 1; y < list.get(i).size(); y++) {
    			 Product product = productService.find(Double.valueOf(list.get(i).get(y)).intValue());
        		 list_product.add(product);
         	 } 
    	 }else{
    		 for(int y = 1; y < list.get(i).size(); y++) {
    			 quantities.add(Double.parseDouble(list.get(i).get(y)));
    		 }
    	 }
     }
     
     
     
     
     if(session.getAttribute("cart") == null){
			List<Item> cart = new ArrayList<Item>();
			for(int i = 0; i < quantities.size(); i++) {
				cart.add(new Item(list_product.get(i),Double.valueOf(quantities.get(i)).intValue()));
			}
			session.setAttribute("cart", cart);
		}else {
			List<Item> cart = (List<Item>) session.getAttribute("cart");
			for(int i = 0; i < quantities.size(); i++) {
				int index = cartService.exist(list_product.get(i).getId(), cart);
				if(index == -1) {
					cart.add(new Item(list_product.get(i),Double.valueOf(quantities.get(i)).intValue()));
				}else {
					
					int quantity_new =  cart.get(index).getQuantity()+ Double.valueOf(quantities.get(i)).intValue();
					cart.get(index).setQuantity(quantity_new);
				}
			}
			session.setAttribute("cart", cart);
		}
     return "redirect:/admin/cart/index/"+ suplier_id;
    }
	
	@RequestMapping(value = "addtocart/{id}", method = RequestMethod.GET)
	public String addtocart(@PathVariable("id") int id, HttpSession session, RedirectAttributes redirectAttributes) {
		Product product = productService.find(id);
		var id_suplier = session.getAttribute("suplier_id").toString();
		if(session.getAttribute("cart") == null){
			List<Item> cart = new ArrayList<Item>();
			cart.add(new Item(product, 1));
			session.setAttribute("cart", cart);
		}else {
			List<Item> cart = (List<Item>) session.getAttribute("cart");
			int index = cartService.exist(id, cart);
			if(index == -1) {
				cart.add(new Item(product, 1));
			}else {
				int quantity_new = cart.get(index).getQuantity()+1;
				cart.get(index).setQuantity(quantity_new);
			}
			session.setAttribute("cart", cart);
		}
		redirectAttributes.addFlashAttribute("msg", "ADD SUCCESS");
		return "redirect:/admin/suplier/detail/" + id_suplier;
	}
	
	
	
	@Transactional
	@RequestMapping(value = "checkout/{id}", method = RequestMethod.GET)
	public String checkout(HttpSession session, @PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getName() == null) {
			return "redirect:/admin/account/login";
		}else {
			String username = authentication.getName();
			
			InputDTO inputDTO = new InputDTO();
			inputDTO.setAccountId(accountService.findByUsername(username).getId());
			LocalDateTime currentTime = LocalDateTime.now();
			inputDTO.setDate(currentTime);
			if(accountService.findByUsername(username).getRole().getId() == 1) {
				inputDTO.setInputinfo("Processing");
				inputDTO.setStatus("Processing");
			}else {
				inputDTO.setInputinfo("Pending approval");
				inputDTO.setStatus("Pending approval");
			}
			inputDTO.setSuplierId(id);
			var input = inputService.save(inputDTO);
			//luu chi tiet hoa don
			List<Item> cart = (List<Item>) session.getAttribute("cart");
			for(Item item : cart) {
				Inputifo inputInfo = new Inputifo();
				inputInfo.setCount(item.getQuantity());
				inputInfo.setProductId(item.getProduct().getId());
				inputInfo.setInput(input);
				inputInfoService.save(inputInfo);
			}
			session.removeAttribute("cart");
			redirectAttributes.addFlashAttribute("msg", "ADD SUCCESS");
			return "redirect:/admin/order/list_input";
		}
		
	}
	
	
	//Output
	@RequestMapping(value = "upload_output", method = RequestMethod.POST)
	public String upload_output(@RequestParam("excelFile") MultipartFile excelFile, HttpSession session,@RequestParam("customer_id") int customer_id ) {
     var list =  cartService.uploadExcel(excelFile); // Trả về danh sách dữ liệu của bảng Excel
     List<Product> list_product = new ArrayList<>();
     List<Double> quantities = new ArrayList<>();
     for(int i = 0; i < list.size(); i++) {
    	 if(i == 0) {
    		 System.out.println(list.get(i));
        	 for(int y = 1; y < list.get(i).size(); y++) {
        		 System.out.println(list.get(i).get(y));
        		 Product product = productService.find(Double.valueOf(list.get(i).get(y)).intValue());
        		 list_product.add(product);
        	 }
    	 }else{
    		 for(int y = 1; y < list.get(i).size(); y++) {
    			 System.out.println(list.get(i).get(y));
    			 quantities.add(Double.parseDouble(list.get(i).get(y)));
    		 }
    	 }
     }
     
     if(session.getAttribute("cart_output") == null){
			List<Item> cart_output = new ArrayList<Item>();
			for(int i = 0; i < quantities.size(); i++) {
				cart_output.add(new Item(list_product.get(i),Double.valueOf(quantities.get(i)).intValue()));
			}
			session.setAttribute("cart_output", cart_output);
		}else {
			List<Item> cart_output = (List<Item>) session.getAttribute("cart_output");
			for(int i = 0; i < quantities.size(); i++) {
				int index = cartService.exist(list_product.get(i).getId(), cart_output);
				if(index == -1) {
					cart_output.add(new Item(list_product.get(i),Double.valueOf(quantities.get(i)).intValue()));
				}else {
					
					int quantity_new =  cart_output.get(index).getQuantity()+ Double.valueOf(quantities.get(i)).intValue();
					cart_output.get(index).setQuantity(quantity_new);
				}
			}
			session.setAttribute("cart_output", cart_output);
		}
     
     return "redirect:/admin/cart/cart_output/"+ customer_id;
	}
	
	@RequestMapping(value = "cart_output/{id}", method = RequestMethod.GET)
	public String cart_output(HttpSession session, ModelMap modelMap, @PathVariable("id") int id) {
		modelMap.put("customer", customerService.find(id));
		if(session.getAttribute("cart_output") != null) {
			List<Item> cart_output = (List<Item>) session.getAttribute("cart_output");
			modelMap.put("total", cartService.total(cart_output));
		}
		return "admin/cart/cart_output";
	}
	
	
	
	@Transactional
	@RequestMapping(value = "checkout_output/{id}", method = RequestMethod.GET)
	public String checkout_output(HttpSession session, @PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getName() == null) {
			return "redirect:/admin/account/login";
		}else {
			LocalDateTime currentTime = LocalDateTime.now();
			String username = authentication.getName();
			Output output= new Output();
			output.setAccount(accountService.findByUsername(username));
			output.setCustomer(customerService.find(id));
			output.setDate(new Date());
			output.setStatus("Processing");
			var output_new = outputService.save(output);
			if(accountService.findByUsername(username).getRole().getId() == 1) {
				output.setStatus("Processing");
			}else {
				output.setStatus("Wait for confirmation");
			}
			
			//luu chi tiet hoa don
			List<Item> cart_output = (List<Item>) session.getAttribute("cart_output");
			System.out.println(cart_output);
			for(Item item : cart_output) {
				Outputinfor outputInfo = new Outputinfor();
				outputInfo.setCount(item.getQuantity());
				outputInfo.setProduct(item.getProduct());
				outputInfo.setPrice(item.getQuantity() + item.getProduct().getPrice() );
				outputInfo.setOutput(output_new);
				outputInfoService.save(outputInfo);
			}
			session.removeAttribute("cart_output");
			
			redirectAttributes.addFlashAttribute("msg", "ADD SUCCESS");
			return "redirect:/admin/order/list_output";
		}
		
	}
//	
//	@RequestMapping(value = "success", method = RequestMethod.GET)
//	public String success(@RequestParam("PayerID") String payerID, HttpServletRequest request) {
//		PayPalConfig payPalConfig = new PayPalConfig();
//		payPalConfig.setAuthToken(environment.getProperty("paypal.authtoken"));
//		payPalConfig.setBusiness(environment.getProperty("paypal.business"));
//		payPalConfig.setPosturl(environment.getProperty("paypal.posturl"));
//		payPalConfig.setReturnurl(environment.getProperty("paypal.returnurl"));
//		
//		PayPalSucess payPalSucess = new PayPalSucess();
//		PayPalResult result = payPalSucess.getPayPal(request, payPalConfig);
//		if(request == null) {
//			System.out.println("Failed");
//		}else {
//			System.out.println("Transacion Info");
//			System.out.println("first name: " + result.getFirst_name());
//			System.out.println("last name: " + result.getLast_name());
//			System.out.println("street: " + result.getAddress_street());
//			
//		}
//		return "cart/success";
//	}
//	
	
}
