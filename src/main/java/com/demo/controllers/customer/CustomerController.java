package com.demo.controllers.customer;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.entities.Inputifo;
import com.demo.entities.Product;
import com.demo.entities.Customer;
import com.demo.services.CustomerService;



@Controller
@RequestMapping({"employee/customer", "customer"})
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	

	@RequestMapping(value = { "index", "", "/" }, method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		modelMap.put("customers", customerService.findAll());
		return "employee/customer/index";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(ModelMap modelMap) {
		modelMap.put("customer", new Customer());
		return "employee/customer/add";
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(@ModelAttribute("customer") Customer customer) {
		customerService.save(customer);
		 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
 		System.out.println("dob: " + simpleDateFormat.format(customer.getDate()));
		return "redirect:/employee/customer/index";
	}
	
	@RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") int id, ModelMap modelMap) {
			var customer = customerService.find(id);
		    modelMap.put("customer", customer);
	    return "employee/customer/detail";
	}
	
	

	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") int id, ModelMap modelMap) {
			modelMap.put("customer", customerService.find(id));
			return "employee/customer/edit";
	}
	
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String edit(@ModelAttribute("customer") Customer customer) {
		Customer customer_last = customerService.find(customer.getId());

		customerService.save(customer);
		return "redirect:/employee/customer/index";
	}
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		if (customerService.delete(id)) {
			redirectAttributes.addFlashAttribute("msg", "Success");
		} else {
			redirectAttributes.addFlashAttribute("msg", "Fails");
		}
		return "redirect:/employee/customer/index";
	}

}
