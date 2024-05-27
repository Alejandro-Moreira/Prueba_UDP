import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        DatagramSocket socket_cliente = null;
        try {
            InetAddress direccion_servidor = InetAddress.getByName("localhost");
            socket_cliente = new DatagramSocket();

            byte[] buffer_envio;
            byte[] buffer_recibido = new byte[1024];

            String mensajeInicial = "Hola servidor";
            buffer_envio = mensajeInicial.getBytes();
            DatagramPacket paquete_envio = new DatagramPacket(buffer_envio, buffer_envio.length, direccion_servidor, 5000);
            socket_cliente.send(paquete_envio);

            DatagramPacket paquete_recibido = new DatagramPacket(buffer_recibido, buffer_recibido.length);
            socket_cliente.receive(paquete_recibido);
            String mensajeRecibido = new String(paquete_recibido.getData(), 0, paquete_recibido.getLength());
            System.out.println("Mensaje del servidor: " + mensajeRecibido);

            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
            int puntajeTotal = 0;

            for (int i = 0; i < 5; i++) {
                paquete_recibido = new DatagramPacket(buffer_recibido, buffer_recibido.length);
                socket_cliente.receive(paquete_recibido);
                String pregunta = new String(paquete_recibido.getData(), 0, paquete_recibido.getLength());
                System.out.println("Preguntas de cultura general: " + pregunta);


                System.out.print("Ingrese su respuesta: ");
                String respuesta = entrada.readLine();
                buffer_envio = respuesta.getBytes();
                paquete_envio = new DatagramPacket(buffer_envio, buffer_envio.length, direccion_servidor, 5000);
                socket_cliente.send(paquete_envio);

                paquete_recibido = new DatagramPacket(buffer_recibido, buffer_recibido.length);
                socket_cliente.receive(paquete_recibido);
                String evaluacion = new String(paquete_recibido.getData(), 0, paquete_recibido.getLength());
                System.out.println("Evaluación: " + evaluacion);

                if (evaluacion.startsWith("Correcto")) {
                    puntajeTotal += 4; // Cada respuesta correcta suma 4 puntos
                }
            }

            paquete_recibido = new DatagramPacket(buffer_recibido, buffer_recibido.length);
            socket_cliente.receive(paquete_recibido);
            String mensajePuntaje = new String(paquete_recibido.getData(), 0, paquete_recibido.getLength());
            System.out.println("Puntaje final: " + mensajePuntaje);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket_cliente != null) {
                socket_cliente.close();
            }
        }
    }
}