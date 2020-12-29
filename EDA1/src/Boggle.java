import java.io.FileNotFoundException;
import java.util.function.DoubleToIntFunction;

public class Boggle extends LinkedList{


    Matriz_jogo matriz = new Matriz_jogo();
    HashFunction3 wordHashTable = new HashFunction3(51190);

    public Boggle() throws FileNotFoundException {
    }

    public LinkedList solve() {
        LinkedList found = new LinkedList();
        for(int i  = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                Search(matriz, matriz.pos[i][j], "",found, "");
            }
        }
        System.out.println(found.size());
        return found;
    }

    public void Search(Matriz_jogo m, Position p, String word, LinkedList found, String output) {

        p.setVisited(true);
        output += "(" + p.letra + ":(" + p.linha + "," + p.coluna + "))->";
        word += p.letra;
        SingleNode temp = new SingleNode(word);

        if (found.find(wordHashTable.stringHashFunction(word),word)==null && wordHashTable.find(word)!=null) {
            output = output.substring(0, output.length()-2);
            found.insert(temp,wordHashTable.stringHashFunction(word));
            System.out.println(word + " " + output);
        }


        for (int r = p.linha-1; r<=p.linha+1 && r<4; r++) {
            for (int c = p.coluna - 1; c <= p.coluna + 1 && c < 4; c++) {
                if (r >= 0 && c >= 0 && !m.pos[r][c].visited) {
                    Search(m, m.pos[r][c], word, found, output);
                }
            }
        }
        word = "" + word.charAt(word.length() - 1);
        p.setVisited(false);
    }
        
}