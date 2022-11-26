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

      if(isSubfolder(url)){
        File folder = new File("./binary/" + host(url) + "_" + fileName(url));
        folder.mkdirs();

        // Send GET request
        ps.print("GET " + get(url) + " HTTP/1.1\r\n");
        ps.print("Host: " + host(url) + "\r\n");
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
        headerStr = new String(header, 0, offset);
        System.out.println(headerStr);

        //Read body with content length
        int contentLength = Integer.parseInt(headerStr.substring(headerStr.indexOf("Content-Length: ") + 16, headerStr.indexOf("Content-Length: ") + 16 + headerStr.substring(headerStr.indexOf("Content-Length: ") + 16).indexOf("\r\n")));
        byte[] body = new byte[contentLength];
        offset = 0;
        while(offset < contentLength)
        {
            cnt = is.read(body, offset, contentLength - offset);
            offset += cnt;
        }
        String bodyStr = new String(body);

        for(int i = 0 ; i<bodyStr.length(); i++){
          if(bodyStr.charAt(i) == 'h' && bodyStr.charAt(i+1) == 'r' && bodyStr.charAt(i+2) == 'e' && bodyStr.charAt(i+3) == 'f' && bodyStr.charAt(i+4) == '='){
            int j = i+6;
            while(bodyStr.charAt(j) != '"'){
              j++;
            }
            String subUrl = bodyStr.substring(i+6, j);
            System.out.println(subUrl);
            if(subUrl.length() - subUrl.replace(".", "").length() > 0){
              System.out.println("Downloading " + url + subUrl);
              Download(s, is, ps, url + subUrl);
            }
          }
        }
      }      
      else{
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
    return "./binary/" + host(url) + "_" + url.substring(url.lastIndexOf("/") + 1);
  }

  private static String fileName(String url) {
    if (isRoot(url)) return "index.html";
    return url.split("/")[url.split("/").length - 1];
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
        System.out.println("Chunked");
        //Read chunk
        byte[] chunk = new byte[2048];
        int chunkOffset = 0;
        int chunkLength = 0;
        while(true)
        {
            chunkOffset = 0;
            chunkLength = 0;
            //Read chunk length
            while(true)
            {
                cnt = is.read(chunk, chunkOffset, 1); 
                System.out.println(cnt);
                if(chunk[chunkOffset] == (byte)'\n')
                {
                    if(chunkOffset >= 1 && chunk[chunkOffset-1] == (byte)'\r')
                    {
                        break;
                    }
                }         
                chunkOffset+=cnt;   
              }
            chunkLength = Integer.parseInt(new String(chunk, 0, chunkOffset-1), 16);
            System.out.println(chunkLength);
            if(chunkLength == 0)
            {
                break;
            }
            chunk = new byte[chunkLength];
            chunkOffset = 0;
            while(chunkOffset < chunkLength)
            {
                cnt = is.read(chunk, chunkOffset, chunkLength - chunkOffset);
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