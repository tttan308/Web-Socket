public class mainSocket {
    public static void main(String[] args) throws InterruptedException{

        WebSocket WebSocket1 = new WebSocket("http://web.stanford.edu/class/cs143/handouts/");
        Thread thread1 = new Thread(WebSocket1);
        thread1.start();
    }
}
