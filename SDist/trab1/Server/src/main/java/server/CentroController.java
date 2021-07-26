package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CentroController {

    @Autowired
    private final CentroRepo centroRepo;

    CentroController(CentroRepo centroRepo) { this.centroRepo = centroRepo; }

    @GetMapping("/centros")
    ResponseEntity<List<Centro>> all() {
        List<Centro> centros = centroRepo.findAll();

        return new ResponseEntity<List<Centro>>(centros, HttpStatus.OK);
    }

    @GetMapping("/centros/{id}")
    ResponseEntity<Centro> one(@PathVariable("id") Long id) {
        Centro centro = centroRepo.findById(id).orElse(null);

        return new ResponseEntity<Centro>(centro,HttpStatus.OK);
    }

    @PostMapping("/centros")
    Centro newCentro( @RequestBody Centro centro) {
        return centroRepo.save(centro);
    }

    @PutMapping("/centros/{id}")
    Centro replaceCentro(@RequestBody Centro newCentro, @PathVariable Long id) {
        return centroRepo.findById(id)
                .map(centro -> {
                    centro.setListaMarcacoes(newCentro.getListaMarcacoes());
                    return centroRepo.save(centro);
                })
                .orElseGet(() -> {
                    newCentro.setId(id);
                    return centroRepo.save(newCentro);
                });
    }

}
