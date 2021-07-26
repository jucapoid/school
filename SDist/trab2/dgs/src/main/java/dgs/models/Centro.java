package dgs.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Centro {

    private @Id @GeneratedValue Long id;

    private String cidade;

    private int capacidade;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = Vacinacao.class)
    @JoinColumn(name = "centroId")
    private List<Vacinacao> listaMarcacoes;

    @OneToMany(targetEntity = VaxPorDia.class)
    @JoinColumn(name = "centroId")
    private List<VaxPorDia> vaxPorDia;

    public Centro() {}

    public Centro(int capacidade, String cidade) {
        this.capacidade = capacidade;
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

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public List<Vacinacao> getListaMarcacoes() {
        return listaMarcacoes;
    }

    public void setListaMarcacoes(List<Vacinacao> listaEspera) {
        this.listaMarcacoes = listaEspera;
    }

    public List<VaxPorDia> getVaxPorDia() {
        return vaxPorDia;
    }

    public void setVaxPorDia(List<VaxPorDia> vaxPorDia) {
        this.vaxPorDia = vaxPorDia;
    }

    @Override
    public String toString() {
        return "\nId: " + id + "\nCidade: " + cidade + "\nCapacidade: " + capacidade;
    }
}
