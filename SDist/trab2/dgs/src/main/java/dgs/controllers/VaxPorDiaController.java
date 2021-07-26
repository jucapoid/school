package dgs.controllers;

import dgs.models.VaxPorDia;
import dgs.repos.VaxPorDiaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class VaxPorDiaController {

    @Autowired
    private final VaxPorDiaRepo vaxPorDiaRepo;

    VaxPorDiaController(VaxPorDiaRepo vaxPorDiaRepo) {
        this.vaxPorDiaRepo = vaxPorDiaRepo;
    }

    @GetMapping("/vaxpordia/{date}/{centroId}")
    Integer numeroVacinas(
            @PathVariable("date") String date,
            @PathVariable("centroId") String centroId) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date data = simpleDateFormat.parse(date);
        VaxPorDia v = vaxPorDiaRepo.findByCentroIdAndData(Long.valueOf(centroId), data);
        return v.getNumVacinas();
    }

    @GetMapping("/vaxpordia")
    List<VaxPorDia> all() {
        return vaxPorDiaRepo.findAll();
    }

}
