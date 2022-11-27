public class mainSocket {
    public static void main(String[] args) throws InterruptedException{

        WebSocket WebSocket1 = new WebSocket("http://web.stanford.edu/class/cs224w/slides/01-intro.pdf");
        
        WebSocket1.start();
        WebSocket WebSocket2 = new WebSocket("http://web.stanford.edu/class/cs224w/slides/02-tradition-ml.pdf");
        
        WebSocket2.start();

        WebSocket WebSocket3 = new WebSocket("http://web.stanford.edu/class/cs224w/slides/03-nodeemb.pdf");
        
        WebSocket3.start();
    }
}
