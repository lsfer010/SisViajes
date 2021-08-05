/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_viajes;

import javax.swing.JFrame;

/**
 *
 * @author lsfer
 */
public class Sis_Viajes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Menu_Principal pantalla = new Menu_Principal();
        pantalla.setVisible(true);
        pantalla.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //EL SIGUIENTE CODIGO ES PARA CENTRAR LA PANTALLA
       pantalla.setLocationRelativeTo(null);
    }
    
}
