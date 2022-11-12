import java.io.*;
import java.net.*;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;

import java.nio.charset.StandardCharsets;

public class WebClient {
  // static String url =
  //   "http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Intro_v8.1.docx";

  static String url = "example.com";

  //====== MAIN ======//

  public static void main(String[] args)
    throws IOException, InterruptedException {
    url = url.replace("http://", "");

    System.out.println("Host: " + host());
    System.out.println("Get: " + get());
    System.out.println("Path: " + path());

    try {
      // Create socket
      Socket socket = new Socket(host(), 80);

      File file = new File(path());

      if (isSubfolder()) {
        file.mkdirs();
      } else {
        Download(socket, file);
      }

      System.out.println("Success!");

      socket.close();
    } catch (IOException e) {
      System.out.println("Error: " + e);
    }
  }

  //====== CALL METHODS ======//

  private static boolean isRoot() {
    return url.split("/").length == 1;
  }

  private static boolean isSubfolder() {
    return !isRoot() && !url.substring(url.lastIndexOf("/") + 1).contains(".");
  }

  private static String host() {
    return url.split("/")[0];
  }

  private static String get() {
    if (isRoot()) return "index.html";
    String get = url.substring(url.indexOf("/", 8));
    if (isSubfolder()) return get + "*";
    return get;
  }

  private static String path() {
    if (isSubfolder()) return (
      "./" + url.split("/")[url.split("/").length - 1] + "/"
    );

    String path = "./binary/" + host() + "_";
    if (isRoot()) return path + "index.html";
    return path + url.substring(url.lastIndexOf("/") + 1);
  }

  //====== SEND METHODS ======//

  private static void Download(Socket s, File file) throws IOException {
    // Initialize streams
    PrintStream ps = new PrintStream(s.getOutputStream());
    InputStream bis = s.getInputStream();
    FileOutputStream fos = new FileOutputStream(file);

    // Send request to server
    ps.print("GET " + get() + " HTTP/1.1\r\n");
    ps.print("HOST: " + host() + "\r\n");
    ps.print("\r\n");
    ps.flush();
    s.shutdownOutput();

    // byte[] buffer = new byte[8192];
    // // Write to file
    // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
    //   8192
    // );
    // int read = bis.read();
    // while (read != -1) {
    //   byteArrayOutputStream.write((byte) read);
    //   read = bis.read();
    // }

    // byteArrayOutputStream.flush();
    // buffer = byteArrayOutputStream.toByteArray();

    // fos.write(buffer);

    int len;
    int contentLength = 0;

    BufferedReader r = new BufferedReader(new InputStreamReader(bis));
    String line;
    while ((line = r.readLine()) != null) {
      if (line.equals("")) break;
      if (line.startsWith("Content-Length: ")) {
        contentLength = Integer.parseInt(line.substring(16));
      }
      System.out.println(line);
    }

    byte[] buffer = new byte[1024];

    while ((len = bis.read(buffer)) > 0) {
      fos.write(buffer, 0, len);
    }

    // RandomAccessFile raf = new RandomAccessFile(file, "rw");
    // raf.setLength(contentLength + 1);
    // raf.close();

    // Close streams
    fos.close();
    bis.close();
    ps.close();
  }
}
