/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_viajes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author lsfer
 */
public class Bajas {
    private PreparedStatement ps;
    private ResultSet rs;
    
    /* Secccion de baja para entidad de Categoria */
    
    public void bajaReservacion(String clave_reservacion) {
        Conexion connect = new Conexion();
        try {
            Connection con = connect.getConexion();
            ps = con.prepareStatement("DELETE FROM reservaciones WHERE Clave_Reservacion=?");
            ps.setString(1, clave_reservacion);
            
            int res = ps.executeUpdate();
            
            if (res > 0) {
                JOptionPane.showMessageDialog(null,"La reservación se eliminó exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null,"Ocurrio un error al dar de baja la reservación");
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showInputDialog(e);
        }
    }
}
