package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VacinacaoController {

    @Autowired
    private final VacinacaoRepo vacinacaoRepo;

    VacinacaoController( VacinacaoRepo vacinacaoRepo ) {
        this.vacinacaoRepo = vacinacaoRepo;
    }

    @GetMapping("/vacinacoes")
    ResponseEntity<List<Vacinacao>> all() {
        List<Vacinacao> vacinacaoList = vacinacaoRepo.findAll();

        return new ResponseEntity<List<Vacinacao>>(vacinacaoList, HttpStatus.OK);
    }

    @GetMapping("/vacinacoes/{id}")
    ResponseEntity<Vacinacao> one(@PathVariable("id") Long id) {
        Vacinacao vacinacao = vacinacaoRepo.findById(id).orElse(null);

        return new ResponseEntity<Vacinacao>(vacinacao,HttpStatus.OK);
    }

    @PostMapping("/vacinacoes")
    Vacinacao newVacinacao( @RequestBody Vacinacao newVax){
        return vacinacaoRepo.save(newVax);
    }

    @PutMapping("/vacinacoes/{id}")
    Vacinacao replaceVacinacao(@RequestBody Vacinacao newVax, @PathVariable Long id) {
        return vacinacaoRepo.findById(id)
                .map(vacinacao -> {
                    vacinacao.setAdministrada(newVax.isAdministrada());
                    vacinacao.setEfeitos(newVax.isEfeitos());
                    vacinacao.setTipo(newVax.getTipo());
                    vacinacao.setDataAdministracao(newVax.getDataAdministracao());
                    return vacinacaoRepo.save(vacinacao);
                })
                .orElseGet(() -> {
                    newVax.setId(id);
                    return vacinacaoRepo.save(newVax);
                });
    }
}