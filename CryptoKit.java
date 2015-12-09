//Netbeans users can uncomment this if they create a New Project named CryptoKit package cryptokit;
/*
 * CryptoKit v1.0 
 * Last Updated: 12/08/2015 17:53PST
 * Description: CryptoKit is a java utility file with common cryptography methods and protocols. 
 * Limitations: - Some methods only work with regex [^\w](alpha-numeric) values.
 *              - Keys must be generated by some other means (for now).
 * 
 * Happy encrypting!
 * @author ahmadsoomro.com
 */


import java.io.File;
import java.io.*;
import java.net.*;
import java.net.ServerSocket;
import java.util.Random;


public class CryptoKit {
    
    //this method transposes each character in input message in a certain direction and by a certain number of moves
    //can be used to encrypt and decrypt, depending on knowledge of direction and number of moves
    public static String transposeString(String text, int direction, int moves){
        String result = "";
        char[] array = text.toCharArray();
            if(direction == -1 || direction == 1){
                for(char c : array){
                    int temp= (int) c;
                        if(direction == 1){
                        temp += moves;}else{
                        temp -= moves;}
                        result = result + (char)temp;                       
                }
            }else{throw new IndexOutOfBoundsException("Direction can only be"
                    + "1 or -1");}
            
       return result; 
    }
    
    //this is method that takes the message and performs some substitution method on it 
    //takes individual characters, if they are present in key1 array, then then are replaced in the
    //resulting string by the character in the same index location, in key2
    public static String substitutionString(String text, char[] key1, char[] key2){
    String result = text;
    StringBuilder strBld = null;
    for(int i = 0; i < text.length(); i++){
        for(int j = 0; j < key1.length; j++){
            if(result.charAt(i) == key1[j]){
                strBld = new StringBuilder(result);
                strBld.setCharAt(i, key2[j]);
                result= strBld.toString();
            }
        }   
    }
    
    return result;
    }
    
    //this method takes a message, srtips punctuation, and uses two keys to translate words
    public static String translateString(String text, String[] key1, String[] key2){
    String result = "";
    String[] array = text.split("\\W+");
    
    
    for(int j = 0 ; j < array.length; j++){
        for(int i = 0; i<key1.length; i++){
            if(array[j].equals(key1[i])){
                array[j] = key2[i];
            }
            
        }result += array[j] + " ";
    }
    return result;   
    }  

    //a much better way to obtain char for int than type conversion, catches IndexOutOfBoundExc
    public static char asciiReader(int asciiVal){
        if(asciiVal >=0 && asciiVal <= 127){
            char c = (char) asciiVal;
            return c;
        }else{throw new IndexOutOfBoundsException("You did not enter a correct ASCII value");}
        
    }
    
    //An easier way to create a file
    public static File newFile(String filename){
        if(filename.toLowerCase().contains(".txt") == false){
             throw new IllegalArgumentException("Please include file extension. "
                     + "\nNote: This package only supports ");
        }else{
            File file = new File(filename);
            return file;
        }  
    }
    
    //an overload that takes a directory
    public static File newFile(String filename, String dir){
        if(filename.toLowerCase().contains(".txt") == false){
             throw new IllegalArgumentException("Please include file extension. "
                     + "\nNote: This package only supports .txt");
        }else if(dir.toLowerCase().contains(":") == false){
            throw new IllegalArgumentException("Please check your directory parameter."
                    + "\n Example: c://ahmad//loc//"
                     + "\nNote: This package only supports .txt");
        }else{
            File file = new File(filename, dir);
            return file;
        }  
    }
    
    //prints the inverse of the message (the message in reverse)
    public static String inverse(String text){
        String[] array = text.split("\\W+");
        String[] holder = new String [array.length];
        String result = "";
        for(int i=0;i<array.length;i++){
           holder[i] = array[array.length - i - 1];
           result += holder[i] + " ";
        }
        return result;
    }
    
    //This method places a number of random characters inbetween char values in a text
    public static String cypherStick(String text, int n){
        String result = "";
        char[] array = text.toCharArray();
        Random r = new Random();
        String rands = ""; 
        String alphabet = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()-_+=`";
        for(int j = 0; j < array.length; j++){
            result += array[j];
            for (int i = 0; i < n; i++) {
                rands += alphabet.charAt(r.nextInt(alphabet.length()));
                result+=rands;
                rands="";
            } 
        }
        return result;
    }
    
    public static String decypherStick(String text, int n){
    String result = "";
    char[] array = text.toCharArray();
    for(int i = 0; i<text.length(); i++){
        if(i==0){
        result+= array[i];
        }
        
        if((i==result.length()+result.length()*n)){
        result+=array[i];   
        }    
    }
    return result;
    }
    
    
    //I got a lot of help from https://systembash.com/a-simple-java-tcp-server-and-tcp-client/
    //for this one. :)
    
    //get a message by connecting to a client machine (TCP)
    //run this method if you want to receive a message
  

    //Investigating further into this.... --AS
    public static void getMessage() throws Exception{
        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(6789);
        
        while(true){
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = 
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println("Received: " + clientSentence);
            capitalizedSentence = clientSentence.toUpperCase() + '\n';
            outToClient.writeBytes(capitalizedSentence);        
        }
    }
    
    //run this method if you want to send a message
    public static void sendMessage(String text) throws Exception{
        try{
            String sentence;
            String modifiedSentence;
            BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
            try (Socket clientSocket = new Socket("localhost", 6789)) {
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                sentence = text;
                outToServer.writeBytes(sentence + '\n');
                modifiedSentence = inFromServer.readLine();
                System.out.println("FROM SERVER: " + modifiedSentence);
            }
        }catch(IOException e){e.getMessage();}
    }
    
}
    
    

    
    