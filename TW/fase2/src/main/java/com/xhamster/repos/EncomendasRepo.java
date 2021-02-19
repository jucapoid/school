package com.xhamster.repos;

import com.xhamster.models.Encomenda;
import com.xhamster.models.Utilizador;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface EncomendasRepo extends CrudRepository<Encomenda, Long> {

	Encomenda findById(long id);

	Encomenda findByUtilizadorAndIsActive(Utilizador uti, boolean bol);
}
