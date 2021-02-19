package com.xhamster.controllers;

import com.xhamster.repos.*;
import com.xhamster.models.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Controller
public class MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private UtilizadorRepo utilizadorRepo;

	@Autowired
	private ProdutoRepo produtoRepo;

	@GetMapping("/logout")
	public String logout(Model model){

		List<Produto> prodList = (List<Produto>) produtoRepo.findAll();
		model.addAttribute("totalProdListSize", prodList.size());
		model.addAttribute("prodList", prodList);
		model.addAttribute("prodListSize", prodList.size());
		return "index";

	}

	@GetMapping("/login")
	public String login(Model model){
		return "login";
	}

	@GetMapping(value = {"/index", "/"})
	public String indexView(Model model){

		List<Produto> prodList = (List<Produto>) produtoRepo.findAll();
		model.addAttribute("totalProdListSize", prodList.size());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", authentication.getName());
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(ga.toString().equals("ADMIN"))
					model.addAttribute("auth", ga.toString());
			}
		}

		model.addAttribute("prodList", prodList);
		model.addAttribute("prodListSize", prodList.size());
		return "index";
	}

}
