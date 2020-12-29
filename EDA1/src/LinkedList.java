import java.io.FileNotFoundException;
import java.util.Scanner;

class LinkedList {
    public SingleNode firstSingleNode = null;

    public int size(){
        SingleNode current = firstSingleNode;
        int c = 0;
        while (current != null) {
            c++;
            current = current.next;
        }
        return c;
    }

    public void insert(SingleNode newSingleNode, int hashKey) {
        SingleNode previous = null;
        SingleNode current = firstSingleNode;
        newSingleNode.key = hashKey;
        while (current != null && newSingleNode.key > current.key) {
            previous = current;
            current = current.next;
        }
        if (previous == null)
            firstSingleNode = newSingleNode;
        else
            previous.next = newSingleNode;
        newSingleNode.next = current;
    }

    public void displayWordList() {
        SingleNode current = firstSingleNode;
        while (current != null) {
            System.out.println(current);
            current = current.next;
        }
    }

    public SingleNode find(int hashKey, String wordToFind) {

        SingleNode current = firstSingleNode;

        while (current != null && current.key <= hashKey) {
            if (current.theWord.equals(wordToFind))
                return current;
            current = current.next;
        }
        return null;
    }

}