/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BBDD;

import Modelo.Cliente;
import Modelo.Factura;
import Modelo.Ingrediente;
import Modelo.Pizza;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Profesor
 */
public class Conexion {

    private String url;
    private String usuario;
    private String pass;
    private Connection con;

    public Conexion() {
        this.url = "jdbc:mysql://localhost:3307/telepizza";
        this.usuario = "root";
        this.pass = "usbw";
    }

    public Conexion(String url, String usuario, String pass) {
        this.url = url;
        this.usuario = usuario;
        this.pass = pass;
    }

    private void conectar() {
        try {
            con = (Connection) DriverManager.getConnection(url, usuario, pass);

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void desconectar() {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Consulta datos de una columna de una tabla
     *
     * @param tabla la tabla a consultar
     * @param columna el nombre de la columna a consultar
     * @return ArrayList con los datos obtenidos, null si no hay datos
     */
    public ArrayList<String> verDatosTabla(String columna, String tabla) {
        ArrayList<String> vDatos = new ArrayList<>();

        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select " + columna + " from " + tabla + ";");
            // ResultSet rs = st.executeQuery("Select * from pizza;");
            while (rs.next()) {
                vDatos.add(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vDatos;
    }

    public Pizza obtenerPizza(String nombre) {
        Pizza pizza = null;
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select * from pizza where nombre = '" + nombre + "';");

            if (rs.next()) {
                pizza = new Pizza(rs.getString(2), rs.getFloat(3));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pizza;
    }

    public Ingrediente buscarIngrediente(String nombre) {
        Ingrediente ingrediente = null;
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select * from ingrediente where nombre = '" + nombre + "';");

            if (rs.next()) {
                ingrediente = new Ingrediente(rs.getString(2), rs.getFloat(3));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ingrediente;
    }

    /**
     * Busca un cliente por su telefono en la BBDD
     *
     * @param numero el número de teléfono por el que buscar
     * @return un cliente si se encuentra en la bbdd o null si no se encuentra
     */
    public Cliente buscarClienteNombre(String numero) {
        Cliente cliente = null;
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select * from cliente where telefono = '" + numero + "';");

            if (rs.next()) {
                cliente = new Cliente(rs.getString(2), rs.getString(3), rs.getString(4));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cliente;
    }

    public void crearFactura(Factura factura) {

        try {
            conectar();
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO telepizza.factura  VALUES ("+ factura.getId_cliente() + ", '" + factura.getPedido() + "', "+factura.getTotal()+");");

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
