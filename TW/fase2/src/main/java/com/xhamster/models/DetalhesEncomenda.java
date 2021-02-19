package com.xhamster.models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.FetchType;
import com.xhamster.repos.ProdutoRepo;

@Entity
public class DetalhesEncomenda{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long d_id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Encomenda encomenda;

	@Column(name = "ProdutoId")
	private Long produtoId;

	private Integer quantidade;

	protected DetalhesEncomenda() {}

	public DetalhesEncomenda(Encomenda encomenda, Long produtoId, Integer quantidade) {
		this.encomenda = encomenda;
		this.produtoId = produtoId;
		this.quantidade = quantidade;
	}

	@Override
	public String toString() {
		return String.format(
				"DetalhesEncomenda[d_id=%d, encomenda_id=%d, produto_id=%d, quantidade=%d]",
				d_id, encomenda.getId(), produtoId, quantidade);
	}

	public Long getId() { return d_id; }

	public Encomenda getEncomenda() { return encomenda; }

	public Long getProdutoId() { return produtoId; }

	public Integer getQuantidade() { return quantidade; }

	public Integer addQuant(Integer quant) { 
		this.quantidade += quant;
		return this.quantidade;
       	}

	public Integer removeQuant(Integer quant) { 
		this.quantidade -= quant;
		return this.quantidade;
       	}

}
