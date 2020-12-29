import java.util.Objects;

public class Position {
    int linha, coluna;
    char letra;
    boolean visited;

    public Position() {
    }

    public Position(int linha, int coluna, char letra) {
        this.linha = linha;
        this.coluna = coluna;
        this.letra = letra;
        this.visited = false;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return letra + "(" + linha + "," + coluna + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return linha == position.linha &&
                coluna == position.coluna;
    }

}
