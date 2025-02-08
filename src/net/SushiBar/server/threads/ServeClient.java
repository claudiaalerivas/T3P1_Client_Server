package net.SushiBar.server.threads;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServeClient extends Thread{
  private DataInputStream clientInputStream;
  private DataOutputStream clientOutputStream;
  private String name;
  private static List<String> clientList = new ArrayList<>();

  public ServeClient() {
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    super.run();
  }
  
}
