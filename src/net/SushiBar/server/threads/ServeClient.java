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

      addFood();
      this.name = clientInputStream.readUTF();
      sendMenu();
    } catch (IOException e) {
      System.out.println("Error en el constructor de ServerClients.");
    }
  }


  public static synchronized void removeFood(String order) {
    clientList.removeIf(dish -> dish.equalsIgnoreCase(order.trim().toLowerCase()));
  }

  public static void addFood() {
    clientList.clear();
    clientList.add("Ramen");
    clientList.add("Maki");
    clientList.add("Nigiri");
    clientList.add("Takoyaki");
    clientList.add("Sashimi");
    clientList.add("Gunkan");
    clientList.add("Futomaki");
    clientList.add("Futomaki");
  }

  public synchronized void sendMenu() {
    try {
      StringBuilder menu = new StringBuilder();
      if (!clientList.isEmpty()) {
        for (String plato : clientList) {
          menu.append(plato).append("\n");
        }
        clientOutputStream.writeUTF(menu.toString());
        clientOutputStream.flush();
      } else {
        clientOutputStream.writeUTF("");
        clientOutputStream.flush();
      }
      
    } catch (IOException e) {
      System.out.println("Error enviando el menú al cliente " + this.name);
    }
  } 


  @Override
  public void run() {
    // TODO Auto-generated method stub
    super.run();
  }

}
