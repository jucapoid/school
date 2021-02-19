package com.xhamster.repos;

import com.xhamster.models.DetalhesEncomenda;
import com.xhamster.models.Encomenda;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface DetalhesEncomendaRepo extends CrudRepository<DetalhesEncomenda, Long> {

	DetalhesEncomenda findById(long id);

	DetalhesEncomenda findByEncomendaAndProdutoId(Encomenda encomenda, Long produto);
}
