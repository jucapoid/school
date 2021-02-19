package com.xhamster.repos;

import com.xhamster.models.Utilizador;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UtilizadorRepo extends CrudRepository<Utilizador, Long> {

	Utilizador findById(long id);
	
	Utilizador findOneByUsername(String username);

}
