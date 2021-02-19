package com.xhamster.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;

@Entity
public class Encomenda{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long e_id;

	@ManyToOne
	@JoinColumn(name="utilizador")
	private Utilizador utilizador;

	@OneToMany(mappedBy = "encomenda")
	private List<DetalhesEncomenda> carrinho;

	private boolean isActive;

	protected Encomenda() {}

	public Encomenda(Utilizador utilizador) {
		this.utilizador = utilizador;
		this.isActive = true;
		this.carrinho = new ArrayList<DetalhesEncomenda>();
	}

	@Override
	public String toString() {
		return String.format(
				"Encomenda[e_id=%d, utilizador_id=%d]",
				e_id, utilizador.getId());
	}

	public Long getId() { return e_id; }

	public Utilizador getUtilizador() { return utilizador; }

	public List<DetalhesEncomenda> getCarrinho() { return carrinho; }

	public DetalhesEncomenda addToCart(DetalhesEncomenda details) {
		for(DetalhesEncomenda d: this.carrinho){
			if(d.getProdutoId() == details.getProdutoId()) {
				d.addQuant(details.getQuantidade());
				return d;
			}
		}
		this.carrinho.add(details);
		return details;
	}

	public DetalhesEncomenda removeFromCart(DetalhesEncomenda details){
		for(DetalhesEncomenda d: this.carrinho){
			if(d.getProdutoId() == details.getProdutoId()) {
				d.removeQuant(details.getQuantidade());
				if(d.getQuantidade() < 1)
					this.carrinho.remove(d);
				return d;
			}
		}
		return null;
	}

	public boolean getIsActive(){ return isActive; }

	public void setIsActive(boolean bol) {
		this.isActive = bol;
	}
}
