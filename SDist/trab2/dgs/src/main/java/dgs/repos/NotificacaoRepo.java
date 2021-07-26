package dgs.repos;

import dgs.models.Notificacao;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificacaoRepo extends CrudRepository<Notificacao, Long> {

    Notificacao findById(int id);

    List<Notificacao> findByVacinacaoId(Long vacinacao);
}
