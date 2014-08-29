package com.acme.xgpa.controllers;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.base.CaseFormat;

@Controller
@Order(1)
public class PagesController {

	@RequestMapping(value = "/pages/{page}")
	public String page(@PathVariable String page) {
		return "pages/"
				+ CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, page);
	}

	@RequestMapping(value = "/pages/{folder}/{page}")
	public String page(@PathVariable String folder, @PathVariable String page) {
		return "pages/" + folder + "/"
				+ CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, page);
	}

	@RequestMapping(value = "/pages/{folder1}/{folder2}/{page}")
	public String page(@PathVariable String folder1,
			@PathVariable String folder2, @PathVariable String page) {
		return "pages/" + folder1 + "/" + folder2 + "/"
				+ CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, page);
	}
}
