import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPPeer {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("¿Este peer actuará como servidor? (s/n): ");
        String tipo = scanner.nextLine();

        if (tipo.equalsIgnoreCase("s")) {
            System.out.print("Puerto local (para escuchar conexiones): ");
            int puerto = scanner.nextInt();
            scanner.nextLine();

            ServerSocket serverSocket = new ServerSocket(puerto);
            System.out.println("Esperando conexión en puerto " + puerto + "...");
            Socket socket = serverSocket.accept();
            System.out.println("Conexión aceptada desde " + socket.getInetAddress());

            manejarConexion(socket);
        } else {
            System.out.print("Ingresa IP del servidor: ");
            String ip = scanner.nextLine();
            System.out.print("Puerto del servidor: ");
            int puerto = scanner.nextInt();
            scanner.nextLine();

            Socket socket = new Socket(ip, puerto);
            System.out.println("Conectado a " + socket.getInetAddress());

            manejarConexion(socket);
        }
    }

    public static void manejarConexion(Socket socket) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Hilo receptor
        Thread receptor = new Thread(() -> {
            try {
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String mensaje;
                while ((mensaje = entrada.readLine()) != null) {
                    System.out.println("\n[Mensaje recibido]: " + mensaje);
                }
            } catch (IOException e) {
                System.out.println("Error al recibir: " + e.getMessage());
            }
        });
        receptor.setDaemon(true);
        receptor.start();

        // Envío
        PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("=== Puedes escribir mensajes para enviar ===");
        while (true) {
            System.out.print("> ");
            String mensaje = scanner.nextLine();
            salida.println(mensaje);
        }
    }
}

