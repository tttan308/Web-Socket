import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class WebSocket implements Runnable{
    private static String url;

    public WebSocket(String url) {
        WebSocket.url = url;
    }

    public static void ReplaceUrl() {
        if(url.contains("https://")) url = url.replace("https://", "");
        else if(url.contains("http://")) url = url.replace("http://", "");
    }

    public static Socket createSocket() throws IOException {
        ReplaceUrl();
        Socket socket = new Socket(host(), 80);
        return socket;
    }

    public static void closeSocket(Socket socket) throws IOException {
        socket.close();
    }

    private static boolean isRoot() {
        return (url.length() - url.replace("/", "").length() == 1 && url.charAt(url.length() - 1) == '/') || (url.length() - url.replace("/", "").length() == 0);
    }

    private static boolean isSubfolder() {
        return !isRoot() && url.length() - url.replace(".", "").length() <= 2 && url.charAt(url.length() - 1) == '/';
    }

    private static String host() {
        return url.split("/")[0];
    }

    private static String get() {
        if (isRoot()) return "/";
        return url.substring(url.indexOf("/", 8));
    }

    private static String path(String fileName) {
        if (isRoot() || (!get().contains(".") && get().charAt(get().length()-1) != '/')) return "./binary/" + host() + "_" + "index.html";
        if (isSubfolder()) return (
            "./binary/" + host() + "_" + fileName() + "/" + host() + "_" + fileName
            );
        return (
            "./binary/" + host() + "_" + url.substring(url.lastIndexOf("/") + 1)
        );
    }

    private static String fileName() {
        if (isRoot()) return "index.html";
        return url.split("/")[url.split("/").length - 1];
    }

    private static byte[] header(InputStream is) throws IOException {
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
        return header;
    }

    private static byte[] content(InputStream is, String header) throws IOException {
        int contentLength = Integer.parseInt(
            header.split("Content-Length: ")[1].split("\r\n")[0]
        );
        byte[] content = new byte[contentLength];
        int offset = 0;

        while (offset < contentLength) {
            int cnt = is.read(content, offset, contentLength - offset);
            offset += cnt;
        }
        return content;
    }

    public static void Download(Socket socket, String fileName) throws IOException {

        InputStream is = socket.getInputStream();
        PrintStream ps = new PrintStream(socket.getOutputStream());

        // Initialize file
        File file = new File(path(fileName));
        // Initialize stream
        System.out.println("Downloading " + fileName + "...");
        FileOutputStream fos = new FileOutputStream(file);
        // Send request to server
        if(isSubfolder()) {
            ps.print("GET " + get() + fileName + " HTTP/1.1\r\n");
            System.out.println("GET " + get() + fileName + " HTTP/1.1");
        }
        else{
            ps.print("GET " + get() + " HTTP/1.1\r\n");
            System.out.println("GET " + get() + " HTTP/1.1");
        }
        System.out.println("Host: " + host());
        ps.print("Host: " + host() + "\r\n");
        // ps.print("Connection: Keep-Alive\r\n");
        ps.print("\r\n");
        ps.flush();

        //Read header
        String header = new String(header(is));
        if (header.contains("Content-Length")) {
            byte[] content = content(is, header);
            fos.write(content);
        } else if(header.contains("Transfer-Encoding: chunked")) {
            // Read chunk
            System.out.println("Chunk");
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
        }

        System.out.println("Downloaded " + fileName + "!");
        
        if(header.contains("Connection: close")) closeSocket(socket);
        
        //close file
        fos.close();
    }

    public static void DownloadFolder(Socket socket) throws IOException{
        InputStream is = socket.getInputStream();
        PrintStream ps = new PrintStream(socket.getOutputStream());
        
        ArrayList<String> files = new ArrayList<String>();
        File folder = new File("./binary/" + host() + "_" + fileName());
        folder.mkdirs();

        // Send GET request
        ps.print("GET " + get() + " HTTP/1.1\r\n");
        ps.print("Host: " + host() + "\r\n");
        ps.print("Connection: Keep-Alive\r\n");
        ps.print("\r\n");
        ps.flush();

        //Read header
        String header = new String(header(is));
        System.out.println(header);
        //Read body with content length
        String body = new String(content(is, header));
        for (int i = 0; i < body.length(); i++) {
            if (
                body.charAt(i) == 'h' &&
                body.charAt(i + 1) == 'r' &&
                body.charAt(i + 2) == 'e' &&
                body.charAt(i + 3) == 'f' &&
                body.charAt(i + 4) == '='
                ) {
                int j = i + 6;
                while (body.charAt(j) != '"') j++;
                String fileName = body.substring(i + 6, j);
                if (fileName.length() - fileName.replace(".", "").length() > 0) files.add(fileName);
            }
        }
        

        // multiple sockets
        // for(String file : files){
        //     try(Socket s = createSocket()){
        //         Download(s, file);
        //         System.out.println("File " + url + file + " downloaded!");
        //         closeSocket(s);
        //     }
        // }

        // single socket
        for(String file : files){
            Download(socket, file);
        }
    }

    public void download() throws IOException {
        Socket socket = createSocket();
        if (isSubfolder()) {
            System.out.println("folder");
            DownloadFolder(socket);
        } else {
            System.out.println("Download file");
            Download(socket, fileName());
        }
        closeSocket(socket);
    }

    @Override
    public void run() {
        try {
            download();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
