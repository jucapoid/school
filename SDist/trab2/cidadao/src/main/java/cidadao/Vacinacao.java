package cidadao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vacinacao {

    private Long id;

    private String nome, email, tipo;
    private int idade;
    private Date dataPreferida, dataAdministracao;
    private boolean efeitos, administrada, confirmada;

    private List<Notificacao> notificacaoList;

    private Long centroId;

    public Vacinacao() {}

    public Vacinacao(String nome, String email, int idade, Date dataPreferida, Long centro) {
        this.nome = nome;
        this.email = email;
        this.idade = idade;
        this.efeitos = false;
        this.dataPreferida = dataPreferida;
        this.centroId = centro;
        this.administrada = false;
        this.confirmada = false;
        this.notificacaoList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Date getDataPreferida() {
        return dataPreferida;
    }

    public void setDataPreferida(Date dataPreferida) {
        this.dataPreferida = dataPreferida;
    }

    public Date getDataAdministracao() {
        return dataAdministracao;
    }

    public void setDataAdministracao(Date dataAdministracao) {
        this.dataAdministracao = dataAdministracao;
    }

    public boolean isEfeitos() {
        return efeitos;
    }

    public void setEfeitos(boolean efeitos) {
        this.efeitos = efeitos;
    }

    public boolean isAdministrada() {
        return administrada;
    }

    public void setAdministrada(boolean administrada) {
        this.administrada = administrada;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public void setConfirmada(boolean confirmada) {
        this.confirmada = confirmada;
    }

    public List<Notificacao> getNotificacaoList() {
        return notificacaoList;
    }

    public void setNotificacaoList(List<Notificacao> notificacaoList) {
        this.notificacaoList = notificacaoList;
    }

    public Long getCentroId() {
        return centroId;
    }

    public void setCentroId(Long centroId) {
        this.centroId = centroId;
    }

    public boolean isUnread() {
        for (Notificacao n: notificacaoList) {
            if (!n.isLida())
                return true;
        }
        return false;
    }

    public List<Notificacao> unread() {
        List<Notificacao> notificacaoList = new ArrayList<>();
        for (Notificacao n: notificacaoList) {
            if (!n.isLida())
                notificacaoList.add(n);
        }
        return notificacaoList;
    }

    @Override
    public String toString() {
        return "Id: " + id
                + "\nNome: " + nome
                + "\nEmail: " + email
                + "\nIdade: " + idade
                + "\nData: " + dataPreferida
                + "\nId centro: " + centroId;
    }
}
