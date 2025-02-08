package net.SushiBar.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

import net.SushiBar.utils.Constants;

public class ClientApp {
  public static void main(String[] args) throws Exception {
    Socket socket = new Socket("localhost", Constants.SERVER_PORT);

    DataOutputStream clientOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    DataInputStream clientInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    Scanner scanner = new Scanner(System.in);

    System.out.println("Bienvenido a Sushi bar!");
    System.out.println("Introduce tu nombre para ver la carta:");
    String name = scanner.nextLine();
  }
}
