package com.xhamster.repos;

import com.xhamster.models.Produto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProdutoRepo extends CrudRepository<Produto, Long> {

	Produto findById(long id);

	List<Produto> findByPrecoBetween(Double min, Double max);

	List<Produto> findByCategoriaAndPrecoBetween(String categoria, Double min, Double max);

	List<Produto> findByDescrContainingAndPrecoBetween(String descr, Double min, Double max);

	List<Produto> findByCategoriaAndDescrContainingAndPrecoBetween(
			String categoria,
			String descr,
			Double precomin,
			Double precomax
			);

	List<Produto> findByDescrContainingOrNomeContaining(String descr, String nome);

}
