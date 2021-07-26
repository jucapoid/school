package dgs.repos;

import dgs.models.Centro;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CentroRepo extends CrudRepository<Centro, Long> {

    Centro findById(long id);

    List<Centro> findAll();

}
