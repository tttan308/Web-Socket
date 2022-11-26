import java.io.*;
import java.net.*;

public class WebClient {
  static String url = "http://web.stanford.edu/class/cs224w/slides/08-GNN-application.pdf";

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

      Download(s, is, ps, fileName());

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

  private static boolean isRoot() {
    return url.split("/").length == 1;
  }

  private static boolean isSubfolder() {
    return !isRoot() && url.split("/").length > 2;
  }

  private static String host() {
    return url.split("/")[0];
  }

  private static String get() {
    if (isRoot()) return "/";
    return url.substring(url.indexOf("/", 8));
  }

  private static String path(String fileName) {
    // if (isSubfolder()) return "./" + host() + "_" + fileName() + "/" + fileName;
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
    ps.print("GET " + get() + " HTTP/1.1\r\n");
    ps.print("Host: " + host() + "\r\n");
    ps.print("\r\n");
    ps.flush();

    //Read header
    byte[] header = new byte[2048];
    int offset = 0;
    int cnt = 0;
    String headerStr = "";
    while(true)
    {
        cnt = is.read(header, offset, 1);
        if(header[offset] == (byte)'\n')
        {
            if(offset >= 3 && header[offset-1] == (byte)'\r' && header[offset-2] == (byte)'\n' && header[offset-3] == (byte)'\r')
            {
                break;
            }
        }         
        offset+=cnt;   
    }
    headerStr = new String(header, 0, offset-1); 
    if(headerStr.contains("Content-Length"))
    {
        int contentLength = Integer.parseInt(headerStr.split("Content-Length: ")[1].split("\r\n")[0]);
        byte[] content = new byte[contentLength];
        int contentOffset = 0;
        while(contentOffset < contentLength)
        {
            cnt = is.read(content, contentOffset, contentLength - contentOffset);
            contentOffset += cnt;
        }
        fos.write(content);
    }
    else
    {
        int chunkSize = 0;
    }


    // Close stream
    fos.close();
  }

}