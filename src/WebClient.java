import java.io.*;
import java.net.*;

public class WebClient {
  static String url = "http://web.stanford.edu/class/cs231a/course_notes/";

  //====== MAIN ======//

  public static void main(String[] args)
    throws IOException, InterruptedException {
    url = url.replace("http://", "");

    System.out.println("Host: " + host());
    System.out.println("Get: " + get());

    try {
      // Create socket and streams
      Socket s = new Socket(host(), 80);
      PrintStream ps = new PrintStream(s.getOutputStream());
      InputStream is = s.getInputStream();

      if (isSubfolder()) {
        File folder = new File("./" + host() + "_" + fileName());
        folder.mkdirs();
        ps = new PrintStream(s.getOutputStream());

        // Send request to server

        ps.print("GET " + get() + " HTTP/1.1\r\n");
        ps.print("Host: " + host() + "\r\n");
        ps.print("\r\n");
        ps.flush();

        // Read response from server
        BufferedReader br = new BufferedReader(
          new InputStreamReader(s.getInputStream())
        );
        String line = br.readLine();

        while (line != null) {
          if (line.contains("href=\"")) {
            int firstIndex = line.indexOf("href=\"") + 6;
            int lastIndex = line.indexOf("\"", firstIndex);
            String fileName = line.substring(firstIndex, lastIndex);

            if (!fileName.contains("http://") && fileName.contains(".")) {
              try {
                System.out.println("Downloading " + fileName);
                Download(s, is, ps, fileName);
              } catch (Exception e) {
                System.out.println("Error downloading " + fileName);
              }
            }
          }
          line = br.readLine();
        }

        br.close();
      } else {
        Download(s, is, ps, fileName());
      }

      System.out.println("Success!");

      // Close streams and socket
      ps.close();
      s.close();
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
    if (isRoot()) return "/";
    return url.substring(url.indexOf("/", 8));
  }

  private static String path(String fileName) {
    if (isSubfolder()) return "./" + host() + "_" + fileName() + "/" + fileName;
    return "./binary/" + host() + "_" + fileName;
  }

  private static String fileName() {
    if (isRoot()) return "index.html";
    return url.split("/")[url.split("/").length - 1];
  }

  //====== SEND METHODS ======//

  private static void Download(
    Socket s,
    InputStream is,
    PrintStream ps,
    String fileName
  )
    throws IOException {
    // Initialize file
    File file = new File(path(fileName));

    // Initialize stream
    FileOutputStream fos = new FileOutputStream(file);

    // Send request to server
    ps.print("GET " + get() + fileName + " HTTP/1.1\r\n");
    ps.print("Host: " + host() + "\r\n");
    ps.print("\r\n");
    ps.flush();
    // s.shutdownOutput();

    // Read response from server
    int count, offset;
    byte[] buffer = new byte[2048];
    boolean eohFound = false;
    while ((count = is.read(buffer)) != -1) {
      offset = 0;
      if (!eohFound) {
        String string = new String(buffer, 0, count);
        int indexOfEOH = string.indexOf("\r\n\r\n");
        if (indexOfEOH != -1) {
          count = count - indexOfEOH - 4;
          offset = indexOfEOH + 4;
          eohFound = true;
        } else {
          count = 0;
        }
      }
      fos.write(buffer, offset, count);
      fos.flush();
    }

    is.close();
    fos.close();
  }
}
