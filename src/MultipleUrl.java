import java.io.IOException;

public class MultipleUrl implements Runnable {
    private static String[] urls;

    public MultipleUrl(String[] urls) {
        MultipleUrl.urls = urls;
    }

    @Override
    public void run() {
        for (String url : urls) {
            try {
                WebSocket w1 = new WebSocket(url);
                w1.download();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
