import java.io.IOException;

public class mainSocket {
    public static void main(String[] args) throws InterruptedException, IOException{
        // String[] urls = {"web.stanford.edu/dept/its/support/techtraining/techbriefing-media/Intro_Net_91407.ppt","web.stanford.edu/class/cs224w/slides/08-GNN-application.pdf","web.stanford.edu/class/cs231a/project.html"};
        
        // WebSocket ws = new WebSocket(urls);
        // ws.downloadUrls();

        WebSocket ws = new WebSocket("http://web.stanford.edu/class/cs143/handouts/") ; 
        ws.download();

    }
}
