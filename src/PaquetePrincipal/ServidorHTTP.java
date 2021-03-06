package PaquetePrincipal;

import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * *****************************************************************************
 * Servidor HTTP que atiende peticiones de tipo 'GET' recibidas por el puerto
 * 8066
 *
 * NOTA: para probar este código, comprueba primero de que no tienes ningún otro
 * servicio por el puerto 8066 (por ejemplo, con el comando 'netstat' si estás
 * utilizando Windows)
 *
 * @author IMCG
 */
class ServidorHTTP {
    
    /**
     * **************************************************************************
     * procedimiento principal que asigna a cada petición entrante un socket
     * cliente, por donde se enviará la respuesta una vez procesada
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        
        //Asociamos al servidor el puerto 8066
        ServerSocket socServidor = new ServerSocket(8066);
        imprimeDisponible();
        Socket socCliente;
        
        //ante una petición entrante, procesa la petición por el socket cliente
        //por donde la recibe
        while (true) {
            //a la espera de peticiones
            try{
                socCliente = socServidor.accept();
                //atiendo un cliente
                //System.out.println("Atendiendo al cliente");
                new AtenderCliente(socCliente).start();
                //cierra la conexión entrante
                //socCliente.close();
                //System.out.println("cliente atendido");
            }catch(IOException ex){
            }
        }
    }
    
    /**
     *****************************************************************************
     * procesa la petición recibida
     *
     * @throws IOException
     */
    
    /**
     * **************************************************************************
     * muestra un mensaje en la Salida que confirma el arranque, y da algunas
     * indicaciones posteriores
     */
    private static void imprimeDisponible() {
        
        System.out.println("El Servidor WEB se está ejecutando y permanece a la "
                + "escucha por el puerto 8066.\nEscribe en la barra de direcciones "
                + "de tu explorador preferido:\n\nhttp://localhost:8066\npara "
                + "solicitar la página de bienvenida\n\nhttp://localhost:8066/"
                + "quijote\n para solicitar una página del Quijote,\n\nhttp://"
                + "localhost:8066/q\n para simular un error\n\n"
                + "---------------------- ACTIVIDAD REGISTRADA ----------------------\n");
        
    }
}
