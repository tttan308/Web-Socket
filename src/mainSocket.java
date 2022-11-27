public class mainSocket {
    public static void main(String[] args) throws InterruptedException{

        WebSocket WebSocket1 = new WebSocket("http://web.stanford.edu/class/cs231a/assignments.html");
        Thread thread1 = new Thread(WebSocket1);
        thread1.start();

        WebSocket WebSocket2 = new WebSocket("http://gaia.cs.umass.edu/wireshark-labs/alice.txt");
        Thread thread2 = new Thread(WebSocket2);
        thread2.start();

    }
}
