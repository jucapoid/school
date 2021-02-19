package com.xhamster.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Column;

@Entity
public class Produto {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long p_id;

	@Column(columnDefinition="TEXT")
	private String descr; 

	private String nome, image, categoria;

	private Double preco;

	protected Produto() {}

	public Produto(String image, String descr, String nome, String categoria, Double preco) {
		this.image = image;
		this.descr = descr;
		this.nome = nome;
		this.preco = preco;
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return String.format(
				"Produto[p_id=%d, nome='%s', image='%s', descr='%s', preco=%f, categoria='%s']",
				p_id, nome, image, descr, preco, categoria);
	}

	public Long getId(){ return p_id; }

	public String getNome(){ return nome; }

	public String getImage(){ return image; }

	public String getDescr(){ return descr; }

	public Double getPreco(){ return preco; }

	public String getCategoria(){ return categoria; }

}
