package com.xhamster.controllers;

import com.xhamster.repos.ProdutoRepo;
import com.xhamster.models.Produto;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Controller
public class ProdutoController {

	private static final Logger log = LoggerFactory.getLogger(ProdutoController.class);

	@Autowired
	private ProdutoRepo produtoRepo;
	
	@GetMapping("/product")
	public String productView(
			@RequestParam(name = "p_id", required = true) Long p_id,
			Model model)
	{
		Produto prod = produtoRepo.findById(p_id).orElse(null);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", authentication.getName());
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(ga.toString().equals("ADMIN"))
					model.addAttribute("auth", ga.toString());
			}
		}

		model.addAttribute("prod", prod);

		return "product";

	}


	@PostMapping("/product")
	public String createProduct(
			@RequestParam(name = "p_id", required=true) Long p_id,
			Model model
			) 
	{

		Produto prod = produtoRepo.findById(p_id).orElse(null);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", authentication.getName());
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(ga.toString().equals("ADMIN"))
					model.addAttribute("auth", ga.toString());
			}
		}

		model.addAttribute("prod", prod);

		return "product";

	}
}
