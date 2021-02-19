package com.xhamster.controllers;

import com.xhamster.models.*;
import com.xhamster.repos.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Controller
public class NovoUtilizadorController {

	private static final Logger log = LoggerFactory.getLogger(NovoUtilizadorController.class);

	@Autowired
	private UtilizadorRepo utilizadorRepo;
    
	@Autowired
	private ProdutoRepo produtoRepo;
    
	@GetMapping("/new-user")
	public String newUserView(Model model){

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", authentication.getName());
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(ga.toString().equals("ADMIN"))
					model.addAttribute("auth", ga.toString());
			}
		}

	       	return "new-user"; }


	@PostMapping(value = "/new-user")
	public String newUser(
			@RequestParam(name="username", required=true) String username,
			@RequestParam(name="password", required=true) String password,
			@RequestParam(name="email", required=true) String email, 
			Model model)
	{

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", authentication.getName());
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(ga.toString().equals("ADMIN"))
					model.addAttribute("auth", ga.toString());
			}
		}
		
		String encodedPassword = new BCryptPasswordEncoder().encode(password);
		utilizadorRepo.save(new Utilizador(username, encodedPassword, email));

		List<Produto> prodList = (List<Produto>) produtoRepo.findAll();
		
		model.addAttribute("prodList", prodList);
		model.addAttribute("prodListSize", String.format("%d",prodList.size()));
		model.addAttribute("username", username);
		return "index";
	}
}
