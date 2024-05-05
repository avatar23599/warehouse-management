package com.demo.controllers.admin;


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
import com.demo.entities.Product;
import com.demo.services.CategoryService;


@Controller
@RequestMapping({"admin/category", "category"})
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = { "index", "", "/" }, method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		modelMap.put("categories", categoryService.findAll());
		return "admin/category/index";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(ModelMap modelMap) {
		modelMap.put("category_last", new Category());
		modelMap.put("categories", categoryService.findAll());
		
		return "admin/category/add";
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(@ModelAttribute("category") Category category) {
	 categoryService.save(category);
		return "redirect:/admin/category/index";
	}

	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") int id, ModelMap modelMap) {
			modelMap.put("category_last", categoryService.find(id));
			modelMap.put("categories", categoryService.findAll());
			return "admin/category/edit";
	}
	
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String edit(@ModelAttribute("category") Category category) {
		 categoryService.save(category);
		return "redirect:/admin/category/index";
		
		
		
	}
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		if (categoryService.delete(id)) {
			redirectAttributes.addFlashAttribute("msg", "Success");
		} else {
			redirectAttributes.addFlashAttribute("msg", "Fails");
		}
		return "redirect:/admin/category/index";
	}
	
	@RequestMapping(value = "searchByKeyword", method = RequestMethod.GET)
	public String searchByKeyword(@RequestParam("keyword") String keyword, ModelMap modelMap) {     
	    List<Category> searchResults = categoryService.searchByKeyword(keyword);  
	    modelMap.put("categories", searchResults);
	    return "admin/category/index";
	}

}
