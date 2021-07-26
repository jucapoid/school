package cidadao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notificacao {
    private Long id;

    private Long vacinacaoId;

    private String titulo, descricao;
    private boolean lida;
    private List<Notificacao> notificacaoList;

    public Notificacao() {
    }

    public Notificacao(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.lida = false;
        this.notificacaoList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVacinacaoId() {
        return vacinacaoId;
    }

    public void setVacinacaoId(Long id) {
        this.vacinacaoId = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public List<Notificacao> getNotificacaoList() {
        return notificacaoList;
    }

    public void setNotificacaoList(List<Notificacao> notificacaoList) {
        this.notificacaoList = notificacaoList;
    }

    @Override
    public String toString() {
        return "\nAssunto: " + titulo + "\nDescricao: " + descricao + "\n";
    }
}
