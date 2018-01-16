/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gimnasio.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author Miguel Carmona
 */
public class Ftp {

    private FTPClient cliente;

    public boolean crearCarpeta(String carpeta) {
        try {
            this.cliente.mkd(carpeta);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean conectar() {
        try {
            final String serverAddress = Text.getText("url_ftp").trim();
            final String userId = Text.getText("usuario_ftp").trim();
            final String password = Text.getText("clave_ftp").trim();
            final String remoteDirectory = "/";
            //String localDirectory         = "";
            this.cliente = new FTPClient();

            this.cliente.connect(serverAddress);
            if (!this.cliente.login(userId, password)) {
                this.cliente.logout();
                return false;
            }
            int reply = this.cliente.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.cliente.disconnect();
                return false;
            }
            //enter passive mode
            this.cliente.enterLocalPassiveMode();
            //get system name
            System.out.println("Remote system is " + this.cliente.getSystemType());
            //change current directory
            this.cliente.changeWorkingDirectory(remoteDirectory);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean desconectar() {
        try {
            this.cliente.logout();
            this.cliente.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean startFTP(String propertiesFilename, String fileToFTP) {
        try {
            //enter passive mode
            // htis.cliente.enterLocalPassiveMode();
            //get system name
            //System.out.println("Remote system is " + ftp.getSystemType());
            //change current directory
            //  this.cliente.changeWorkingDirectory(remoteDirectory);
            //System.out.println("Current directory is " + ftp.printWorkingDirectory());

            //get input stream
            //InputStream input       = null;
            // input                   = new FileInputStream(fileToFTP);
            //store the file in the remote server
            // ftp.storeFile(fileToFTP, input);
            //this.cliente.
            //close the stream
            //  input.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
