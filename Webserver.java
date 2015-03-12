
import java.util.*;
import java.io.*;
import java.net.*;

public class Webserver {
  private static ServerSocket serverSocket;

  public static void main(String[] args) throws IOException {
    serverSocket=new ServerSocket(8086);  // Start, listen on port 80
    while (true) {
      try {
        Socket s=serverSocket.accept();  // Wait for a client to connect
        new ClientHandler(s);  // Handle the client in a separate thread
      }
      catch (Exception x) {
        System.out.println(x);
      }
    }
  }
}

class ClientHandler extends Thread {
  private Socket socket;  
  public ClientHandler(Socket s) {
    socket=s;
    start();
  }

  public void run() {
    try {

      BufferedReader in=new BufferedReader(new InputStreamReader(
        socket.getInputStream()));
      PrintStream out=new PrintStream(new BufferedOutputStream(
        socket.getOutputStream()));

      String s=in.readLine();
      System.out.println(s);  // Log the request

      String filename="";
      StringTokenizer st=new StringTokenizer(s);
      try {

        if (st.hasMoreElements() && st.nextToken().equalsIgnoreCase("GET") && st.hasMoreElements())
          filename=st.nextToken();
        else
          throw new FileNotFoundException(); 

        // Append trailing "/" with "index.html"
        if (filename.endsWith("/"))
          filename+="index.html";

        // Remove leading / from filename
        while (filename.indexOf("/")==0)
          filename=filename.substring(1);

        // Replace "/" with "\" in path for PC-based servers
        filename=filename.replace('/', File.separator.charAt(0));

        // Check for illegal characters to prevent access to superdirectories
        if (filename.indexOf("..")>=0 || filename.indexOf(':')>=0
            || filename.indexOf('|')>=0)
          throw new FileNotFoundException();

        if (new File(filename).isDirectory()) {
          filename=filename.replace('\\', '/');
          out.print("HTTP/1.0 301 Moved Permanently\r\n"+
            "Location: /"+filename+"/\r\n\r\n");
          out.close();
          return;
        }
        InputStream f=new FileInputStream(filename);

        String mimeType="text/plain";
        if (filename.endsWith(".html") || filename.endsWith(".htm"))
          mimeType="text/html";
        else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg"))
          mimeType="image/jpeg";
        else if (filename.endsWith(".gif"))
          mimeType="image/gif";
        else if (filename.endsWith(".class"))
          mimeType="application/octet-stream";
        out.print("HTTP/1.0 200 OK\r\n"+
          "Content-type: "+mimeType+"\r\n\r\n");
        System.out.print("HTTP/1.1 200 OK\r\n"+
                "Content-type: "+mimeType+"\r\n\r\n");
       // System.out.println("hsabd");

        byte[] a=new byte[4096];
        int n;
        while ((n=f.read(a))>0)
          out.write(a, 0, n);
        out.close();
      }
      catch (FileNotFoundException x) {
        out.println("HTTP/1.0 404 Not Found\r\n"+
          "Content-type: text/html\r\n\r\n"+
          "<html><head></head><body>"+filename+" not found</body></html>\n");
        out.close();
      }
    }
    catch (IOException x) {
      System.out.println(x);
    }
  }
}

