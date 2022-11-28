import java.io.IOException;

public class mainSocket {
    public static void main(String[] args) throws InterruptedException, IOException{
        String[] urls = {"http://web.stanford.edu/class/cs224w/slides/08-GNN-application.pdf","http://web.stanford.edu/class/cs231a/project.html"};
        Runnable r1 = new WebSocket(urls[0]);
        Thread t1 = new Thread(r1);
        Runnable r2 = new WebSocket(urls[1]);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();
    }
}
