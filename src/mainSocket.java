public class mainSocket {
    public static void main(String[] args) throws InterruptedException{

        WebSocket WebSocket1 = new WebSocket("http://web.stanford.edu/class/cs224w/slides/01-intro.pdf");
        
        WebSocket WebSocket2 = new WebSocket("http://web.stanford.edu/class/cs231a/assignments.html");

        Thread t1 = new Thread(WebSocket1);
        Thread t2 = new Thread(WebSocket2);
        System.out.println("Start");
        t1.start();
        t2.start();
        System.out.println("mainSocket");
    }
}
