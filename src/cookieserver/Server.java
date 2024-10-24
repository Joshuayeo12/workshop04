package cookieserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException; //(throws input and output exceptions)
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // args[0] ==> port number
    // args[1] ==> directory path
    public static void main(String[] args) throws NumberFormatException, IOException {

        String portNumber = "";
        String dirPath = "";
        String fileName = "";
        if (args.length > 0) {
            // 1st arugment is the directory
            portNumber = args[0];
            dirPath = args[1];
            fileName= args[2];
        } else {
            System.err.println("Invalid number of arguments expected");
            System.exit(0);

        }

        //Here help
        //create a new File obeject for dirPath
        //if file doesnt exist mkdir creates it
        File newDirectory = new File(dirPath);
        if (!newDirectory.exists()) {
            newDirectory.mkdirs();
        }

        //Here help
        //read and print cookies
        //reads based on the directory provided
        Cookie c = new Cookie();
        c.readCookieFile(dirPath + File.separator + fileName);
        // c.printCookies();

        // day4 slide 8
        ServerSocket ss = new ServerSocket(Integer.parseInt(portNumber));
        Socket s = ss.accept();
        System.out.printf("\r\nWebsocket server started on port... %s\r\n", portNumber);

        // day4 slide 9
        try {
            InputStream is = s.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            String messageRecieved = "";

            OutputStream os = s.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);

            
            // day 04 - slide 9
            //ensures program is running until client sends "quit"
            //readUTF is how the server read from client
            while (!messageRecieved.toLowerCase().equals("quit")) {
                System.out.println("Waiting for client input");
                messageRecieved = dis.readUTF(dis);

                // do something to serve the cookie, pick random cookie
                String retrievedCookie = c.getRandomCookie();

                //put it to the DataOutputStream to send back to client
                dos.writeUTF(retrievedCookie);
                dos.flush();
            }

            dos.close();
            bos.close();
            os.close();

            dis.close();
            bis.close();

            //here help
        } catch (EOFException ex) {
            System.err.println(ex.toString());

        } finally {

        }
    }
}
