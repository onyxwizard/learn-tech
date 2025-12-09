package Channels.serversocketchannel;

import java.io.*;
import java.net.*;

public class SimpleServer {
  public static void main(String[] args) throws IOException {
    try (ServerSocket server = new ServerSocket()) {
      server.bind(new InetSocketAddress("localhost", 9090));
      server.setSoTimeout(10_000); // accept() times out after 10s

      try (Socket client = server.accept();
          BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
          PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

        String msg = in.readLine();
        out.println("Echo: " + msg);
      }
    } // auto-closes everything
  }
}