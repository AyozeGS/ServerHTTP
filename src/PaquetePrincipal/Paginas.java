package PaquetePrincipal;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.net.InetAddress;

/**
 * ****************************************************************************
 * clase no instanciable donde se definen algunos valores finales
 *
 * @author IMCG
 */
public class Paginas {
    
    public static String primeraCabecera =
            "Content-Type:text/html;charset=UTF-8";
            
    public static String cabeceraFecha =
            "Date:"+DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z")
                    .withZone(ZoneId.of("GMT")).format(LocalDateTime.now())+"";       

    public static String cabeceraFicheroAdjunto (String nombre){
            return "Content-Type:text/plain\n" +
            "Content-Disposition: attachment; filename="+nombre;
    }

    //contenido menu
    public static String html_menu = "<html>"
            + "<body><ul>"
            + "<li><a href='inicio'>Inicio</a></li>"
            + "<li><a href='quijote'>El quijote</a></li>"
            + "<li><a href='divisionPorCero'>Divisi&oacute;n por cero</a></li>"
            + "<li><a href='tiempoTransmisionURL'>Tiempo de transmisi&oacute;n "
            + "de una URL</a></li>"
            + "<li><a href='inetAdress'>Informaci&oacute;n de inetAdress</a></li>"
            + "<li><a href='mostrarFicheroLog'>Mostrar el contenido del fichero "
            + "log</a></li>"
            + "<li><a href='descargarFicheroLog'>Descargar el fichero log</a></li>"
            + "</ul></html>";
    
    //contenido index
    public static String html_index = "<html>"
            + "<head><title>index</title></head>"
            + "<body>"
            + "<h1>&iexcl;Enhorabuena!</h1>"
            + "<p>Tu servidor HTTP m&iacute;nimo funciona correctamente</p>"
            + "</body>"
            + "</html>";
    
    //contenido quijote
    public static String html_quijote = "<html>"
            + "<head>"
            + "<title>quijote</title>"
            + "<link rel='icon' type='image/png' "
            + "href='/faviconGreen.png' sizes='16x16'></link>"
            + "</head>"
            + "<body>"
            + "<h1>As&iacute; comienza el Quijote</h1>"
            + "<div style='background-image: url(\"fondoLibro.jpg\"); "
            + "background-repeat:no-repeat; background-size:100% 100%;'>"
            + "<p  style='padding: 80px; text-align:justify; "
            + "font-family: verdana; font-style: oblique'><b>"
            + "En un lugar de la Mancha, de cuyo nombre no quiero acordarme, "
            + "no ha mucho tiempo que viv&iacute;a un hidalgo de los de lanza "
            + "en astillero, adarga antigua, roc&iacute;n flaco y galgo "
            + "corredor. Una olla de algo m&aacute;s vaca que carnero, "
            + "salpic&oacute;n las m&aacute;s noches, duelos y quebrantos "
            + "(huevos con tocino) los s&aacute;bados, lentejas los viernes, "
            + "alg&uacute;n palomino de a&ntilde;adidura los domingos, "
            + "consum&iacute;an las tres partes de su hacienda. El resto della "
            + "conclu&iacute;an sayo de velarte (traje de pa&ntilde;o fino), "
            + "calzas de velludo (terciopelo) para las fiestas con sus "
            + "pantuflos de lo mismo, y los d&iacute;as de entresemana se "
            + "honraba con su vellor&iacute; (pardo de pa&ntilde;o) de lo "
            + "m&aacute;s fino. Ten&iacute;a en su casa una ama que pasaba de "
            + "los cuarenta, y una sobrina que no llegaba a los veinte, y un "
            + "mozo de campo y plaza, que as&iacute; ensillaba el roc&iacute;n "
            + "como tomaba la podadera..."
            + "</b></p></div>"
            + "</body>"
            + "</html>";
    
    
    //contenido divisionPorCero
    public static String html_divisionPorCero (Exception e){
        return "<html>"
                + "<head><title>divis&oacute;nPorCero</title>"
                + "<link rel='icon' type='image/png' "
                + "href='/faviconOrange.png' sizes='16x16'></link>"
                + "</head>"
                + "<body>"
                + "<h1>Divis&oacuten por cero</h1>"
                + "<p style='color:red'>"+e.getMessage()+"</p>"
                + "</body>"
                + "</html>";
    }
    
    //contenido tiempoTransmisionURL
    public static String html_tiempoTransmisionURL (String url,
            String autoridad, String recurso, long tiempo){
        return "<html>"
                + "<head><title>tiempoTransmisi&oacute;nURL</title>"
                + "<style>table, th, td {border: 1px solid black; "
                + "border-collapse: collapse;}</style>"
                + "</head><body>"
                + "<h1>Tiempo de transmisi&oacuten de URL</h1>"
                + "<p><b>URL:</b> "+url+"<p/>"
                + "<table>"
                + "<tr><td><p>Autoridad </td><td>"+autoridad+"</td></tr>"
                + "<tr><td><p>Recurso </td><td>"+recurso+"</td></tr>"
                + "<tr><td><p>Tiempo de transmisi&oacuten </td><td>"+tiempo+""
                + "</td></tr></table>"
                + "<form action='tiempoTransmisionURL' method='get'>"
                + "<p><b>Nueva URL:</b> "
                + "http://<input type='text' name='url' size='40'> "
                + "<input type='submit' value='Recalcular'> "
                + "<input type='reset' value='Borrar'></p>\n"
                + "</form>"
                + "</body>"
                + "</html>";
    }
    
    //contenido inetAddress
    public static String html_inetAddress (InetAddress localhost,
            InetAddress lan, InetAddress web){
        return "<html>"
                + "<head><title>inetAddress</title>"
                + "<link rel='icon' type='image/png' "
                + "href='/faviconBlue.png' sizes='16x16'></link>"
                + "</head>"
                + "<body>"
                + "<h1>Información de inetAddress</h1>"
                + "<h2>***********LOCALHOST**************</h2>"
                + "<p>Nombre: "+localhost.getHostName()+"</p>"
                + "<p>IP: "+localhost.getHostAddress()+"</p>"
                + "<h2>**********LA RED LOCAL*************</h2>"
                + "<p>Nombre de mi Equipo: "+lan.getHostName()+"</p>"
                + "<p>IP: "+lan.getHostAddress()+"</p>"
                + "<h2>**********WEB EXTERNA*************</h2>"
                + "<p>Nombre de host: "+web.getHostName()+"</p>"
                + "<p>Host canónico: "+web.getCanonicalHostName()+"</p>"
                + "<p>IP: "+web.getHostAddress()+"</p>"
                + "</body>"
                + "</html>";
    }
    
    //contenido mostrarFichero
    public static String html_mostrarFichero (String contenido){
        return "<html>"
                + "<head><title>mostrarFicheroLog</title>"
                + "<link rel='icon' type='image/png' href='/faviconRed.png' "
                + "sizes='16x16'></link></head>"
                + "<body>"
                + "<h1>Contenido Fichero Log <a href='descargarFicheroLog'>"
                + "<img src='save.png' alt='Guardar' width='32' height='32'></a>"
                + "</h1>"
                + "<p style='font-family: monospace'>"+contenido+"</p>"
                + "</body>"
                + "</html>";
    }
    
    //contenido noEncontrado
    public static String html_noEncontrado = "<html>"
            + "<head><title>noEncontrado</title></head>"
            + "<body>"
            + "<h1>¡ERROR! Página no encontrada</h1>"
            + "<p>La página que solicitaste no existe en nuestro "
            + "servidor</p>"
            + "</body>"
            + "</html>";
}