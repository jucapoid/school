package dgs.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Vacinacao {

    private @Id @GeneratedValue Long id;

    private String nome, email, tipo;
    private int idade;
    private Date dataPreferida ,dataAdministracao;
    private boolean efeitos, administrada, confirmada;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = Notificacao.class)
    @JoinColumn(name = "vacinacaoId")
    List<Notificacao> notificacaoList;

    @Column(name = "centroId")
    private Long centroId;

    public Vacinacao() {}

    public Vacinacao(String nome, String email, int idade, Date dataPreferida, Long centro) {
        this.nome = nome;
        this.email = email;
        this.idade = idade;
        this.dataPreferida = dataPreferida;
        this.efeitos = false;
        this.centroId = centro;
        this.administrada = false;
        this.confirmada = false;
        notificacaoList = new ArrayList<>();
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

    public void setCentroId(Long centro_id) {
        this.centroId = centro_id;
    }

    public void addNotificacao(Notificacao notificacao) {
        notificacaoList.add(notificacao);
    }

    @Override
    public String toString() {
        return "\nId: " + id + "\nNome: " + nome + "\nEmail: " + email + "\nData agendamento: " + dataPreferida + "\nConfirmada: " + confirmada +"\nCentro: " + centroId;
    }

}
