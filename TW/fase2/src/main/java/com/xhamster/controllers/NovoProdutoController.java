package com.xhamster.controllers;

import com.xhamster.repos.ProdutoRepo;
import com.xhamster.models.Produto;
import com.xhamster.services.StorageService;
import com.xhamster.exceptions.StorageException;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Controller
public class NovoProdutoController {

	private static final Logger log = LoggerFactory.getLogger(NovoProdutoController.class);
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private ProdutoRepo produtoRepo;
	
	@GetMapping("/new-product")
	public String createProductView(Model model) 
	{

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", authentication.getName());
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(ga.toString().equals("ADMIN"))
					model.addAttribute("auth", ga.toString());
			}
		}

		return "new-product";
	}


	@PostMapping("/new-product")
	public String createProduct(
			@RequestParam(name = "descricao", required=true) String descr,
			@RequestParam(name = "raca", required=true) String nome,
			@RequestParam(name = "categorias", required=true) String categoria,
			@RequestParam(name = "preco", required=true) Double preco,
			@RequestParam(name = "imagem", required=true) MultipartFile image,
			Model model
			) 
	{
		String imagem = storageService.uploadFile(image);

		produtoRepo.save(new Produto(imagem, descr, nome, categoria, preco));

		List<Produto> prodList =(List<Produto>) produtoRepo.findAll();

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
		model.addAttribute("nomeprod", nome);
		return "index";

	}

	@ExceptionHandler(StorageException.class)
	public String handleStorageFileNotFound(StorageException e) {
		throw e;
	}

}
