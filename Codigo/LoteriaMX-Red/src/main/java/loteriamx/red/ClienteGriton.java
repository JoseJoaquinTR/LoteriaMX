package loteriamx.red;

import loteriamx.dominio.Carta;
import loteriamx.modelo.ModeloPartida;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClienteGriton {

    private final ModeloPartida modelo;
    private Socket socket;

    public ClienteGriton(ModeloPartida modelo) {
        this.modelo = modelo;
    }

    public void conectar(String host, int puerto) throws IOException {
        socket = new Socket(host, puerto);
        escucharSegundoPlano();
    }

    private void escucharSegundoPlano() {
        Thread hilo = new Thread(() -> {
            try (BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String linea;
                while ((linea = lector.readLine()) != null) {
                    procesaLinea(linea);
                }
            } catch (IOException e) {
                
            }
        });
        hilo.setDaemon(true);
        hilo.start();
    }

    private void procesaLinea(String linea) {
        if (linea.equals("FIN")) {
            modelo.updateCarta(null);
            return;
        }
        String[] partes = linea.split("\\|", -1);
        if (partes.length == 4 && partes[0].equals("CARTA")) {
            int numero = Integer.parseInt(partes[1]);
            String nombre = partes[2];
            String imagen = partes[3];
            if (imagen.isEmpty()) {
                imagen = null;
            }
            modelo.updateCarta(new Carta(numero, nombre, imagen));
        }
    }
}
