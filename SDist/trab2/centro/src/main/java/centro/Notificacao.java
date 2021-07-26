package centro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notificacao {
    private Long id;
    private Long vacinacaoId;
    private String titulo, descricao;
    private boolean lida;

    public Notificacao() {}

    public Notificacao(String titulo, String descricao, Long vacinacao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.vacinacaoId = vacinacao;
        this.lida = false;
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

    public void setVacinacao(Long vacinacao) {
        this.vacinacaoId = vacinacao;
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
}
