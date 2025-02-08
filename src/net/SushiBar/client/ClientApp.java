package net.SushiBar.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.InputMismatchException;
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

    int exit = 1;
    while (exit != 0) {
      try {
        String menu = clientInputStream.readUTF();
        if (!menu.isEmpty()) {
          System.out.println("********* Menú *********");
          System.out.println(menu);
          System.out.println("Pon tu pedido:");
          String order = scanner.nextLine();
          clientOutputStream.writeUTF(order);
          clientOutputStream.flush();
          String responseServer = clientInputStream.readUTF();
          System.out.println(responseServer);

          System.out.println("¿Quieres seguir pidiendo?");
          System.out.println("1. Sí, 0. No");
          exit = scanner.nextInt();
          scanner.nextLine();
        } else {
          System.out.println(
              "Ups! se ha acabado la comida, conectate mañana si gustas para seguir disfrutando de nuestros servicios!");
          System.exit(0);
        }

      } catch (InputMismatchException e) {
        System.out.println("Eso no es un número.");
        scanner.nextLine();
      } catch (SocketException e) {
        System.out.println("Restaurante a cerrado, nos vemos mañana !");
      }
    }
    socket.close();
    scanner.close();
    System.exit(0);
  }
}
