package com.xhamster.controllers;

import com.xhamster.repos.*;
import com.xhamster.models.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Controller
public class ApagaProdutoController {

	private static final Logger log = LoggerFactory.getLogger(ApagaProdutoController.class);

	@Autowired
	private UtilizadorRepo utilizadorRepo;

	@Autowired
	private ProdutoRepo produtoRepo;

	@GetMapping("/del-product")
	public String delProductView(
			@RequestParam(name = "p_id", required=false) Long p_id,
			Model model){
		
		List<Produto> prodList;

		prodList = (List<Produto>) produtoRepo.findAll();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", authentication.getName());
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(ga.toString().equals("ADMIN"))
					model.addAttribute("auth", ga.toString());
			}
		}
		
		if(p_id == null){

			model.addAttribute("prodList", prodList);
			model.addAttribute("prodListSize", String.format("%d",prodList.size()));
			return "del-product";
		}

		Produto prod = produtoRepo.findById(p_id).orElse(null);

		produtoRepo.deleteById(p_id);

		prodList = (List<Produto>) produtoRepo.findAll();

		model.addAttribute("dprodname", prod.getNome());
		model.addAttribute("prodList", prodList);
		model.addAttribute("prodListSize", String.format("%d",prodList.size()));


		return "index";
	}

}
