import java.io.*;
import java.net.*;

public class WebClient {

  public static void main(String[] args)
    throws IOException, InterruptedException {
    for (String url : args) {
      WebSocket ws = new WebSocket(url);
      ws.main();
    }
  }
}
