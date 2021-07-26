package dgs.repos;

import dgs.models.Vacinacao;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface VacinacaoRepo extends CrudRepository<Vacinacao, Long> {

    Vacinacao findById(long id);

    List<Vacinacao> findByDataAdministracaoAndTipo(Date date, String tipo);

    List<Vacinacao> findByDataPreferidaAndAdministradaOrderByIdade(Date date, boolean administrada);

    List<Vacinacao> findAll();
}
