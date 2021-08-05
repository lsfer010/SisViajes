/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_viajes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
/**
 *
 * @author lsfer
 */
public class Altas {
    
    private PreparedStatement ps;
    
    public void alta(String compañia, String apellido_Paterno, String apellido_Materno, String nombre, 
            String fecha_Nacimiento, String Sexo, int no_Club_Premier) {
        
        Conexion connect = new Conexion();
        Connection con = connect.getConexion();
        
        try {
            ps = con.prepareStatement("INSERT INTO clientes (Compañia, Apellido_Paterno, Apellido_Materno, "
                    + "Nombre, Fecha_Nacimiento, No_Club_Premier, Sexo) VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, compañia);
            ps.setString(2, apellido_Paterno);
            ps.setString(3, apellido_Materno);
            ps.setString(4, nombre);
            ps.setString(5, fecha_Nacimiento);
            ps.setInt(6, no_Club_Premier);
            ps.setString(7, Sexo);
            
            int res = ps.executeUpdate();
            
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "¡Cliente dado de alta exitosamente!");
            } else {
                JOptionPane.showMessageDialog(null, "Ocurrio un error al dar de alta el cliente");
            }
           
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
        }     
    }
    
    public void alta(String clave_Reservacion, int id_Operador, int id_Cliente, String forma_Pago, float precio, String requiere_Factura, String no_factura_cliente, String no_factura_operador, String forma_Pago_Vendedor) {
        
        Conexion connect = new Conexion();
        Connection con = connect.getConexion();
        
        try {
            ps = con.prepareStatement("INSERT INTO reservaciones (Clave_Reservacion, Clientes_ID_Cliente, Operadores_idOperadores, "
                    + "Forma_Pago_Cliente, Precio, Req_Factura, No_Factura_Cliente, No_Factura_Operador, Forma_Pago_Vendedor) VALUES (?,?,?,?,?,?,?,?,?)");
            ps.setString(1, clave_Reservacion);
            ps.setInt(2, id_Cliente);
            ps.setInt(3, id_Operador);
            ps.setString(4, forma_Pago);
            ps.setFloat(5, precio);
            ps.setString(6, requiere_Factura);
            ps.setString(7, no_factura_cliente);
            ps.setString(8, no_factura_operador);
            ps.setString(9, forma_Pago_Vendedor);
            
            int res = ps.executeUpdate();
            
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "¡Reservación dada de alta exitosamente!");
            } else {
                JOptionPane.showMessageDialog(null, "Ocurrio un error al dar de alta la reservación");
            }
           
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
        }
        
    }
    
    public void alta(String nombre_empresa) {
        Conexion connect = new Conexion();
        Connection con = connect.getConexion();
        
        try {
            ps = con.prepareStatement("INSERT INTO operadores (Nombre) VALUES (?)");
            ps.setString(1, nombre_empresa);
            
            int res = ps.executeUpdate();
            
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "¡Operador dada de alta exitosamente!");
            } else {
                JOptionPane.showMessageDialog(null, "Ocurrio un error al dar de alta al operador");
            }
           
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    
}
