/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IndexService;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.awt.PageAttributes.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.ArrayList;
import static javax.swing.text.html.FormSubmitEvent.MethodType.GET;
import static jdk.nashorn.internal.runtime.PropertyDescriptor.GET;
import org.json.simple.JSONObject;

/**
 *
 * @author Rudolfaraya
 */

public class IndexServer {

    /**
     * @param args the command line arguments
     */

    public static MongoDB mongodb = new MongoDB();
    
        
    public static void main(String[] args) {
        int contadorRespuesta = 0;
        ArrayList <DBCursor> Resultados = new ArrayList<DBCursor>();
        JSONObject json = new JSONObject();
        
        
        
        // COMUNICACIÓN CON EL CLIENTE
        ServerSocket serverSocketCliente; 
        Socket socketCliente;
        DataInputStream in; //Flujo de datos de entrada
        ObjectOutputStream out; //Flujo de datos de salida
        String mensajedelCliente;
        String mensajeCliente;
        
        
        //COMUNICACIÓN CON EL CACHE
        ServerSocket serverSocketCache;        
        Socket socketCache;
        DataOutputStream outCache;
        DataInputStream inCache;
        String mensajeCache;
        
        
        try{
            serverSocketCliente = new ServerSocket(5555);
            System.out.print("SERVIDOR INDEX ACTIVO a la espera de peticiones");
            
            //MIENTRAS PERMANEZCA ACTIVO EL SERVIDOR INDEX ESPERARÁ POR PETICIONES DEL CLIENTE
            while(true){
                //RECIBIR CONSULTA DEL CLIENTE
                socketCliente = serverSocketCliente.accept();
                in = new DataInputStream(socketCliente.getInputStream()); //Entrada de los mensajes del cliente
                mensajedelCliente = in.readUTF();                                  
                System.out.println("\nHe recibido del CLIENTE: "+mensajedelCliente); //Muestro el mensaje recibido por el CLIENTE
                
                
                Resultados = mongodb.BuscarPalabra(mensajedelCliente);
                

                for (int i = 0; i<Resultados.size ();i++){
                
                    while (Resultados.get(i).hasNext()) {
                    DBObject objeto = Resultados.get(i).next();
                   
                    if(json.containsKey(objeto.get("Palabra"))){
                        String Aux = (String) json.get(objeto.get("Palabra"));
                        Aux = Aux+","+"ID:"+objeto.get("ID")+" F:"+objeto.get("Frecuencia");
                        json.replace(objeto.get("Palabra"), Aux);
                    }else{
                        json.put(objeto.get("Palabra"),"ID:"+objeto.get("ID")+" F:"+objeto.get("Frecuencia"));
                    }
                     
                    }
                }
                
                //RESPUESTA PARA EL CLIENTE                
                out = new ObjectOutputStream(socketCliente.getOutputStream());
                mensajeCliente = "Respuesta para "+mensajedelCliente;                   
                out.writeObject(json);
                System.out.println("He enviado al Cliente: "+mensajeCliente);
                
                //CIERRE DE LOS SOCKETS (FLUJOS DE DATOS)
                out.close();
                in.close();
                socketCliente.close();
                                
                
                //RESPUESTA DEL SERVIDOR INDICE AL SERVIDOR CACHE
                socketCache = new Socket("127.0.0.1",6666);
                outCache = new DataOutputStream(socketCache.getOutputStream());
                String respuesta = mensajeCliente;
                outCache.writeUTF(respuesta); //Envía respuesta al Cache
                System.out.println("\nTengo la respuesta. He respondido al SERVIDOR CACHE: "+respuesta);
                             
                //inCache.close();
                outCache.close();
                socketCache.close();                     
                
                
            }      
        }catch(Exception e){
            System.out.print(e.getMessage());
        }
    }
    
        
        
    }
    