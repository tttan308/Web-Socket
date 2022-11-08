import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.io.FileWriter;

public class WebClient {

    public static void main(String[] args) throws IOException {

        // if( args.length != 1)                                                      
        // {
        //     System.out.println( "Usage: SimpleSocketClientExample <server> <path>" );
        //     System.exit( 0 );
        // }
        // String url = args[0];
        String url = "example.com";
        String path = "/index.html";
        try {
            Socket socket = new Socket(url, 80); 
            PrintStream output = new PrintStream(socket.getOutputStream());
            output.print("GET " + path + " HTTP/1.1\r\n");
            output.print("HOST: "+ url + "\r\n");
            output.print("\r\n");
            output.flush();
            socket.shutdownOutput();
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // if(path == "/"){
                File file = new File("./binary/" + url + "_index.html");
                boolean createFile = file.createNewFile();
                System.out.println("File created: "+ file.getName());
                FileWriter fw = new FileWriter(file);
                String outStr;
                while ((outStr = input.readLine()) != null) {
                    fw.write(outStr + "\n");
                }
                fw.close();
            // }
            // else{
            //     File fileName = new File("./binary/" + url + path);
            //     FileOutputStream newFile = new FileOutputStream(fileName);
            // }
            input.close();
            output.close();
            socket.close();              
        } 
        catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}

