import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class WebClient {

  public static void main(String[] args)
    throws IOException, InterruptedException {
    String url = "example.com/";

    String host = url.substring(0, url.indexOf("/"));
    String path = url.substring(url.indexOf("/"));
    if (path.equals("/")) path = "index.html";

    try {
      //Create socket
      Socket socket = new Socket(host, 80);
      PrintStream req = new PrintStream(socket.getOutputStream());
      BufferedInputStream recv = new BufferedInputStream(
        socket.getInputStream()
      );

      //Send request to server with url and path
      req.print("GET " + path + " HTTP/1.1\r\n");
      req.print("HOST: " + host + "\r\n");
      req.print("\r\n");
      req.flush();
      socket.shutdownOutput();

      String fullPathName = "./binary/" + host + "_" + path;

      File file = new File(fullPathName);

      if (!file.exists()) {
        file.delete();
        file.createNewFile();
      }

      FileOutputStream fos = new FileOutputStream(file);

      byte[] buffer = new byte[1024];
      int len;

      // Get body
      while ((len = recv.read(buffer)) > 0) {
        fos.write(buffer, 0, len);
      }

      fos.close();
      System.out.println("Success!");
      recv.close();
      req.close();
      socket.close();
    } catch (IOException e) {
      System.out.println("Error: " + e);
    }
  }
}
