import java.io.*;
import java.io.IOException;


public class HashFunction3 {

    LinkedList[] theArray;
    int arraySize;
    int itemsInArray = 0;

    public int stringHashFunction(String wordToHash) {
        int hashKeyValue = 0;

        for (int i = 0; i < wordToHash.length(); i++) {
            int charCode = wordToHash.charAt(i) - 96;
            int hKVTemp = hashKeyValue;
            hashKeyValue = (hashKeyValue * 27 + charCode) % arraySize;
        }
        return hashKeyValue;
    }

    HashFunction3(int size) throws FileNotFoundException {
        arraySize = size;
        theArray = new LinkedList[size];
        for (int i = 0; i < arraySize; i++) {
            theArray[i] = new LinkedList();
        }
        BufferedReader br;
        try{
            br = new BufferedReader(new FileReader("Resources/words.txt"));
            String line = br.readLine();
            while(line != null){
                SingleNode palavra = new SingleNode(line);
                insert(palavra);
                line = br.readLine();
            }
            br.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void insert(SingleNode newSingleNode) {
        String wordToHash = newSingleNode.theWord;
        int hashKey = stringHashFunction(wordToHash);
        theArray[hashKey].insert(newSingleNode, hashKey);
    }

    public SingleNode find(String wordToFind) {
        int hashKey = stringHashFunction(wordToFind);
        SingleNode theSingleNode = theArray[hashKey].find(hashKey, wordToFind);
        return theSingleNode;
    }

    public void displayTheArray() {
        for (int i = 0; i < arraySize; i++) {
            System.out.println("theArray Index " + i);
            theArray[i].displayWordList();
        }
    }
}

