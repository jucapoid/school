package server;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Centro {
    private @Id @GeneratedValue Long id;

    private String cidade;


    @OneToMany(cascade = CascadeType.ALL, targetEntity = Vacinacao.class)
    @JoinColumn(name = "centroId")
    private List<Vacinacao> listaMarcacoes;

    public Centro() {}

    public Centro(String cidade) {
        this.cidade = cidade;
        this.listaMarcacoes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public List<Vacinacao> getListaMarcacoes() {
        return listaMarcacoes;
    }

    public int countFila() {
        int n = 0;
        for (Vacinacao v: listaMarcacoes) {
            if (!v.isAdministrada())
                n++;
        }
        return n;
    }

    public void setListaMarcacoes(List<Vacinacao> listaEspera) {
        this.listaMarcacoes = listaEspera;
    }

    @Override
    public String toString() {
        return "\nId: " + id + "\nCidade: " + cidade;
    }
}
