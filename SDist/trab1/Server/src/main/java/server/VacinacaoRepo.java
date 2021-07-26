package server;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VacinacaoRepo extends CrudRepository<Vacinacao, Long> {

    Vacinacao findById(long id);

    List<Vacinacao> findAll();

}
