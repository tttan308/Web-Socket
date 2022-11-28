import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class mainSocket {
    public static void main(String[] args) throws InterruptedException, IOException{
        String[] urls = {"http://web.stanford.edu/class/cs224w/slides/08-GNN-application.pdf","http://web.stanford.edu/class/cs231a/project.html"};
        
        ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(2);
        executor.execute(new WebSocket(urls[0]));
        executor.execute(new WebSocket(urls[1]));
        executor.shutdown();
    }
}
