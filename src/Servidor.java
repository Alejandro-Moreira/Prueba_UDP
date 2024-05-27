import java.io.*;
import java.net.*;

public class Servidor {
    private static final String[][] preguntasRespuestas = {
            {"Cuál es el océano más grande?", "Pacífico"},
            {"Cuál es el país con más copas del mundo?", "Brasil"},
            {"Cuántas estaciones tiene el año?", "cuatro"},
            {"Cuál es el país más grande del mundo?", "Rusia"},
            {"Cuál es la región conocida como litoral?", "Costa"}
    };

    public static void main(String[] args) {
        DatagramSocket socket_servidor = null;
        try {
            socket_servidor = new DatagramSocket(5000);
            System.out.println("Servidor esperando conexiones...");

            while (true) {
                byte[] buffer_recibido = new byte[1024];
                DatagramPacket paquete_recibido = new DatagramPacket(buffer_recibido, buffer_recibido.length);
                socket_servidor.receive(paquete_recibido);

                InetAddress direccion_cliente = paquete_recibido.getAddress();
                int puerto_cliente = paquete_recibido.getPort();

                System.out.println("Cliente conectado: " + direccion_cliente.getHostAddress() + ":" + puerto_cliente);

                HiloCliente hilo = new HiloCliente(socket_servidor, paquete_recibido, preguntasRespuestas);
                hilo.start();
                hilo.join();
            }
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (socket_servidor != null) {
                socket_servidor.close();
            }
        }
    }
}