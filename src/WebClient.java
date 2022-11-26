import java.io.*;
import java.net.*;

public class WebClient {
  static String url = "http://web.stanford.edu/class/cs231a/course_notes/";

  //====== MAIN ======//

  public static void main(String[] args)
    throws IOException, InterruptedException {
    url = url.replace("http://", "");

    System.out.println("Host: " + host(url));
    System.out.println("Get: " + get(url));

    try {
      // Create socket and streams
      Socket s = new Socket(host(url), 80);
      PrintStream ps = new PrintStream(s.getOutputStream());
      InputStream is = s.getInputStream();

      if (isSubfolder(url)) {
        File folder = new File(fileName(url));
        folder.mkdirs();

        // Send GET request
        ps.print("GET " + get(url) + " HTTP/1.1\r\n");
        ps.print("Host: " + host(url) + "\r\n");
        ps.print("\r\n");
        ps.flush();

        //Read header
        String header = new String(header(is));
        System.out.println(header);

        //Read body with content length
        String body = new String(content(is, header));

        for (int i = 0; i < body.length(); i++) {
          if (
            body.charAt(i) == 'h' &&
            body.charAt(i + 1) == 'r' &&
            body.charAt(i + 2) == 'e' &&
            body.charAt(i + 3) == 'f' &&
            body.charAt(i + 4) == '='
          ) {
            int j = i + 6;
            while (body.charAt(j) != '"') {
              j++;
            }
            String subUrl = body.substring(i + 6, j);
            System.out.println(subUrl);
            if (subUrl.length() - subUrl.replace(".", "").length() > 0) {
              System.out.println("Downloading " + url + subUrl);
              Download(s, is, ps, url + subUrl);
            }
          }
        }
      } else {
        Download(s, is, ps, url);
      }

      System.out.println("Success!");

      // Close streams and socket
      is.close();
      ps.close();
      s.close();
    } catch (IOException e) {
      System.out.println("Error: " + e);
    }
  }

  //====== CALL METHODS ======//

  private static boolean isRoot(String url) {
    return url.length() - url.replace("/", "").length() == 1;
  }

  private static boolean isSubfolder(String url) {
    return !isRoot(url) && url.length() - url.replace(".", "").length() == 2;
  }

  private static String host(String url) {
    return url.split("/")[0];
  }

  private static String get(String url) {
    if (isRoot(url)) return "/";
    return url.substring(url.indexOf("/", 8));
  }

  private static String path(String url) {
    if (isRoot(url)) return "./binary/" + host(url) + "_" + "index.html";
    if (isSubfolder(url)) return "./binary/" + host(url) + "_" + get(url);
    return (
      "./binary/" + host(url) + "_" + url.substring(url.lastIndexOf("/") + 1)
    );
  }

  private static String fileName(String url) {
    if (isRoot(url)) return "index.html";
    return url.split("/")[url.split("/").length - 1];
  }

  private static byte[] header(InputStream is) throws IOException {
    byte[] header = new byte[2048];
    int offset = 0;

    while (true) {
      int cnt = is.read(header, offset, 1);
      if (header[offset] == (byte) '\n') {
        if (
          offset >= 3 &&
          header[offset - 1] == (byte) '\r' &&
          header[offset - 2] == (byte) '\n' &&
          header[offset - 3] == (byte) '\r'
        ) {
          break;
        }
      }
      offset += cnt;
    }
    return header;
  }

  private static byte[] content(InputStream is, String header)
    throws IOException {
    int contentLength = Integer.parseInt(
      header.split("Content-Length: ")[1].split("\r\n")[0]
    );
    byte[] content = new byte[contentLength];
    int offset = 0;

    while (offset < contentLength) {
      int cnt = is.read(content, offset, contentLength - offset);
      offset += cnt;
    }
    return content;
  }

  //====== SEND METHODS ======//

  private static void Download(
    Socket s,
    InputStream is,
    PrintStream ps,
    String url
  )
    throws IOException {
    // Initialize file
    File file = new File(path(url));
    System.out.println(path(url));
    // Initialize stream
    FileOutputStream fos = new FileOutputStream(file);

    // Send request to server
    ps.print("GET " + get(url) + " HTTP/1.1\r\n");
    ps.print("Host: " + host(url) + "\r\n");
    ps.print("Connection: Keep-Alive\r\n");
    ps.print("\r\n");
    ps.flush();

    //Read header
    String header = new String(header(is));

    if (header.contains("Content-Length")) {
      byte content[] = content(is, header);
      fos.write(content);
    } else {
      //Read chunk
      byte[] chunk = new byte[2048];
      int chunkOffset, chunkLength;
      while (true) {
        chunkOffset = chunkLength = 0;
        //Read chunk length
        while (true) {
          int cnt = is.read(chunk, chunkOffset, 1);
          if (
            chunkOffset >= 1 &&
            chunk[chunkOffset] == (byte) '\n' &&
            chunk[chunkOffset - 1] == (byte) '\r'
          ) break;
          chunkOffset += cnt;
        }
        chunkLength =
          Integer.parseInt(new String(chunk, 0, chunkOffset - 1), 16);

        if (chunkLength == 0) break;
        chunk = new byte[chunkLength];
        chunkOffset = 0;
        while (chunkOffset < chunkLength) {
          int cnt = is.read(chunk, chunkOffset, chunkLength - chunkOffset);
          chunkOffset += cnt;
        }
        fos.write(chunk);
        is.read(chunk, 0, 2);
      }
    }

    // Close stream
    fos.close();
  }
}
