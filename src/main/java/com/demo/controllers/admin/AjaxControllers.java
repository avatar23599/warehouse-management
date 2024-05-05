package com.demo.controllers.admin;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.util.mime.MimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("admin/ajax")//có một đường dẫn

public class AjaxControllers {
	
	@RequestMapping(value = {"calculate"}, method = RequestMethod.GET, produces = MimeTypeUtils.TEXT_PLAIN_VALUE)//có thể sử dụng nhiều đường dẫn chỉ cần có {}
	public double calculate(@RequestParam("quantity") String quantity) {
		System.out.println(quantity);
		return Double.parseDouble(quantity) * 3;
	}
	
	
}
