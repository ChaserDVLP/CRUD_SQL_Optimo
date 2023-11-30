/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ad_crud_sql;

import java.sql.*;
import java.util.Scanner;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import oracle.ucp.jdbc.PoolDataSource;


public class AD_CRUD_SQL {
    
    static final String DB_URL = "jdbc:mysql://localhost:3306/jcvd";
    static final String USER = "admin";
    static final String PASS = "1234";
    static Statement stmt = null;
    static PreparedStatement sentencia = null;
    
    //Consulta parametrizada
    static public boolean buscarNombre(String nombreBuscado) throws SQLException {
        
        String querySelect = "SELECT * FROM videojuegos WHERE Nombre = ? ";
        boolean res = false;
        
        try {
            Connection conexion = DriverManager.getConnection(DB_URL, USER, PASS);
            sentencia = conexion.prepareStatement(querySelect);
            //Sustituimos la interrogacion por la variable
            sentencia.setString(1, nombreBuscado); //Empieza en 1
            ResultSet rs = sentencia.executeQuery(); //Como es una consulta usamos el executeQuery()
            
            while (rs.next()) {
                System.out.println("Se ha encontrado el nombre: "+rs.getString("Nombre"));
                res = true;
            }
        
        } catch (SQLException ex) {
            System.out.println("Error al conectar la BBDD: "+ex);
            ex.printStackTrace();
            
        } finally { //Cerramos la sentencia igusl que son Statement
            if (sentencia != null) {
                sentencia.close();
            }
        }
        
        return res;
    }
    
    static public void ejecutarConsulta(String query) throws SQLException {
        
        try {
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Nombre: " +rs.getString("Nombre"));
                System.out.println("Genero: " + rs.getString("Genero"));
                System.out.println("Fecha Lanzamiento: " + rs.getDate("FechaLanzamiento"));
                System.out.println("Compañía: " + rs.getString("Compañia"));
                System.out.println("Precio: " + rs.getFloat("Precio")+"\n");
            }
            
        } catch (SQLException ex) {
            System.out.println("Error al conectar la BBDD: "+ex);
            ex.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    static public void nuevoRegistro (String nombre, String genero, String fechaLanzamiento, 
            String compañia, float precio) throws SQLException {
        
        String query = "INSERT INTO videojuegos VALUES (null,?,?,?,?,?);";
                        
        try {
            Connection conexion = DriverManager.getConnection(DB_URL, USER, PASS);
            sentencia = conexion.prepareStatement(query);
            sentencia.setString(1, nombre);
            sentencia.setString(2, genero);
            sentencia.setString(3, fechaLanzamiento);
            sentencia.setString(4, compañia);
            sentencia.setFloat(5, precio);
            
            if (sentencia.executeUpdate() > 0) { //Devuelve la cantidad de filas afectadas
                System.out.println("Se ha insertado el registro");
            } else {
                System.out.println("fallo al insertar");
            }
            
        } catch (SQLException e) {
            System.out.println("Error al conectar la BBDD: "+e);
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                sentencia.close();
            }
        }
    }
    
    static public void nuevoRegistroManual() throws SQLException {
        
        String nombre, genero, fechaLanzamiento, compañia;
        float precio;
        Scanner teclado = new Scanner(System.in); 
        
        System.out.print("Introduce el nombre: ");
        nombre = teclado.nextLine();
        System.out.print("Introduce el genero: ");
        genero = teclado.nextLine();
        System.out.print("Introduce la fecha de lanzamiento: ");
        fechaLanzamiento = teclado.nextLine();
        System.out.print("Introduce la compañia: ");
        compañia = teclado.nextLine();
        System.out.print("Introduce el precio: ");
        precio = teclado.nextFloat();
        
        //String query = "INSERT INTO videojuegos VALUES (null,?,?,?,?,?);";
                        
        try {
            Connection conexion = DriverManager.getConnection(DB_URL, USER, PASS);
            sentencia = conexion.prepareStatement("INSERT INTO videojuegos VALUES (null,?,?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, nombre);
            sentencia.setString(2, genero);
            sentencia.setString(3, fechaLanzamiento);
            sentencia.setString(4, compañia);
            sentencia.setFloat(5, precio);
            
            
            if (sentencia.executeUpdate() > 0) { //Devuelve la cantidad de filas afectadas
                System.out.println("Se ha insertado el registro");
            } else {
                System.out.println("fallo al insertar");
            }
            
            //Obtener la clave del registro generado
            ResultSet rs = sentencia.getGeneratedKeys();
            while (rs.next()) {                
                int claveGenerada = rs.getInt(1);
                System.out.println("Clave generada = "+claveGenerada);
            }
            
        } catch (SQLException e) {
            System.out.println("Error al conectar la BBDD: "+e);
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                sentencia.close();
            }
        }
        
    }
    
    static public boolean eliminarRegistro (String nombre) throws SQLException {
        
        boolean res = false;
        String query = "DELETE FROM videojuegos WHERE Nombre = ?;";
        
        try {
            Connection conexion = DriverManager.getConnection(DB_URL, USER, PASS);
            sentencia = conexion.prepareStatement(query);
            sentencia.setString(1, nombre);
            
            if (sentencia.executeUpdate() > 0) { //Devuelve la cantidad de filas afectadas
                System.out.println("Se ha eliminado el registro");
                res = true;
            } else {
                System.out.println("fallo al eliminar");
            }
            
        } catch (SQLException e) {
            System.out.println("Error al conectar la BBDD: "+e);
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                sentencia.close();
            }
        }
        
        return res;
    }
    
    static public void probandoPool() {
        
        try {
            PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
            pds.setConnectionFactoryClassName( "com.mysql.cj.jdbc.Driver");
            pds.setURL(DB_URL);
            pds.setUser(USER);
            pds.setPassword(PASS);

            Connection conn = pds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM videojuegos;");
            
            while(rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Nombre: " +rs.getString("Nombre"));
                System.out.println("Genero: " + rs.getString("Genero"));
                System.out.println("Fecha Lanzamiento: " + rs.getDate("FechaLanzamiento"));
                System.out.println("Compañía: " + rs.getString("Compañia"));
                System.out.println("Precio: " + rs.getFloat("Precio")+"\n");
            }
            
            conn.close();
            conn = null;
            
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        
    }

    public static void main(String[] args) throws SQLException {
        
        
        /*Scanner teclado = new Scanner(System.in);
        System.out.print("Introduce el nombre a buscar: ");
        String nombre = teclado.nextLine();
        
        
        if (!buscarNombre(nombre)) {
            System.out.println("No se ha encontrado ningun registro");
        }*/
        
        //ejecutarConsulta("SELECT * FROM videojuegos WHERE Nombre = 'Monster Hunter World'");

        /*nuevoRegistro("prueba", "test", "2010/10/10", "test", 50);
        nuevoRegistroManual();
        eliminarRegistro("prueba");*/

        probandoPool();
        
    }
}
