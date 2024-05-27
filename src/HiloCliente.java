import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HiloCliente extends Thread {

    private DatagramSocket socket_servidor;
    private DatagramPacket paquete_recibido;
    private String[][] preguntasRespuestas;

    public HiloCliente(DatagramSocket socket, DatagramPacket paquete, String[][] preguntasRespuestas) {
        this.socket_servidor = socket;
        this.paquete_recibido = paquete;
        this.preguntasRespuestas = preguntasRespuestas;
    }

    public void run() {
        try {
            InetAddress direccion_cliente = paquete_recibido.getAddress();
            int puerto_cliente = paquete_recibido.getPort();

            String mensajeInicial = "Preguntas";
            byte[] buffer_envio = mensajeInicial.getBytes();
            DatagramPacket paquete_envio = new DatagramPacket(buffer_envio, buffer_envio.length, direccion_cliente, puerto_cliente);
            socket_servidor.send(paquete_envio);
            int puntaje = 0;

            for (int i = 0; i < preguntasRespuestas.length; i++) {
                String pregunta = preguntasRespuestas[i][0];
                buffer_envio = pregunta.getBytes();
                paquete_envio = new DatagramPacket(buffer_envio, buffer_envio.length, direccion_cliente, puerto_cliente);
                socket_servidor.send(paquete_envio);

                byte[] buffer_recibido = new byte[1024];
                paquete_recibido = new DatagramPacket(buffer_recibido, buffer_recibido.length);
                socket_servidor.receive(paquete_recibido);
                String respuestaCliente = new String(paquete_recibido.getData(), 0, paquete_recibido.getLength());
                System.out.println("Respuesta: " + respuestaCliente);

                String respuestaCorrecta = preguntasRespuestas[i][1];
                String evaluacion;
                if (respuestaCliente.equalsIgnoreCase(respuestaCorrecta)) {
                    evaluacion = "Correcto";
                    puntaje += 4;
                } else {
                    evaluacion = "Incorrecto. La respuesta correcta es: " + respuestaCorrecta;
                }

                buffer_envio = evaluacion.getBytes();
                paquete_envio = new DatagramPacket(buffer_envio, buffer_envio.length, direccion_cliente, puerto_cliente);
                socket_servidor.send(paquete_envio);
            }

            String mensajePuntaje = "Puntaje final: " + puntaje + "/" + preguntasRespuestas.length * 4;
            buffer_envio = mensajePuntaje.getBytes();
            paquete_envio = new DatagramPacket(buffer_envio, buffer_envio.length, direccion_cliente, puerto_cliente);
            socket_servidor.send(paquete_envio);

            System.out.println("Cliente desconectado.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}