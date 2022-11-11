import java.io.*;
import java.net.*;

// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;

// import java.nio.charset.StandardCharsets;

public class WebClient {
  static String url =
    "http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Intro_v8.1.docx";

  //====== MAIN ======//

  public static void main(String[] args)
    throws IOException, InterruptedException {
    url = url.replace("http://", "");

    System.out.println(host());
    System.out.println(get());
    System.out.println(path());

    try {
      // Create socket
      Socket socket = new Socket(host(), 80);

      File file = new File(path());

      if (isSubfolder()) {
        file.mkdirs();
        // Path path = Paths.get(url);
        // List<Path> paths;
        // try (Stream<Path> walk = Files.walk(path)) {
        //   paths =
        //     walk.filter(Files::isRegularFile).collect(Collectors.toList());
        // }
        // paths.forEach(x -> System.out.println(x));
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
    BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
    FileOutputStream fos = new FileOutputStream(file);

    // Send request to server
    ps.print("GET " + get() + " HTTP/1.1\r\n");
    ps.print("HOST: " + host() + "\r\n");
    ps.print("\r\n");
    ps.flush();
    s.shutdownOutput();

    // Write to file
    byte[] buffer = new byte[1024];
    int len;
    while ((len = bis.read(buffer)) != -1) {
      fos.write(buffer, 0, len);
    }

    // Close streams
    fos.close();
    bis.close();
    ps.close();
  }
}
