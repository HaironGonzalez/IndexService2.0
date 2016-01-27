/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IndexService;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
/**
 *
 * @author Hairon
 */
public class MongoDB  {
    
    DB baseDatos;
    DBCollection tIndice; 
    DBCollection tPaginas; 
    Mongo mongo;
    
    public MongoDB() {
                
        try {
            mongo = new Mongo("localhost", 27017);
            baseDatos = mongo.getDB("BaseDatos");
            tIndice = baseDatos.getCollection("Indice");
            tPaginas = baseDatos.getCollection("Paginas");   
        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
   /* public void MostrarBDs(){    
        List <String> basesDatos = mongo.getDatabaseNames();
        System.out.println("Bases de datos disponibles: " + basesDatos + "\n");
    }
    

    */
    
    public ArrayList<DBCursor> BuscarPalabra(String Palabras){   // retorna un array con los DBcursors de cada palabra
        BasicDBObject busqueda = new BasicDBObject();
        BasicDBObject orden = new BasicDBObject();
        orden.put("Frecuencia", -1 );
        String[] Aux=Palabras.split("\\s+" );    // array de palabras
        ArrayList<DBCursor> Resultados = new ArrayList<DBCursor>();
        for(int i =0;i<Aux.length;i++){ 
            //int Cont = 0;
            if(!Aux[i].contentEquals(" ")&&!Aux[i].contentEquals("")){   // entra si la palabra no es vacia ni espacio
                busqueda.put("Palabra", Aux[i]);
                //int Largo;
                Resultados.add(tIndice.find(busqueda).sort(orden));
                //if(Integer.parseInt(Cant)==0)
                //Largo = Resultados.size(); // si el usuraio elige la opcion Todos 
                //else {Largo= Integer.parseInt(Cant);}
                /*while (Resultados.hasNext()&& Cont<Largo) {
                    DBObject objeto = Resultados.next();
                   
                   // jTextArea1.setText(jTextArea1.getText()+"Palabra: "+ objeto.get("Palabra")+"     ID: "+objeto.get("ID")+"     Frecuencia: "+objeto.get("Frecuencia")+"\n"); 
                    // System.out.println("Palabra: "+ objeto.get("Palabra")+" ID: "+objeto.get("ID")+" Frecuencia: "+objeto.get("Frecuencia")+"\n");
                    Cont++;
                }*/
               // jTextArea1.setText(jTextArea1.getText()+"\n---------------------------------------------------------------------------------------------------------\n");
            }
        }
        return Resultados;
    }
    

    public void BuscarPagina(String ID,javax.swing.JTextArea jTextArea1){
        BasicDBObject busqueda = new BasicDBObject();
        busqueda.put("ID", ID);
        DBCursor Resultados = tPaginas.find(busqueda);
        while (Resultados.hasNext()) {
            DBObject objeto = Resultados.next();
            jTextArea1.setText("ID: "+ objeto.get("ID")+"\n\n"+objeto.get("Titulo")+"\n\n\n"+objeto.get("Texto")+"\n"); 
           System.out.println("ID: "+ objeto.get("ID")+"\n\n"+objeto.get("Titulo")+"\n\n\n"+objeto.get("Texto")+"\n");
        }
    }
    
    
    
}
