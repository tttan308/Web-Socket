import java.io.IOException;

public class main {
    public static void main(String[] args) throws InterruptedException, IOException
    {
            if(args.length == 0){
                System.out.println("Please enter a URL");
                System.exit(0);
            }
            else if(args.length == 1){
                System.out.println("Download 1 url");
                WebSocket ws = new WebSocket(args[0]);
                ws.download();
            }
            else if(args.length == 2){
                if(args[1].contains("-s")){
                    System.out.println("Download folder with single sockets");
                    WebSocket ws = new WebSocket(args[0]);
                    ws.DownloadFolderWithSingleSocket();
                }
                else{
                    System.out.println("Download multithread with multiple URLs");
                    WebSocket ws = new WebSocket(args);
                    ws.downloadUrlsWithThread();
                }
            }
            else{
                System.out.println("Download multithread with multiple URLs");
                WebSocket ws = new WebSocket(args);
                ws.downloadUrlsWithThread();
            }
        }
}