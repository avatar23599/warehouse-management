package com.demo.controllers.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.dtos.InputDTO;
import com.demo.dtos.InputInfoDTO;
import com.demo.entities.Input;
import com.demo.entities.Inputifo;
import com.demo.entities.Item;
import com.demo.entities.Product;
import com.demo.helpers.FileHelper;
import com.demo.services.AccountService;
import com.demo.services.CartService;
import com.demo.services.CategoryService;
import com.demo.services.CustomerService;
import com.demo.services.InputInfoService;
import com.demo.services.InputService;
import com.demo.services.OutputInfoService;
import com.demo.services.OutputService;
import com.demo.services.ProductService;
import com.demo.services.SuplierService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping({ "admin/order", "order", })
public class OrderController {
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private InputService inputService;
	
	@Autowired
	private OutputService outputService;
	
	@Autowired
	private InputInfoService inputinfoService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private SuplierService suplierService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OutputInfoService outputInfoService;
	
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(ModelMap modelMap) {
		modelMap.put("products", productService.findAll());
		modelMap.put("inputDTO", new InputDTO());
		return "admin/order/input";
	}
	
	@RequestMapping(value = "list_input", method = RequestMethod.GET)
	public String list_input(ModelMap modelMap) {
		modelMap.put("inputs", inputService.findAll2());
		return "admin/order/list_input";
	}
	
	//Output
	@RequestMapping(value = "list_output", method = RequestMethod.GET)
	public String list_output(ModelMap modelMap) {
		modelMap.put("outputs", outputService.findAll2());
		return "admin/order/list_output";
	}
	
	@RequestMapping(value = "pending_approval", method = RequestMethod.GET)
	public String pending_approval(ModelMap modelMap,  RedirectAttributes redirectAttributes) {
		modelMap.put("inputs", inputService.findByStatus("Pending approval"));
		return "admin/order/pending_approval";
	}
	
	@RequestMapping(value = "confirmation_output", method = RequestMethod.GET)
	public String confirmation_output(ModelMap modelMap) {
		modelMap.put("ouputs", outputService.findByStatus("Wait for confirmation"));
		return "admin/order/confirmation_output";
	}
	
	@RequestMapping(value = "accept/{id}", method = RequestMethod.GET)
	public String accept(ModelMap modelMap, @PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		var input = inputService.find(id);
		input.setStatus("Processing");
		inputService.save(input);
		
		redirectAttributes.addFlashAttribute("msg", "SUCCESS");
		return "redirect:/admin/order/input";
	}
	
	@RequestMapping(value = "input", method = RequestMethod.GET)
	public String input(ModelMap modelMap) {
		modelMap.put("inputs", inputService.findByStatus("Processing"));
		return "admin/order/input";
	}
	
	@RequestMapping(value = "inputinfo/{id}", method = RequestMethod.GET)
	public String inputinfo(HttpSession session,ModelMap modelMap, @PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		session.setAttribute("id_input", id);
		var inputinfoDTOs = inputinfoService.findByIdInput(id);
		var input = inputService.find(id);
		var total = 0;
		System.out.println(inputinfoService.findByIdInput(id));
		for(var inputinfoDTO : inputinfoDTOs) {
			total += inputinfoDTO.getCount(); 
		}
		modelMap.put("account", accountService.find(input.getAccount().getId()));
		modelMap.put("suplier", suplierService.find(input.getSuplier().getId()));
		modelMap.put("inputinfoDTOs", inputinfoService.findByIdInput(id));
		modelMap.put("total", total);	
		return "admin/order/inputinfo";
	}
	
	@RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
	public String detail(HttpSession session,ModelMap modelMap, @PathVariable("id") int id) {
		var inputinfoDTOs = inputinfoService.findByIdInput(id);
		var input = inputService.find(id);
		var total = 0;
		System.out.println(inputinfoService.findByIdInput(id));
		for(var inputinfoDTO : inputinfoDTOs) {
			total += inputinfoDTO.getCount(); 
		}
		modelMap.put("account", accountService.find(input.getAccount().getId()));
		modelMap.put("suplier", suplierService.find(input.getSuplier().getId()));
		modelMap.put("inputinfoDTOs", inputinfoService.findByIdInput(id));
		modelMap.put("total", total);	
		modelMap.put("input", input);	
		return "admin/order/detail_input";
	}
	
	@RequestMapping(value = "inputinfo", method = RequestMethod.POST)
	public String inputinfo(HttpSession session,ModelMap modelMap, @RequestParam ("quantities") int[] quantities, RedirectAttributes redirectAttributes) {
		String id_input = session.getAttribute("id_input").toString();
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Input input = inputService.find(Integer.parseInt(id_input));
		var account = accountService.findByUsername(authentication.getName());
		var inputinfoDTOs = inputinfoService.findByIdInput(Integer.parseInt(id_input));
		var count = 0;
		List<Item> cart = new ArrayList();
		for(int i = 0; i < inputinfoDTOs.size(); i++) {
			var inputinfoDTO = inputinfoService.findById(inputinfoDTOs.get(i).getId());
			if(inputinfoDTOs.get(i).getCount() > quantities[i]) {
				// Them vao list
				int quantity = inputinfoDTO.getCount() - quantities[i];
				cart.add( new Item(inputinfoDTO.getProduct(), quantity));
				//sua so luong
				inputinfoDTO.setCount(quantities[i]);
				inputinfoService.save(inputinfoDTO);
				count++;
			}
			//update so luong san pham
			var product = productService.find(inputinfoDTO.getProduct().getId());
			product.setQuantity(product.getQuantity() + quantities[i]);
			productService.save(product);
			//Tao hoa don moi
			
		}
		if(count > 0) {
			InputDTO inputDTO = new InputDTO();
			LocalDateTime currentTime = LocalDateTime.now();
			inputDTO.setDate(currentTime);
			inputDTO.setInputinfo("Processing");
			inputDTO.setStatus("Processing");
			inputDTO.setSuplierId(input.getSuplier().getId());
			inputDTO.setAccountId(account.getId());
			var input_new = inputService.save(inputDTO);
			for(Item item : cart) {
				Inputifo inputInfo = new Inputifo();
				inputInfo.setCount(item.getQuantity());
				inputInfo.setInput(input_new);
				inputInfo.setProductId(item.getProduct().getId());
				inputinfoService.save(inputInfo);
			}
		}
		input.setStatus("Confirmed");
		input.setAccount(account);
		inputService.save(input);
		redirectAttributes.addFlashAttribute("msg", "SUCCESS");
		return "redirect:/admin/order/input";
	}
	
	@RequestMapping(value = "form_input", method = RequestMethod.GET)
	public String from_input(ModelMap modelMap) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		modelMap.put("account", accountService.findByUsername(authentication.getName()));
		return "admin/order/form_input";
	}
	 
	
	
	//Output
	@RequestMapping(value = "form_output", method = RequestMethod.GET)
	public String form_output(ModelMap modelMap) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		modelMap.put("account", accountService.findByUsername(authentication.getName()));
		modelMap.put("customers", customerService.findAll());
		return "admin/order/form_output";
	}
	
	@RequestMapping(value = "output", method = RequestMethod.GET)
	public String output(ModelMap modelMap) {
		modelMap.put("outputs", outputService.findByStatus("Processing"));
		return "admin/order/output";
	}
	
	
	
	
	@RequestMapping(value = "accept_comfirm/{id}", method = RequestMethod.GET)
	public String accept_comfirm(ModelMap modelMap, @PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		var output = outputService.find(id);
		output.setStatus("Processing");
		outputService.save(output);
		redirectAttributes.addFlashAttribute("msg", "SUCCESS");
		return "redirect:/admin/order/list_output";
	}
	
	@RequestMapping(value = "accept_output/{id}", method = RequestMethod.GET)
	public String accept_output(ModelMap modelMap, @PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		var output = outputService.find(id);
		var outputInfos = outputInfoService.findByIdOutput(id);
		for(int i = 0; i < outputInfos.size(); i++) {
			var product = outputInfos.get(i).getProduct();
			product.setQuantity(product.getQuantity() - outputInfos.get(i).getCount());
			productService.save(product);
		}
		output.setStatus("Comfirmed");
		outputService.save(output);
		redirectAttributes.addFlashAttribute("msg", "SUCCESS");
		return "redirect:/admin/order/list_output";
	}
	
	
	
	
	@RequestMapping(value = "detail_output/{id}", method = RequestMethod.GET)
	public String detail_output(HttpSession session,ModelMap modelMap, @PathVariable("id") int id) {
		var outputInfos = outputInfoService.findByIdOutput(id);
		var output = outputService.find(id);
		var total = 0;
		System.out.println(inputinfoService.findByIdInput(id));
		for(var outputInfo : outputInfos) {
			total += outputInfo.getCount(); 
		}
		modelMap.put("account", accountService.find(output.getAccount().getId()));
		modelMap.put("customer", suplierService.find(output.getCustomer().getId()));
		modelMap.put("outputInfos", outputInfoService.findByIdOutput(id));
		modelMap.put("total", total);	
		modelMap.put("output", output);	
		return "admin/order/detail_output";
	}

	
//	@Transactional
//	@RequestMapping(value = "create", method = RequestMethod.POST)
//	public String create(@ModelAttribute("inputDTO") InputDTO inputDTO, RedirectAttributes redirectAttributes) {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		var account = accountService.findByUsername(authentication.getName());
//		LocalDateTime currentTime = LocalDateTime.now();
//		inputDTO.setDate(currentTime);
//		inputDTO.setAccountId(account.getId());
//		if (inputService.save(inputDTO)) {
//			inputinfoDTO.setSuplierId(1);
//			inputinfoDTO.setInputId(1);
//			inputinfoDTO.setStatusId(1);
//			if (inputinfoService.save(inputinfoDTO)) {
//				return "redirect:/admin/product/index";
//			} else {
//				redirectAttributes.addFlashAttribute("msg", "Failed");
//				return "redirect:/admin/order/create";
//			}
//		} else {
//			redirectAttributes.addFlashAttribute("msg", "Failed");
//			return "redirect:/admin/order/create";
//		}
//	}

	@RequestMapping(value = "order_import/{id}", method = RequestMethod.GET)
	public String order_import(ModelMap modelMap, @PathVariable("id") int id) {
		modelMap.put("product", productService.find(id));
		return "admin/order/order_import";
	}
	
	

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(ModelMap modelMap) {
		modelMap.put("categories", categoryService.findAll());
		modelMap.put("product", new Product());
		return "admin/product/add";
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file) {
		try {
			if (file != null && file.getSize() > 0) {

				File folderImage = new File(new ClassPathResource(".").getFile().getPath() + "/static/assets/admin/images");
				String fileName = FileHelper.generateFileName(file.getOriginalFilename());
				Path path = Paths.get(folderImage.getAbsolutePath() + File.separator + fileName);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				product.setPicture(fileName);
				product.setStatus("In stock");
			} else {
				product.setPicture("no-image.jpg");
			}
			if (productService.save(product)) {
				return "redirect:/admin/product/index";
			} else {
				redirectAttributes.addFlashAttribute("msg", "Failed");
				return "redirect:/admin/product/add";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/product/index";
	}
	
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") int id, ModelMap modelMap) {
			modelMap.put("categories", categoryService.findAll());
			modelMap.put("product", productService.find(id));
			return "admin/product/edit";
	}
	
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String edit(@ModelAttribute("product") Product product, 
			RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file) {
		Product product_last = productService.find(product.getId());
		try {
			if (file != null && file.getSize() > 0) {

				File folderImage = new File(new ClassPathResource(".").getFile().getPath() + "/static/assets/admin/images");
				String fileName = FileHelper.generateFileName(file.getOriginalFilename());
				Path path = Paths.get(folderImage.getAbsolutePath() + File.separator + fileName);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				product.setPicture(fileName);
				product.setStatus("In stock");
			} else {
				product.setPicture(product_last.getPicture());
			}
			product.setStatus(product_last.getStatus());
			if (productService.save(product)) {
				return "redirect:/admin/product/index";
			} else {
				redirectAttributes.addFlashAttribute("msg", "Failed");
				return "redirect:/admin/product/edit";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/product/index";
	}
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		if (productService.delete(id)) {
			redirectAttributes.addFlashAttribute("msg", "Success");
		} else {
			redirectAttributes.addFlashAttribute("msg", "Fails");
		}
		return "redirect:/admin/product/index";
	}
	

}
