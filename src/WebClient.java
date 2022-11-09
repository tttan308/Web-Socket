import java.io.*;
import java.net.*;

public class WebClient {
    
    public static void main(String[] args) throws IOException {

        // if( args.length != 1)                                                      
        // {
        //     System.out.println( "Usage: SimpleSocketClientExample <server> <path>" );
        //     System.exit( 0 );
        // }
        // String url = args[0];
        String url = "anglesharp.azurewebsites.net";
        String path = "/Chunked";
        try {
            Socket socket = new Socket(url, 80); 
            PrintStream req = new PrintStream(socket.getOutputStream());

            req.print("GET " + path + " HTTP/1.1\r\n");
            req.print("HOST: "+ url + "\r\n");
            req.print("\r\n");
            req.flush();
            socket.shutdownOutput();
            // BufferedReader recv = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataInputStream recv = new DataInputStream(socket.getInputStream());
            if(path == "/Chunked"){
                File file = new File("./binary/" + url + "_index.html");
                boolean createFile = file.createNewFile();
                System.out.println("File created: "+ file.getName());
                FileOutputStream fw = new FileOutputStream(file);
                byte[] outStr = new byte[1024];
                int len;
                while((len = recv.read(outStr)) != -1){
                    fw.write(outStr, 0, len);
                }
                fw.close();
            }
            else if(path.contains(".txt")){
                File file = new File("./binary/" + url + ".txt");
                boolean createFile = file.createNewFile();
                System.out.println("File created: "+ file.getName());
                FileOutputStream fw = new FileOutputStream(file);
                byte[] outStr = new byte[1024];
                int len;
                while((len = recv.read(outStr)) != -1){
                    
                    fw.write(outStr, 0, len);
                }
                fw.close();
            }
            
        } 
        catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}

