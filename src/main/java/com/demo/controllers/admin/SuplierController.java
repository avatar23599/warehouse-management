package com.demo.controllers.admin;



import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.entities.Category;
import com.demo.entities.Inputifo;
import com.demo.entities.Product;
import com.demo.entities.Suplier;
import com.demo.services.SuplierService;

import jakarta.servlet.http.HttpSession;

import com.demo.services.ProductService;


@Controller
@RequestMapping({"admin/suplier", "suplier"})
public class SuplierController {

	@Autowired
	private SuplierService suplierService;
	
	@Autowired
	private ProductService productService;

	@RequestMapping(value = { "index", "", "/" }, method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		modelMap.put("supliers", suplierService.findAll());
		return "admin/suplier/index";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(ModelMap modelMap) {
		modelMap.put("suplier", new Suplier());
		return "admin/suplier/add";
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(@ModelAttribute("suplier") Suplier suplier) {
	 suplierService.save(suplier);
		return "redirect:/admin/suplier/index";
	}
	
	@RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") int id, ModelMap modelMap, HttpSession session) {
			session.setAttribute("suplier_id", id);
			var supiler = suplierService.find(id);
		    modelMap.put("suplier", supiler);
		    List<Product> products = productService.findSuplier(id);
		    modelMap.put("products", products);
	    return "admin/suplier/detail";
	}
	
	

	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") int id, ModelMap modelMap) {
			modelMap.put("suplier", suplierService.find(id));
			return "admin/suplier/edit";
	}
	
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String edit(@ModelAttribute("suplier") Suplier suplier) {		
		 suplierService.save(suplier);
		return "redirect:/admin/suplier/index";
		
		
		
	}
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		if (suplierService.delete(id)) {
			redirectAttributes.addFlashAttribute("msg", "Success");
		} else {
			redirectAttributes.addFlashAttribute("msg", "Fails");
		}
		return "redirect:/admin/suplier/index";
	}
	
	@RequestMapping(value = "searchByKeyword", method = RequestMethod.GET)
	public String searchByKeyword(@RequestParam("keyword") String keyword, ModelMap modelMap) {     
	    List<Suplier> searchResults = suplierService.searchByKeyword(keyword);  
	    modelMap.put("supliers", searchResults);
	    return "admin/suplier/index";
	}

}
