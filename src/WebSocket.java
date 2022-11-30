import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebSocket implements Runnable {

    private static String url;

    private static String[] urls;

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
        }
        else if (urls != null) {
            for(int i = 0; i < urls.length; i++) {
                urls[i] = urls[i].replace("http://", "");
                urls[i] = urls[i].replace("https://", "");
            }
        }
    }

    public static Socket createSocket(String url) throws IOException {
        Socket socket = new Socket(host(url), 80);
        socket.setSoTimeout(10000);
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

    private static byte[] header(InputStream is) throws IOException, InterruptedException {
        byte[] header = new byte[2048];
        int offset = 0;
        while (true) {
            try {
                int read = is.read(header, offset, 1);
                if (read == -1) {
                    throw new IOException("Connection closed");
                }
                if (header[offset] == (byte) '\n') {
                    if (
                        offset >= 3 &&
                        header[offset - 1] == (byte) '\r' &&
                        header[offset - 2] == (byte) '\n' &&
                        header[offset - 3] == (byte) '\r'
                    ) break;
                }
                offset += read;
            } catch (SocketTimeoutException e) {
                System.out.println("Socket timed out");
                break;
            }
        }
        return header;
    }

    private static byte[] content(DataInputStream is, String header) throws IOException {
        int contentLength = Integer.parseInt(
            header.split("Content-Length: ")[1].split("\r\n")[0]
        );
        System.out.println("Content length: " + contentLength);

        byte[] content = new byte[contentLength];
        int offset = 0;
        while (offset < contentLength) {
            try{
                int read = is.read(content, offset, contentLength - offset);
                if (read == -1) {
                    throw new IOException("Connection closed");
                }
                offset += read;
            } catch (SocketTimeoutException e) {
                System.out.println("Socket timed out");
                break;
            }
        }
        return content;
    }

    public static void Request(PrintStream ps, String url, String fileName) {
        if(isSubfolder(url)) {
            ps.print("GET " + get(url) + fileName + " HTTP/1.1\r\n");
            System.out.println("GET " + get(url) + fileName + " HTTP/1.1");
        }
        else{
            ps.print("GET " + get(url) + " HTTP/1.1\r\n");
            System.out.println("GET " + get(url) + " HTTP/1.1");
        }
        ps.print("Host:" + host(url) + "\r\n");
        System.out.println("Host: " + host(url));
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

    public static void Download(DataInputStream is, PrintStream ps, String fileName, String url) throws IOException, InterruptedException {

        // Initialize file
        File file = new File(path(url, fileName));

        // Initialize stream
        System.out.println("Downloading " + fileName + "...");

        // Send request to server
        Request(ps, url, fileName);
    
        //Read header
        String header = new String(header(is));

        System.out.println(header);
        
        if(header.contains("HTTP/1.1 200 OK")) {
            FileOutputStream fos = new FileOutputStream(file);
            if (header.contains("Content-Length: ")) {
                downloadContentLength(fos, is, header);
            }
            else if (header.contains("Transfer-Encoding: chunked")) {
                downloadChunked(fos, is);
            }
            System.out.println("Downloaded " + fileName + "!");   
        }
        else if(header.contains("HTTP/1.1 301 Moved Permanently")) {
            String newUrl = header.split("Location: ")[ 1 ].split("\r\n")[0];
            System.out.println("Redirecting to " + newUrl);
            Download(is, ps, fileName, newUrl);
        }
        else if(header.contains("HTTP/1.1 404 Not Found")) {
            System.out.println("File not found");
            return ;
        }
        else {
            System.out.println("Unknown error");
            return ;
        }    
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

    public static void DownloadFolderWithSingleSocket(Socket socket) throws IOException, InterruptedException{
        
        File folder = new File("./binary/" + host(url) + "_" + fileName(url));
        folder.mkdirs();
        
        // Send GET request
        PrintStream ps = new PrintStream(socket.getOutputStream());
        Request(ps, url, "");
    
        DataInputStream is = new DataInputStream(socket.getInputStream());

        //Read header
        String header = new String(header(is));
        String body = new String(content(is, header));
        ArrayList<String> files = getFileFromFolder(body);

        try{
            for(String file : files){
                Download(is, ps, file, url);
                System.out.println("File " + url + file + " downloaded!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void DownloadFolderWithMultipleSocket(Socket socket) throws IOException, InterruptedException{
        
        File folder = new File("./binary/" + host(url) + "_" + fileName(url));
        folder.mkdirs();
        
        // Send GET request
        PrintStream ps = new PrintStream(socket.getOutputStream());
        Request(ps, url, "");
    
        
        DataInputStream is = new DataInputStream(socket.getInputStream());
        //Read header
        String header = new String(header(is));
        String body = new String(content(is, header));
        System.out.println(body);
        ArrayList<String> files = getFileFromFolder(body);

        // multiple sockets
        for(String file : files){
            try(Socket s = new Socket(host(url), 80)){
                Download(is, ps, file, url);
                System.out.println("File " + url + file + " downloaded!");
                closeSocket(s);
            }
        }
    }

    public void download() throws IOException, InterruptedException {
        ReplaceUrl();
        Socket socket = createSocket(url);
        System.out.println(url);
        DataInputStream is = new DataInputStream(socket.getInputStream());
        PrintStream ps = new PrintStream(socket.getOutputStream());
        if (isSubfolder(url)) {
            System.out.println("Download folder");
            DownloadFolderWithSingleSocket(socket);
        } else {
            System.out.println("Download file");
            Download(is, ps, fileName(url), url);
        }
        is.close();
        ps.close();
        closeSocket(socket);
        System.out.println("Closed socket");
    }

    public void downloadUrlsWithThread() throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(urls.length);
        for(String url: urls){
            System.out.println(url);
            executor.execute(new WebSocket(url));
        }
        executor.shutdown();
    }

    public void run(){
        try {
            ReplaceUrl();
            Socket socket = createSocket(url);
            System.out.println(url);
            DataInputStream is = new DataInputStream(socket.getInputStream());
            PrintStream ps = new PrintStream(socket.getOutputStream());

            if (isSubfolder(url)) {
                System.out.println("Download folder");
                DownloadFolderWithSingleSocket(socket);
            } else {
                System.out.println("Download file");
                Download(is, ps, fileName(url), url);
            }
            is.close();
            ps.close();
            closeSocket(socket);
            System.out.println("Da dong socket");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
