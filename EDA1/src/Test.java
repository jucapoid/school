import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test extends Matriz_jogo{
    public Test() throws FileNotFoundException {
    }

    public static void main(String [ ] args) throws FileNotFoundException  {
        Boggle boggle = new Boggle();
        System.out.println(boggle.matriz.toString());
        boggle.solve();
    }

}
