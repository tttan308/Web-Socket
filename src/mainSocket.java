import java.io.IOException;

public class mainSocket {
    public static void main(String[] args) throws InterruptedException, IOException{
        // String[] urls = {"http://web.stanford.edu/dept/its/support/techtraining/techbriefing-media/Intro_Net_91407.ppt","http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Intro_v8.1.docx", "web.stanford.edu/class/cs231a/project.html", "http://web.stanford.edu/class/cs224w/slides/01-intro.pdf"};
        
        // WebSocket ws = new WebSocket(urls);
        // ws.downloadUrlsWithThread();

        WebSocket ws = new WebSocket("http://web.stanford.edu/class/cs231a/course_notes/") ; 
        ws.download();
    }
}
