import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Matriz_jogo extends Position {

    Position[][] pos = new Position[4][4];

    public Matriz_jogo() throws FileNotFoundException {
        BufferedReader buf;
        try {
            buf = new BufferedReader(new FileReader("Resources/boggle5.txt"));
            int i = 0, j = 0;
            char temp = (char) buf.read();
            for(j = 0; j < 4; j++){
                if(i>3){
                    break;
                }
                pos[i][j] = new Position(i,j,Character.toLowerCase(temp));
                temp = (char) buf.read();
                if(j>=3){
                    j=-1;
                    i++;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        /*
        Random rnd = new Random();
        char temp;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                temp = (char) (rnd.nextInt(26)+ 'a');
                pos[i][j] = new Position(i,j,temp);
            }
        }*/
    }

    @Override
    public String toString() {
        String prnt = "";
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                prnt += pos[i][j] + "\t";
            }
            prnt += "\n";
        }
        return prnt;
    }
}
