/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package PaquetePrincipal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author tibur
 */
public class AtenderCliente extends Thread {
    Socket socket;
    
    public AtenderCliente(Socket socket){
            this.socket = socket;
    }
    
    @Override
    public void run() {
            procesaPeticion(socket);
    }
    
    private static void procesaPeticion(Socket socket) {
        //variables locales
        String peticion = null, html = "", log_peticion = ""; 
        long inicio_procesamiento, tProcesado;
        File fichero;
        Scanner lector;
        PrintWriter escritor = null;
        InputStreamReader inSR;
        BufferedReader bufLeer = null;
        PrintWriter printWriter = null;
        String clienteAddress;
        
        //Inicio del procesamiento de la petición
        inicio_procesamiento = System.currentTimeMillis();
        clienteAddress = socket.getInetAddress().getHostAddress();
        if (clienteAddress.equals("0:0:0:0:0:0:0:1"))
            clienteAddress = "127.0.0.1";
        
        log_peticion = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").
                format(new Date())+ String.format(" %16s ",clienteAddress);

        try{
            //Flujo de entrada y salida del fichero
            fichero = new File("peticiones.log");
            if (!fichero.exists())
                fichero.createNewFile();
            lector = new Scanner(new FileReader(fichero));
            escritor = new PrintWriter(new FileWriter(fichero, true));
            
            //Flujo de entrada
            inSR = new InputStreamReader(socket.getInputStream());
            //espacio en memoria para la entrada de peticiones
            bufLeer = new BufferedReader(inSR);

            //objeto de java.io que entre otras características, permite escribir
            //'línea a línea' en un flujo de salida
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            //mensaje petición cliente
            peticion = bufLeer.readLine();
            //para compactar la petición y facilitar así su análisis, suprimimos todos
            //los espacios en blanco que contenga
            if(peticion == null) return;

            peticion = peticion.replaceAll(" ", "");
            //si realmente se trata de una petición 'GET' (que es la única que vamos a
            //implementar en nuestro Servidor)

            if (peticion.startsWith("GET")) {
                //extrae la subcadena entre 'GET' y 'HTTP/1.1'
                peticion = peticion.substring(3, peticion.lastIndexOf("HTTP"));

                //si corresponde a la página de inicio
                if (peticion.length() == 0 || peticion.equals("/") || peticion.equals("/inicio")) {
                    html = Paginas.html_menu + Paginas.html_index;
                    //sirve las cabeceras
                    printWriter.println(Mensajes.lineaInicial_OK);
                    printWriter.println(Paginas.primeraCabecera);
                    printWriter.println(Paginas.cabeceraFecha);
                    printWriter.println("Content-Length: " + html.getBytes().length);
                    printWriter.println();
                    //sirve el contenido
                    printWriter.println(html);
                    printWriter.flush();
                }
                //si corresponde a la página del Quijote
                else if (peticion.equals("/quijote")) {
                    html = Paginas.html_menu+ Paginas.html_quijote;
                    //sirve las cabeceras
                    printWriter.println(Mensajes.lineaInicial_OK);
                    printWriter.println(Paginas.primeraCabecera);
                    printWriter.println(Paginas.cabeceraFecha);
                    printWriter.println("Content-Length: " + html.getBytes().length);
                    printWriter.println();
                    //sirve el contenido
                    printWriter.println(html);
                    printWriter.flush();
                }
                //si corresponde a la página divisionPorCero
                else if (peticion.equals("/divisionPorCero")) {
                    try{
                        Integer.toString(1/0);
                    } catch(java.lang.ArithmeticException ex){
                        escritor.write(log_peticion+ex.toString()+" ("+peticion+")\n");
                        System.out.println("Aviso:" + peticion + " --> "+ ex.toString());
                        html = Paginas.html_menu + Paginas.html_divisionPorCero(ex);
                    }
                    //sirve las cabeceras
                    printWriter.println(Mensajes.lineaInicial_OK);
                    printWriter.println(Paginas.primeraCabecera);
                    printWriter.println(Paginas.cabeceraFecha);
                    printWriter.println("Content-Length: " + html.getBytes().length);
                    printWriter.println();
                    //sirve el contenido
                    printWriter.println(html);
                    printWriter.flush();
                }
                //si corresponde a la página TiempoTransmisionURL
                else if (peticion.equals("/tiempoTransmisionURL") ||
                        peticion.startsWith("/tiempoTransmisionURL?url=")) { 
                    String recurso;
                    URL url;
                    URLConnection conexion;
                    InputStream inputStream;
                    long tiempoCliente, tiempoServidor;
                            
                    if (peticion.startsWith("/tiempoTransmisionURL?url=")){
                        peticion = peticion.replaceAll("%2F", "/").
                                replaceAll("%3A", ":").replaceAll("%3F", "?");
                        recurso = "http://"+peticion.substring(26);
                    }
                    else
                        recurso = "http://www3.gobiernodecanarias.org"
                        + "/medusa/edublog/ieselrincon";
                    
                    try{
                        url = new URL(recurso);
                        conexion = url.openConnection();
                        conexion.connect();
                        inputStream = conexion.getInputStream();
                        while(inputStream.read()>-1){}
                        tiempoCliente = System.currentTimeMillis();
                        tiempoServidor = conexion.getDate();
                        html = Paginas.html_menu + Paginas.html_tiempoTransmisionURL(
                                recurso, url.getAuthority(), url.getFile(), 
                                tiempoCliente - tiempoServidor);
                    }catch(UnknownHostException ex){
                        escritor.write(log_peticion+"Aviso "+ex.toString()+" ("+peticion+")\n");
                        System.out.println("Aviso:" + peticion + " --> "+ ex.toString());
                        html = Paginas.html_menu + Paginas.html_tiempoTransmisionURL(
                                recurso, "No encontrado", "-", 0);
                    }catch (IllegalArgumentException ex){
                        escritor.write(log_peticion+"Aviso "+ex.toString()+" ("+peticion+")\n");
                        System.out.println("Aviso:" + peticion + " --> "+ ex.toString());
                        html = Paginas.html_menu + Paginas.html_tiempoTransmisionURL(
                                recurso, "No existe URL", "-", 0);
                    }catch (IOException ex){
                        escritor.write(log_peticion+"Aviso "+ex.toString()+" ("+peticion+")\n");
                        System.out.println("Aviso:" + peticion + " --> "+ ex.toString());
                        html = Paginas.html_menu + Paginas.html_tiempoTransmisionURL(
                                recurso, "Acceso denegado", "-", 0);
                    }
                    
                    //sirve las cabeceras
                    printWriter.println(Mensajes.lineaInicial_OK);
                    printWriter.println(Paginas.primeraCabecera);
                    printWriter.println(Paginas.cabeceraFecha);
                    printWriter.println("Content-Length: " + html.getBytes().length);
                    printWriter.println();
                    //sirve el contenido
                    printWriter.println(html);
                    printWriter.flush();
                }
                //si corresponde a la página inetAdress
                else if (peticion.equals("/inetAdress")) {
                    InetAddress localhost = InetAddress.getByName("localhost");
                    InetAddress lan = InetAddress.getLocalHost();
                    InetAddress web = InetAddress.getByName("www.ulpgc.es");
                    html = Paginas.html_menu 
                            + Paginas.html_inetAddress(localhost,lan, web);
                    //sirve las cabeceras
                    printWriter.println(Mensajes.lineaInicial_OK);
                    printWriter.println(Paginas.primeraCabecera);
                    printWriter.println(Paginas.cabeceraFecha);
                    printWriter.println("Content-Length: " + html.getBytes().length);
                    printWriter.println();
                    //sirve el contenido
                    printWriter.println(html);
                    printWriter.flush();
                }
                //si corresponde a la página mostrarFicheroLog
                else if (peticion.equals("/mostrarFicheroLog")) {
                    html = ""; 
                    while (lector.hasNextLine()){
                       html += lector.nextLine() + "<br>";
                    }
                    html = (html + log_peticion + "Procesando").
                            replaceAll(" ", "&nbsp");
                    html = Paginas.html_menu+ Paginas.html_mostrarFichero(html); 
                    //sirve las cabeceras
                    printWriter.println(Mensajes.lineaInicial_OK);
                    printWriter.println(Paginas.primeraCabecera);
                    printWriter.println(Paginas.cabeceraFecha);
                    printWriter.println("Content-Length: " + html.getBytes().length);
                    printWriter.println();
                    //sirve el contenido
                    printWriter.println(html);
                    printWriter.flush();
                }
                //si corresponde a la página descargarFicheroLog
                else if (peticion.equals("/descargarFicheroLog")) {
                    String texto = "";
                    String FLog = "peticiones.log";
                    while (lector.hasNextLine()){
                        texto += lector.nextLine()+ "\n" ;
                    }
                    //sirve las cabeceras
                    printWriter.println(Mensajes.lineaInicial_OK);
                    printWriter.println(Paginas.cabeceraFicheroAdjunto(FLog));
                    printWriter.println(Paginas.cabeceraFecha);
                    printWriter.println(
                            "Content-Length: " + texto.getBytes().length);
                    printWriter.println();
                    //sirve el contenido
                    printWriter.print(texto);
                    printWriter.flush();
                }
                //si corresponde al icono favicon
                else if (peticion.equals("/favicon.ico")) {
                    byte[] bytes = Files.readAllBytes(
                            Paths.get("images/favicon.ico"));
                    //sirve las cabeceras
                    printWriter.println(Mensajes.lineaInicial_OK);
                    printWriter.println("Content-Type:image/x-icon");
                    printWriter.println(Paginas.cabeceraFecha);
                    printWriter.println("Content-Length: " + bytes.length);
                    printWriter.println();
                    //sirve el contenido
                    socket.getOutputStream().write(bytes);
                    printWriter.flush();
                }  
                //si corresponde a imagenes png o jpg
                else if (peticion.endsWith(".png") || peticion.endsWith(".jpg")) {
                    byte[] bytes = Files.readAllBytes(
                            Paths.get("images"+peticion));
                    String tipo = peticion.substring(peticion.length()-3);
                    //sirve las cabeceras
                    printWriter.println(Mensajes.lineaInicial_OK);
                    printWriter.println("Content-Type:image/"+tipo);
                    printWriter.println(Paginas.cabeceraFecha);
                    printWriter.println("Content-Length: " + bytes.length);
                    printWriter.println();
                    //sirve el contenido
                    socket.getOutputStream().write(bytes);
                    printWriter.flush();
                }  
                //en cualquier otro caso
                else {
                    html = Paginas.html_menu + Paginas.html_noEncontrado;
                    peticion += "(No Existe)";
                    //sirve las cabeceras
                    printWriter.println(Mensajes.lineaInicial_NotFound);
                    printWriter.println(Paginas.primeraCabecera);
                    printWriter.println(Paginas.cabeceraFecha);
                    printWriter.println("Content-Length: " + html.getBytes().length);
                    printWriter.println();
                    //sirve el contenido
                    printWriter.println(html);
                    printWriter.flush();
                }
                
                //Finalizamos la petición
                tProcesado = System.currentTimeMillis() - inicio_procesamiento;
                escritor.write(log_peticion + "Procesada ("+ tProcesado +" ms) " 
                        + peticion + "\n");
                System.out.println(clienteAddress+" "+ "Procesada: "+peticion);
                escritor.close();
                lector.close();
                printWriter.close();
                bufLeer.close();
                socket.close();
            }
        }catch (Exception ex){
            printWriter.println(Mensajes.lineaInicial_InternalServerError);
            printWriter.println();
            escritor.write(log_peticion+"Error "+ex.toString()+" ("+peticion+")\n");
            escritor.write(log_peticion + "Abortada "+ peticion + "\n");
            System.out.println("Error:" + peticion + " --> "+ ex.toString());
            System.out.println(clienteAddress+" "+ "Abortada: "+peticion);
            escritor.close();
            printWriter.close();
        }
    }
}
