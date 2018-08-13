/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BachBackUpJava;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author 0015352
 */
public class BachBackUpJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try{
            
            /*
                Consultar Tablas: SHOW FULL TABLES FROM mi_base_de_datos;
                Consultar Campos: SHOW COLUMNS FROM Correos;
            
            */
            
            Calendar cl = Calendar.getInstance();
            
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/pruebas", "root", "");
            String fecha = cl.get(Calendar.YEAR)+"-"+(cl.get(Calendar.MONTH)+1)+"-"+cl.get(Calendar.DAY_OF_MONTH);
            
            String archivo, cadena;
            
            System.out.println("Preparando BackUp SKDragons");
            
            Statement consultaTablas = con.createStatement();
            
            ResultSet consultaTa = consultaTablas.executeQuery("SHOW FULL TABLES FROM SKDragons");
            
            while(consultaTa.next()){
              
                archivo = consultaTa.getString(1)+"_"+fecha+".txt";
            
                try{

                    File file = new File(archivo);

                    if(!file.exists()){
                        file.createNewFile();
                    }

                    FileWriter escribir = new FileWriter(archivo);
                    
                    Statement consultaCampos = con.createStatement();
                    ResultSet consultaCam = consultaCampos.executeQuery("SHOW COLUMNS FROM "+consultaTa.getString(1));
                    
                    Statement consultaTodo = con.createStatement();
                    ResultSet consultaT = consultaTodo.executeQuery("SELECT * FROM "+consultaTa.getString(1)+" WHERE 1 = 1");
                    
                    ArrayList<String> lista = new ArrayList<String>();
                    
                    while(consultaCam.next()){
                        lista.add(consultaCam.getString(1));
                    }
                    
                    while(consultaT.next()){
                        cadena = "";
                        
                        for(String i: lista){
                            cadena += consultaT.getString(i)+";";
                        }
                        
                        escribir.write(cadena+"\n");
                    }
                    
                    
                    escribir.flush();
                    escribir.close();

                }
                catch(IOException ex){
                    System.out.println("Error con archivo");
                }
                
            
            }
            
        }
        catch(SQLException ex){
            System.out.println("Error al conectar con la DB");
            ex.printStackTrace();
        }
        
    }
    
}
