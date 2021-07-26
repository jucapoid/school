package dgs.controllers;

import dgs.models.Notificacao;
import dgs.repos.NotificacaoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotificacaoController {

    @Autowired
    private final NotificacaoRepo notificacaoRepo;

    NotificacaoController(NotificacaoRepo notificacaoRepo) {
        this.notificacaoRepo = notificacaoRepo;
    }

    @PostMapping("/notificacoes")
    void newNotificacao(@RequestBody Notificacao notificacao) {
        notificacaoRepo.save(notificacao);
    }

    @GetMapping("/notificacoes/{id}")
    ResponseEntity<List<Notificacao>> getNotificacoes(@PathVariable Long id) {
        List<Notificacao> n = notificacaoRepo.findByVacinacaoId(id);
        return new ResponseEntity<List<Notificacao>>(n,HttpStatus.OK);
    }

    @PutMapping("/notificacoes/{id}")
    Notificacao replaceNotificacao(@RequestBody Notificacao newNot, @PathVariable Long id) {
        return notificacaoRepo.findById(id)
                .map(notificacao -> {
                    notificacao.setLida(newNot.isLida());
                    return notificacaoRepo.save(notificacao);
                })
                .orElseGet(() -> {
                    newNot.setId(id);
                    return notificacaoRepo.save(newNot);
                });
    }
}
