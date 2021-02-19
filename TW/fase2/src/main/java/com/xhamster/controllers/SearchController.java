package com.xhamster.controllers;

import com.xhamster.MyUserDetails;
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
public class SearchController {

	private static final Logger log = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	private ProdutoRepo produtoRepo;
	
	@GetMapping("/search")
	public String simpleSearch(
			@RequestParam(name = "produtos", required=false) String descr,
			Model model
			) 
	{
		List<Produto> prodList = (List<Produto>) produtoRepo.findAll();
		model.addAttribute("totalProdListSize", prodList.size());

		prodList = produtoRepo.findByDescrContainingOrNomeContaining(descr, descr);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info(authentication.getAuthorities().toString());

		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", authentication.getName());
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(ga.toString().equals("ADMIN"))
					model.addAttribute("auth", ga.toString());
			}
		}

		model.addAttribute("prodList", prodList);
		model.addAttribute("prodListSize", String.format("%d",prodList.size()));
		return "index";

	}
}
