package com.xhamster.controllers;

import com.xhamster.repos.*;
import com.xhamster.models.*;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;

@Controller
public class EncomendaController {

	private static final Logger log = LoggerFactory.getLogger(EncomendaController.class);

	@Autowired
	private UtilizadorRepo utilizadorRepo;

	@Autowired
	private EncomendasRepo encomendasRepo;

	@Autowired
	private ProdutoRepo produtoRepo;

	@Autowired
	private DetalhesEncomendaRepo detailsRepo;

	@GetMapping("/list-order")
	public String orderListView(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Utilizador utilizador = utilizadorRepo.findOneByUsername(authentication.getName());

		Encomenda encomenda = encomendasRepo.findByUtilizadorAndIsActive(utilizador, true);
		
		if(encomenda == null){
			encomendasRepo.save(new Encomenda(utilizador));
			encomenda = encomendasRepo.findByUtilizadorAndIsActive(utilizador, true);
		}

		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", authentication.getName());
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(ga.toString().equals("ADMIN"))
					model.addAttribute("auth", ga.toString());
			}
		}
		List<Produto> prodList = (List<Produto>) produtoRepo.findAll();
		model.addAttribute("totalProdListSize", prodList.size());

		prodList = new ArrayList<Produto>();

		for(DetalhesEncomenda de : encomenda.getCarrinho()) {
			Produto prod = produtoRepo.findById(de.getProdutoId()).orElse(null);
			if(prod != null){
				model.addAttribute(String.format("%d", prod.getId()), de.getQuantidade());
				prodList.add(prod);
			}
		}		       	

		model.addAttribute("details", encomenda.getCarrinho());
		model.addAttribute("prodList", prodList);
		model.addAttribute("prodListSize", prodList.size());

		return "list-order";
	}

	@PostMapping("/add-to-order")
	public String addToOrder( 
			@RequestParam(name="quant", required=true) Integer quant,
			@RequestParam(name="p_id", required=true) Long p_id,
			Model model
			)	
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Utilizador utilizador = utilizadorRepo.findOneByUsername(authentication.getName());

		Produto prod = produtoRepo.findById(p_id).orElse(null);

		Encomenda encomenda = encomendasRepo.findByUtilizadorAndIsActive(utilizador, true);
		
		if(encomenda == null){
			encomendasRepo.save(new Encomenda(utilizador));
			encomenda = encomendasRepo.findByUtilizadorAndIsActive(utilizador, true);
		}

		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", authentication.getName());
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(ga.toString().equals("ADMIN"))
					model.addAttribute("auth", ga.toString());
			}
		}

		List<DetalhesEncomenda> details = encomenda.getCarrinho();

		DetalhesEncomenda det = new DetalhesEncomenda(encomenda, prod.getId(), quant);

		det = encomenda.addToCart(det);

		detailsRepo.save(det);
		encomendasRepo.save(encomenda);

		List<Produto> prodList = (List<Produto>) produtoRepo.findAll();
		model.addAttribute("totalProdListSize", prodList.size());

		prodList = new ArrayList<Produto>();

		for(DetalhesEncomenda de : encomenda.getCarrinho()) {
			prod = produtoRepo.findById(de.getProdutoId()).orElse(null);
			if(prod != null){
				model.addAttribute(String.format("%d", prod.getId()), de.getQuantidade());
				prodList.add(prod);
			}
		}		       	
		model.addAttribute("details", encomenda.getCarrinho());
		model.addAttribute("prodList", prodList);
		model.addAttribute("prodListSize", prodList.size());

		return "list-order";
	}

	@PostMapping("/remove-from-order")
	public String removeFromOrder(
			@RequestParam(name = "p_id") Long p_id,
			@RequestParam(name = "quant") Integer quant,
			Model model)
	{

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Utilizador utilizador = utilizadorRepo.findOneByUsername(authentication.getName());

		Produto prod = produtoRepo.findById(p_id).orElse(null);

		Encomenda encomenda = encomendasRepo.findByUtilizadorAndIsActive(utilizador, true);
		
		if(encomenda == null){
			encomendasRepo.save(new Encomenda(utilizador));
			encomenda = encomendasRepo.findByUtilizadorAndIsActive(utilizador, true);
		}

		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			model.addAttribute("user", authentication.getName());
			for(GrantedAuthority ga : authentication.getAuthorities()){
				if(ga.toString().equals("ADMIN"))
					model.addAttribute("auth", ga.toString());
			}
		}

		DetalhesEncomenda det = new DetalhesEncomenda(encomenda, prod.getId(), quant);
		det = encomenda.removeFromCart(det);
		
		if(det.getQuantidade() < 1)
			detailsRepo.delete(det);
		else
			detailsRepo.save(det);

		encomendasRepo.save(encomenda);


		List<Produto> prodList = (List<Produto>) produtoRepo.findAll();
		model.addAttribute("totalProdListSize", prodList.size());

		prodList = new ArrayList<Produto>();

		for(DetalhesEncomenda de : encomenda.getCarrinho()) {
			prod = produtoRepo.findById(de.getProdutoId()).orElse(null);
			if(prod != null){
				model.addAttribute(String.format("%d", prod.getId()), de.getQuantidade());
				prodList.add(prod);
			}
		}		       	
		model.addAttribute("details", encomenda.getCarrinho());
		model.addAttribute("prodList", prodList);
		model.addAttribute("prodListSize", prodList.size());

		return "list-order";
		
	}
}
