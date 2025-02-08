package net.SushiBar.server;

import java.net.ServerSocket;
import java.net.Socket;

import net.SushiBar.server.threads.ServeClient;
import net.SushiBar.utils.Constants;

public class ServerApp {
  public static void main(String[] args) throws Exception {
    ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);
    System.out.println("       Sushi Bar      ");
    System.out.println("******   OPEN   ******");

    while (true) {
      Socket clientSocket = serverSocket.accept();
      System.out.println("Ha entrado un cliente al restaurante");
      ServeClient clientHandler = new ServeClient(clientSocket);
      clientHandler.start();
    }
  }
}
