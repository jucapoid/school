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
public class AdvancedSearchController {

	private static final Logger log = LoggerFactory.getLogger(AdvancedSearchController.class);

	@Autowired
	private ProdutoRepo produtoRepo;
	
	@GetMapping("/advanced_search")
	public String advancedSearchView(Model model) 
	{

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", authentication.getName());
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(ga.toString().equals("ADMIN"))
					model.addAttribute("auth", ga.toString());
			}
		}
		return "advanced_search";

	}

	@PostMapping("/advanced_search")
	public String advancedSearch(
			@RequestParam(name = "search", required=false) String descr,
			@RequestParam(name = "categorias", required=false) String categoria,
			@RequestParam(name = "precomin", required=false) Double precomin,
			@RequestParam(name = "precomax", required=false) Double precomax,
			Model model
			) 
	{
		List<Produto> prodList = (List<Produto>) produtoRepo.findAll();
		model.addAttribute("totalProdListSize", prodList.size());

		if(precomax == null) { precomax = 100000.0; }
		if(precomin == null) { precomin = 0.0; }

		if(descr != null && categoria != null){
			prodList =(List<Produto>) produtoRepo.findByCategoriaAndDescrContainingAndPrecoBetween(categoria, descr, precomin, precomax);
		}
		else if(categoria != null){
			prodList =(List<Produto>) produtoRepo.findByCategoriaAndPrecoBetween(categoria, precomin, precomax);
		}
		else if(descr != null){
			prodList =(List<Produto>) produtoRepo.findByDescrContainingAndPrecoBetween(descr, precomin, precomax);
		}
		else{
			prodList =(List<Produto>) produtoRepo.findByPrecoBetween(precomin, precomax);
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

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
