import java.io.IOException;

public class main {
    public static void main(String[] args) throws InterruptedException, IOException
    {
            if(args.length == 0){
                System.out.println("Please enter a URL");
                System.exit(0);
            }
            else if(args.length == 1){
                System.out.println("Donwload 1 url");
                System.out.println(args[0]);
                WebSocket ws = new WebSocket(args[0]);
                ws.download();
            }
            else{
                System.out.println("Download with multithread with multiple URLs");
                WebSocket ws = new WebSocket(args);
                ws.downloadUrlsWithThread();
            }
        }
}