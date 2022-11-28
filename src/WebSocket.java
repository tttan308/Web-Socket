import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class WebSocket {
    private static String url;
    private static String[] urls;
    private static int dem = 0;

    public WebSocket(String url) {
        WebSocket.url = url;
    }

    public WebSocket(String[] urls) {
        WebSocket.urls = urls;
    }

    public static void ReplaceUrl() {
        if(url != null) {
            url = url.replace("http://", "");
            url = url.replace("https://", "");
            url = url.replace("www.", "");
        }
        else if (urls != null) {
            for(int i = 0; i < urls.length; i++) {
                urls[i] = urls[i].replace("http://", "");
                urls[i] = urls[i].replace("https://", "");
                urls[i] = urls[i].replace("www.", "");
            }
        }
    }

    public static Socket createSocket(String url) throws IOException {
        Socket socket = new Socket(host(url), 80);
        return socket;
    }

    public static void closeSocket(Socket socket) throws IOException {
        socket.close();
    }

    private static boolean isRoot(String url) {
        return (url.length() - url.replace("/", "").length() == 1 && url.charAt(url.length() - 1) == '/') || (url.length() - url.replace("/", "").length() == 0);
    }

    private static boolean isSubfolder(String url) {
        return !isRoot(url) && url.length() - url.replace(".", "").length() <= 2 && url.charAt(url.length() - 1) == '/';
    }

    private static String host(String url) {
        return url.split("/")[0];
    }

    private static String get(String url) {
        if (isRoot(url)) return "/";
        return url.substring(url.indexOf("/", 8));
    }

    private static String path(String url, String fileName) {
        if (isRoot(url) || (!get(url).contains(".") && get(url).charAt(get(url).length()-1) != '/')) return "./binary/" + host(url) + "_" + "index.html";
        if (isSubfolder(url)) return (
            "./binary/" + host(url) + "_" + fileName(url) + "/" + host(url) + "_" + fileName
            );
        return (
            "./binary/" + host(url) + "_" + url.substring(url.lastIndexOf("/") + 1)
        );
    }

    private static String fileName(String url) {
        if (isRoot(url)) return "index.html";
        return url.split("/")[url.split("/").length - 1];
    }

    private synchronized static String header(InputStream is) throws IOException {
        byte[] header = new byte[2048*2048];
        int offset = 0;

        while (true) {
            int cnt = is.read(header, offset, 1);
            // System.out.println(cnt);
            if (cnt == -1) break;
            if (header[offset] == (byte) '\n') {
            if (
                offset >= 3 &&
                header[offset - 1] == (byte) '\r' &&
                header[offset - 2] == (byte) '\n' &&
                header[offset - 3] == (byte) '\r'
            ) break;
            }
            offset += cnt;
        }
        if(offset == 0) offset = 1;
        String headerStr = new String(header, 0, offset-1); 
        return headerStr;
    }

    private static byte[] content(DataInputStream is, String header) throws IOException {
        int contentLength = Integer.parseInt(
            header.split("Content-Length: ")[1].split("\r\n")[0]
        );
        byte[] content = new byte[contentLength];
        int offset = 0;
        // System.out.println(contentLength);
        while (offset < contentLength) {
            int cnt = is.read(content, offset, contentLength - offset);
            offset += cnt;
        }
        return content;
    }

    public static void Request(PrintStream ps, String url, String fileName) {
        if(isSubfolder(url)) {
            System.out.println("hehe");
            ps.print("GET " + get(url) + fileName + " HTTP/1.1\r\n");
            System.out.println("GET " + get(url) + fileName + " HTTP/1.1");
        }
        else{
            ps.print("GET " + get(url) + " HTTP/1.1\r\n");
            System.out.println("GET " + get(url) + " HTTP/1.1");
        }
        System.out.println("Host: " + host(url));
        ps.print("Host: " + host(url) + "\r\n");
        ps.print("Connection: Keep-Alive\r\n");
        ps.print("\r\n");
        ps.flush();
    }

    public static void downloadContentLength(FileOutputStream fos, DataInputStream is, String header) throws IOException {
        byte[] content = content(is, header);
        fos.write(content);
        fos.flush();
        fos.close();
    }

    public static void downloadChunked(FileOutputStream fos, InputStream is) throws IOException{
        byte[] chunk = new byte[2048];
        int chunkOffset, chunkLength;
        while (true) {
            chunkOffset = chunkLength = 0;

            // Read chunk length
            while (true) {
            int cnt = is.read(chunk, chunkOffset, 1);
            if (
                chunkOffset >= 1 &&
                chunk[chunkOffset] == (byte) '\n' &&
                chunk[chunkOffset - 1] == (byte) '\r'
            ) break;
            chunkOffset += cnt;
            }
            chunkLength =
            Integer.parseInt(new String(chunk, 0, chunkOffset - 1), 16);

            if (chunkLength == 0) break;
            chunk = new byte[chunkLength];
            chunkOffset = 0;
            while (chunkOffset < chunkLength) {
            int cnt = is.read(chunk, chunkOffset, chunkLength - chunkOffset);
            chunkOffset += cnt;
            }
            fos.write(chunk);
            is.read(chunk, 0, 2);
        }
        fos.close();
    }

    public static void Download(DataInputStream is, PrintStream ps, String fileName, String url) throws IOException {
        System.out.println("bat dau tai file");

        // Initialize file
        File file = new File(path(url, fileName));
        // Initialize stream
        System.out.println("Downloading " + fileName + "...");
        FileOutputStream fos = new FileOutputStream(file);
        // Send request to server
        
        Request(ps, url, fileName);
        System.out.println("toi day r nek");
        
        //Read header
        String header = header(is);
        System.out.println(header);
        
        if (header.contains("Content-Length")) downloadContentLength(fos, is, header);
        else if(header.contains("Transfer-Encoding: chunked")) downloadChunked(fos, is);
        System.out.println("Downloaded " + fileName + "!");
        
        // if(header.contains("Connection: close")) closeSocket(socket);
        
        //close file
    }

    public static ArrayList<String> getFileFromFolder(String body){
        ArrayList<String> list = new ArrayList<String>();
        String[] arr = body.split("\n");
        for(String line : arr){
            if (line.contains("href=\"")) {
                int firstIndex = line.indexOf("href=\"") + 6;
                int lastIndex = line.indexOf("\"", firstIndex);
                String fileName = line.substring(firstIndex, lastIndex);
                if (!fileName.contains("http://") && fileName.contains(".")) {
                    list.add(fileName);
                }
            }
        }
        return list;
    }

    public static void DownloadFolder(Socket socket) throws IOException{
        
        PrintStream ps = new PrintStream(socket.getOutputStream());
        File folder = new File("./binary/" + host(url) + "_" + fileName(url));
        folder.mkdirs();
        
        // Send GET request
        ps.print("GET " + get(url) + " HTTP/1.1\r\n");
        ps.print("Host: " + host(url) + "\r\n");
        ps.print("Connection: Keep-Alive\r\n");
        ps.print("\r\n");
        ps.flush();
        
        System.out.println("TOi day r");
        
        DataInputStream is = new DataInputStream(socket.getInputStream());
        //Read header
        String header = header(is);

        System.out.println("TOi day r");
        String body = new String(content(is, header));
        System.out.println(body);
        ArrayList<String> files = getFileFromFolder(body);

        System.out.println("Xong phan body cua folder r");

        // multiple sockets
        // for(String file : files){
        //     try(Socket s = new Socket(host(url), 80)){
        //         Download(s, file, url);
        //         System.out.println("File " + url + file + " downloaded!");
        //         closeSocket(s);
        //     }
        // }
        
        // single socket
        try{
            for(String file : files){
                System.out.println("tai nay");
                Download(is, ps, file, url);
                System.out.println("File " + url + file + " downloaded!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        System.out.println("DOng socket cua folder ah nha");
    }

    public void download() throws IOException {
        ReplaceUrl();
        System.out.println(url);
        Socket socket = createSocket(url);
        // InputStream is = socket.getInputStream();
        // PrintStream ps = new PrintStream(socket.getOutputStream());
        if (isSubfolder(url)) {
            System.out.println("folder");
            DownloadFolder(socket);
        } else {
            // System.out.println("Download file");
            // Download(socket, fileName(url), url);
        }
        closeSocket(socket);
        System.out.println("Da dong socket tong");
    }

    // public void downloadUrls() throws IOException, InterruptedException {
    //     Socket socket = createSocket("web.stanford.edu");
    //     InputStream is = socket.getInputStream();
    //     PrintStream ps = new PrintStream(socket.getOutputStream());
    //     ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(4);
    //     for(String url : urls){
    //         executor.execute(new Runnable() {
    //             @Override
    //             public void run() {
    //                 try {
    //                     Download(socket, url, url);
    //                 } catch (IOException e) {
    //                     e.printStackTrace();
    //                 }
    //             }
    //         });
    //     }
    //     executor.shutdown();
    //     // Thread.sleep(2000);
    //     // closeSocket(socket);
    // }

}
