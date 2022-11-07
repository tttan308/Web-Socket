import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;


public class WebClient {

    public static void main(String[] args) throws IOException {

        if( args.length != 1)
        {
            System.out.println( "Usage: SimpleSocketClientExample <server> <path>" );
            System.exit( 0 );
        }
        String url = args[0];
        // String url = "example.com";
        try {
            Socket socket = new Socket(url, 80); 
            PrintStream output = new PrintStream(socket.getOutputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output.print("GET / HTTP/1.1\r\n");
            output.print("Host:"+ url + "\r\n");
            output.print("\r\n");
            output.flush();
            socket.shutdownOutput();
            String outStr;
            while ((outStr = input.readLine()) != null) {
                System.out.println(outStr);
            }
            input.close();
            output.close();
            socket.close();              
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
    }
}