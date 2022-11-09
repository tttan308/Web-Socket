import java.io.*;
import java.net.*;

public class WebClient {
    
    public static void main(String[] args) throws IOException {
        String url = "www-net.cs.umass.edu";
        String path = "/wireshark-labs/Wireshark_Intro_v8.1.docx";
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

            //Handle 
            File file;
            if(path.substring(path.length()-1) != "/" || path.length() == 1)
            {
                //File Handling
                if(path == "/") file = new File("./binary/" + url + "_index.html");
                else file = new File("./binary/" + url + "_" + path.substring(path.lastIndexOf("/") + 1, path.length()));
                if(file.createNewFile() == false)
                {
                    file.delete();
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len;
                while((len = recv.read(buffer)) != -1)
                {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            else{
                
            }
            System.out.println("Success!");
            socket.close();
        } 
        catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}

