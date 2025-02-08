package net.SushiBar.server.threads;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServeClient extends Thread {
  private DataInputStream clientInputStream;
  private DataOutputStream clientOutputStream;
  private String name;
  private static List<String> clientList = new ArrayList<>();

  public ServeClient(Socket socket) {
    try {
      this.clientInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
      this.clientOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

      this.name = clientInputStream.readUTF();
    } catch (IOException e) {
      System.out.println("Error en el constructor de ServerClients.");
    }
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    super.run();
  }

}
