import java.io.*;
import java.net.*;

public class WebClient {
  static String url;
  static String path;

  public static File getFile() throws IOException {
    URL u = new URL(url);
    URLConnection uc = u.openConnection();
    int contentLength = uc.getContentLength();

    try (InputStream raw = uc.getInputStream()) {
      InputStream in = new BufferedInputStream(raw);
      byte[] data = new byte[contentLength];
      int bytesRead = 0;
      int offset = 0;
      while (offset < contentLength) {
        bytesRead = in.read(data, offset, data.length - offset);
        if (bytesRead == -1) break;
        offset += bytesRead;
      }

      if (offset != contentLength) {
        throw new IOException(
          "Only read " + offset + " bytes; Expected " + contentLength + " bytes"
        );
      }

      String filename = u.getFile();
      filename = filename.substring(filename.lastIndexOf('/') + 1);
      File f = new File(filename);
      try (FileOutputStream fout = new FileOutputStream(f)) {
        fout.write(data);
        fout.flush();
      }
      return f;
    }
  }

  public static void main(String[] args) throws IOException {
    url = "www-net.cs.umass.edu";
    path = "/wireshark-labs/Wireshark_Intro_v8.1.docx";

    try {
      Socket socket = new Socket(url, 80);
      PrintStream req = new PrintStream(socket.getOutputStream());
      DataInputStream recv = new DataInputStream(socket.getInputStream());

      req.print("GET " + path + " HTTP/1.1\r\n");
      req.print("HOST: " + url + "\r\n");
      req.print("\r\n");
      req.flush();
      socket.shutdownOutput();

      // if (path.substring(path.length() - 1) != "/" || path.length() == 1) {

      // String name = (path == "/")
      //   ? "index.html"
      //   : path.substring(path.lastIndexOf("/") + 1);

      // String fullPathName = "./binary/" + url + "_" + name;

      // File file = new File(fullPathName);

      // if (!file.exists()) {
      //   file.delete();
      //   file.createNewFile();
      // }

      FileOutputStream fos = new FileOutputStream(getFile());

      byte[] buffer = new byte[1024];
      int len;
      while ((len = recv.read(buffer)) != -1) {
        fos.write(buffer, 0, len);
      }

      fos.close();

      // } else {}

      System.out.println("Success!");

      socket.close();
    } catch (IOException e) {
      System.out.println("Error: " + e);
    }
  }
}
