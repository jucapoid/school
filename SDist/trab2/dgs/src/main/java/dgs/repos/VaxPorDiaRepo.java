package dgs.repos;

import dgs.models.VaxPorDia;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface VaxPorDiaRepo extends CrudRepository<VaxPorDia, Long> {

    VaxPorDia findByCentroIdAndData(Long centroId, Date date);

    VaxPorDia findByData(Date date);

    List<VaxPorDia> findAll();

}
