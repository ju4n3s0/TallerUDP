import java.net.*;
import java.util.Scanner;

public class Peer {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Leer datos del peer local
        System.out.print("Ingresa el puerto local: ");
        int localPort = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer

        // Leer datos del peer remoto
        System.out.print("Ingresa la IP destino del peer: ");
        String ipDestino = scanner.nextLine();

        System.out.print("Ingresa el puerto destino del peer: ");
        int puertoDestino = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer

        DatagramSocket socket = new DatagramSocket(localPort);

        // Hilo para recibir mensajes
        Thread receptor = new Thread(() -> {
            byte[] buffer = new byte[1024];
            while (true) {
                try {
                    DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                    socket.receive(paquete);
                    String mensaje = new String(paquete.getData(), 0, paquete.getLength());
                    System.out.println("\n[Mensaje recibido de " + paquete.getAddress() + "]: " + mensaje);
                } catch (Exception e) {
                    System.out.println("Error al recibir: " + e.getMessage());
                }
            }
        });
        receptor.setDaemon(true);
        receptor.start();

        // Bucle de envío
        System.out.println("=== Puedes escribir mensajes para enviar ===");
        while (true) {
            System.out.print("> ");
            String mensaje = scanner.nextLine();
            byte[] datos = mensaje.getBytes();
            DatagramPacket paquete = new DatagramPacket(datos, datos.length,
                    InetAddress.getByName(ipDestino), puertoDestino);
            socket.send(paquete);
        }
    }
}
