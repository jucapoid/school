package cidadao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Centro {
    private Long id;
    private String cidade;
    private int capacidade;


    public Centro() {}

    public Centro(Long id, String cidade, int capacidade) {
        this.id = id;
        this.cidade = cidade;
        this.capacidade = capacidade;
    }

    public Long getId() {
        return id;
    }

    public String getCidade() {
        return cidade;
    }

    public int getCapacidade() {
        return capacidade;
    }

    @Override
    public String toString() {
        return "Id: " + id + "\nCidade: " + cidade + "\nCapacidade: " + capacidade;
    }
}
