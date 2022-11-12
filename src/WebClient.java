import java.io.*;
import java.net.*;

public class WebClient {
  static String url ="http://web.stanford.edu/dept/its/support/techtraining/techbriefing-media/Intro_Net_91407.ppt";

  // static String url = "example.com";

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
    ps.print("Host: " + host() + "\r\n");
    ps.print("\r\n");
    ps.flush();
    s.shutdownOutput();

    // Read response from server
    int count, offset;
    byte[] buffer = new byte[2048];
    boolean eohFound = false;
    while ((count = bis.read(buffer)) != -1)
    {
        offset = 0;
        if(!eohFound){
            String string = new String(buffer, 0, count);
            int indexOfEOH = string.indexOf("\r\n\r\n");
            if(indexOfEOH != -1) {
                count = count-indexOfEOH-4;
                offset = indexOfEOH+4;
                eohFound = true;
            } else {
                count = 0;
            }
        }
      fos.write(buffer, offset, count);
      fos.flush();
    }

    // Close streams
    fos.close();
    bis.close();
    ps.close();
  }
}
