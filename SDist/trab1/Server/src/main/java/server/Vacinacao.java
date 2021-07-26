package server;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Vacinacao {
    private @Id @GeneratedValue Long id;

    private String nome, tipo;
    private int idade;
    private Date dataAdministracao;
    private boolean efeitos, administrada;
    char genero;


    @Column(name = "centroId")
    private Long centroId;

    public Vacinacao() {}

    public Vacinacao(String nome, int idade, Date dataPreferida, Long centro) {
        this.nome = nome;
        this.idade = idade;
        this.efeitos = false;
        this.centroId = centro;
        this.administrada = false;
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

    public Long getCentroId() {
        return centroId;
    }

    public void setCentroId(Long centro_id) {
        this.centroId = centro_id;
    }

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "\nId: " + id + "\nNome: " + nome + "\nAdministrada: " + administrada +"\nCentro: " + centroId;
    }


}
