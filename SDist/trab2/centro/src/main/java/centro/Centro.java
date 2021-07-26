package centro;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Centro {

    private Long id;

    private String cidade;

    private int capacidade;

    private List<Vacinacao> listaMarcacoes;

    public Centro() {}

    public Centro(int capacidade, String cidade) {
        this.cidade = cidade;
        this.capacidade = capacidade;
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

    public List<Vacinacao> getListaMarcacoesDiaOrderIdade(Date data) {
        List<Vacinacao> list = new ArrayList<>();
        for (Vacinacao v: listaMarcacoes) {
            if (v.getDataPreferida().equals(data)){
                list.add(v);
            }
        }
        list.sort(new SortByIdade());
        return list;
    }
}

class SortByIdade implements Comparator<Vacinacao> {
    public int compare(Vacinacao a, Vacinacao b) {
        return b.getIdade()-a.getIdade();
    }
}
