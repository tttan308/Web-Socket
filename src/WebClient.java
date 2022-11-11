import java.io.*;
import java.net.*;

// import java.nio.charset.StandardCharsets;

public class WebClient {
  static String url = "web.stanford.edu/class/cs224w/slides/01-intro.pdf";

  static String host = url.substring(0, url.indexOf("/"));
  static String get = url.substring(url.indexOf("/"));
  static String path = url.substring(url.lastIndexOf("/") + 1);

  public static void main(String[] args)
    throws IOException, InterruptedException {
    if (path.equals("")) path = "index.html";

    try {
      //Create socket
      Socket socket = new Socket(host, 80);

      String name = "./binary/" + host + "_" + path;

      Download(socket, name);

      System.out.println("Success!");

      socket.close();
    } catch (IOException e) {
      System.out.println("Error: " + e);
    }
  }

  private static void Download(Socket s, String name) throws IOException {
    File file = new File(name);

    PrintStream ps = new PrintStream(s.getOutputStream());
    BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
    FileOutputStream fos = new FileOutputStream(file);

    //Send request to server
    ps.print("GET " + get + " HTTP/1.1\r\n");
    ps.print("HOST: " + host + "\r\n");
    ps.print("\r\n");
    ps.flush();
    s.shutdownOutput();

    if (!file.exists()) {
      file.delete();
      file.createNewFile();
    }

    byte[] buffer = new byte[1024];
    int len;

    while ((len = bis.read(buffer)) != -1) {
      fos.write(buffer, 0, len);
    }

    fos.close();
    bis.close();
    ps.close();
  }
}
