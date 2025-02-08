package net.SushiBar.server;

import java.net.ServerSocket;
import net.SushiBar.utils.Constants;

public class ServerApp {
  public static void main(String[] args) throws Exception {
    ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);
    System.out.println("       Sushi Bar      ");
    System.out.println("******   OPEN   ******");
  }
}
