package dgs.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class VaxPorDia {

    private @Id @GeneratedValue Long id;

    @Column(name = "centroId")
    private Long centroId;

    private Date data;

    int numVacinas;

    VaxPorDia() {}

    public VaxPorDia(Long centro_id, Date data, int numVacinas) {
        this.centroId = centro_id;
        this.data = data;
        this.numVacinas = numVacinas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCentroId() {
        return centroId;
    }

    public void setCentroId(Long centro_id) {
        this.centroId = centro_id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getNumVacinas() {
        return numVacinas;
    }

    public void setNumVacinas(int numVacinas) {
        this.numVacinas = numVacinas;
    }

    public void incrementNumVacinas() {
        this.numVacinas++;
    }
}
