import java.io.File;  
import java.io.FileInputStream;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
import java.net.Socket;  
import java.util.Arrays;  
import java.lang.*;  
import java.util.Scanner;  
  
  
public class Client {  
    public static void main(String[] args) throws Exception {  
        String fileName = null;  
  
       try {  
            fileName = args[0];  
        } catch (Exception e) {  
        System.out.println("Enter the Request :");  
        Scanner scanner = new Scanner(System.in);  
        String req = scanner.nextLine();  
           
        Socket socket = new Socket("localhost", 8009);  
       // ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());  
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());  
  
        oos.writeObject(req);  
        oos.close();  
        System.exit(0);      
}  
  
}  
  
}   
//DELETE /coverletter.txt HTTP/1.1