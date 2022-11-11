import java.io.*;
import java.net.*;

public class WebClient {
    
    public static void main(String[] args) throws IOException, InterruptedException {
        String url = "gaia.cs.umass.edu";
        String path = "/wireshark-labs/alice.txt";
        try {
            //Create socket
            Socket socket = new Socket(url, 80); 
            PrintStream req = new PrintStream(socket.getOutputStream());
            DataInputStream recv = new DataInputStream(socket.getInputStream());

            //Send request to server with url and path
            req.print("GET " + path + " HTTP/1.1\r\n");
            req.print("HOST: "+ url + "\r\n");
            req.print("\r\n");
            req.flush();
            socket.shutdownOutput(); 
            String name = (path == "/")
            ? "index.html"
            : path.substring(path.lastIndexOf("/") + 1);
    
          String fullPathName = "./binary/" + url + "_" + name;
    
          File file = new File(fullPathName);
    
          if (!file.exists()) {
            file.delete();
            file.createNewFile();
          }
    
          FileOutputStream fos = new FileOutputStream(file);
    
          byte[] buffer = new byte[1024];
          int len;
          while ((len = recv.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
          }
    
          fos.close();
            System.out.println("Success!");
            recv.close();
            req.close();
            socket.close();
        } 
        catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}