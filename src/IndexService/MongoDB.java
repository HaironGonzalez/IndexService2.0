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
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
  
    public void MostrarBDs(){    
        List <String> basesDatos = mongo.getDatabaseNames();
        System.out.println("Bases de datos disponibles: " + basesDatos + "\n");
    }

    public void IngresarPaguina(String titulo,String texto,int id){
        BasicDBObject documento = new BasicDBObject();            
        documento.put("ID",Integer.toString(id));
        documento.put("Titulo",titulo);
        documento.put("Texto",texto);
        
        tPaginas.insert(documento);
        documento.clear();
    }

    public void IngresarPalabra(String palabra,int frecuencia,int id){
 
        
       // System.out.println("Cantidad tablas antes agregado: " + tIndice.count());
        BasicDBObject documento = new BasicDBObject();            
        documento.put("Palabra",palabra);
        documento.put("ID "+Integer.toString(id), Integer.toString(id));
        tIndice.insert(documento);
        documento.clear();  // Hairon sabio :)   Hay que limpiar el documento antes de agregar otro, o bien, usar otro documento.
        //System.out.println("Cantidad tablas despues agregado: " + tIndice.count());
            
        
    }
    
    public void BuscarPalabra(String Palabra,javax.swing.JTextArea jTextArea1,String Cant){
        BasicDBObject busqueda = new BasicDBObject();
        busqueda.put("Palabra", Palabra);
        int Cont = 0;
        DBCursor Resultados = tIndice.find(busqueda);
        while (Resultados.hasNext()&& Cont<Integer.parseInt(Cant)) {
            DBObject objeto = Resultados.next();
            jTextArea1.setText(jTextArea1.getText()+"Palabra: "+ objeto.get("Palabra")+"     ID: "+objeto.get("ID")+"     Frecuencia: "+objeto.get("Frecuencia")+"\n"); 
           // System.out.println("Palabra: "+ objeto.get("Palabra")+" ID: "+objeto.get("ID")+" Frecuencia: "+objeto.get("Frecuencia")+"\n");
            Cont++;
        }
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
