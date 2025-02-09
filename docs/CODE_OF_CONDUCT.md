# ¡Bienvenido al código fuente del programa!

En este documento podrás visualizar el como esta construido el programa a total detalle.

# Clase Server

Esta clase inicia el servidor y gestiona las conexiones entrantes de los clientes.

```
  public class ServerApp {
    public static void main(String[] args) throws Exception {
      ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);
      System.out.println("       Sushi Bar      ");
      System.out.println("******   OPEN   ******");

      ServeClient.addFood();
      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Ha entrado un cliente al restaurante");
        ServeClient clientHandler = new ServeClient(clientSocket);
        clientHandler.start();
      }
    }
  }
```

# Funcionamiento

  >  - Crea un ServerSocket en el puerto especificado y espera conexiones entrantes.
  >  - Acepta clientes y genera un hilo (ServerClients) para manejar cada conexión de forma independiente.
  > - Permite concurrencia mediante el uso de hilos, evitando bloqueos en la atención de nuevos clientes.

# Clase ServerClients

Esta clase maneja la comunicación con un cliente dentro de un hilo independiente.

```
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
```
## Variables

  >  - **DataInputStream** permite leer datos primitivos y cadenas de texto desde un flujo de entrada en un formato binario estructurado. Se utiliza para recibir datos enviados por el cliente o el servidor.
  >  - **DataOutputStream** se utiliza para enviar datos en un formato binario estructurado a través de un flujo de salida. En este caso, permite enviar respuestas al cliente.
  > - **Name** Permite guardar el nombre del cliento una vez conectado
  > - **clientList** Lista de platos ofrecidos en el restaurante. 

# Métodos 

### addFood(): 
Inicializa la lista de platos disponibles.

```
  public static void addFood() {
    clientList.clear();
    clientList.add("Ramen");
    clientList.add("Maki");
    clientList.add("Nigiri");
    clientList.add("Takoyaki");
    clientList.add("Sashimi");
    clientList.add("Gunkan");
    clientList.add("Futomaki");
  }

```

### removeFood(String order): 

Elimina un plato si está disponible.

```
  public static synchronized void removeFood(String order) {
      clientList.removeIf(dish -> dish.equalsIgnoreCase(order.trim()));
    }
```

### sendMenu(): 

Solo si la lista tiene informacion envía el menú al cliente usando el String Builder de java. 

```
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
```

### run(): 

Método ejecutable del hilo, es el encargado de la ejecucion de gran parte de la comunicacion entre el servidor y el cliente, enviando el menu, recibiendo los pedidos de los clientes removiendo ese pedido de la lista de platos disponibles en el restaurante, controlando errores en el caso de que el pedido del usuario estuviera mal y controlando excepciones en el caso de que el cliente se desconectase. 

```
  @Override
    public void run() {
      try {
        sendMenu();
        while (true) {
          String order = clientInputStream.readUTF();

          if (!clientList.contains(order.trim())) {
            clientOutputStream.writeUTF("ERROR: El plato no existe. Intente de nuevo.");
            clientOutputStream.flush();
            sendMenu();
          } else {
            System.out.println(this.name + " ha pedido: " + order);
            removeFood(order);
            clientOutputStream.writeUTF("- Pedido confirmado: " + order);
            clientOutputStream.flush();
            sendMenu();
          }
        }
      } catch (IOException ioe) {
        System.out.println("El cliente " + this.name + " se ha marchado ...");
      }
    }

```

# Clase ClientApp

La clase ClientApp simula el comportamiento de un cliente que se conecta a un servidor para realizar pedidos en un restaurante virtual.

## Funcionalidad
Esta clase realiza las siguientes tareas:
  >  - Conexión al servidor: Establece una conexión con el servidor utilizando un socket para la comunicación.
  >  - Envía datos al servidor: El cliente envía su nombre y los pedidos realizados al servidor.
  > - Interacción con el menú: El cliente visualiza el menú enviado por el servidor y realiza pedidos.
  > - Control de flujo de pedidos: Después de cada pedido, el cliente tiene la opción de seguir pidiendo más platos o finalizar la interacción.

Manejo de excepciones:
  > - Errores de entrada: Si el cliente no introduce un número válido al preguntar si desea seguir pidiendo, se captura una excepción (InputMismatchException).
  > - Desconexión del servidor: Si el servidor se desconecta o cierra la conexión, se captura la excepción SocketException.
  > - Lista vacía: Si no hay más platos disponibles, el cliente recibe un mensaje de despedida y la aplicación se cierra.

Código: 
```
public class ClientApp {
  public static void main(String[] args) throws Exception {
    Socket socket = new Socket("localhost", Constants.SERVER_PORT);

    DataOutputStream clientOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    DataInputStream clientInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    Scanner scanner = new Scanner(System.in);

    System.out.println("Bienvenido a Sushi bar!");
    System.out.println("Introduce tu nombre para ver la carta:");
    String name = scanner.nextLine();
    clientOutputStream.writeUTF(name);
    clientOutputStream.flush();

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
```



