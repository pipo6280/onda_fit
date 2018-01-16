package com.gimnasio.model;

import java.io.BufferedReader;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileReader;
import javax.swing.JTextArea;

public class Conexion {

    private List<String> listSql;
    private static Connection conexion;

    private String host;
    private String user;
    private String dataBase;
    private String password;

    public Conexion() {
        try {
            File file;
            FileReader fr;
            BufferedReader br;

            file = new File("conexion.txt");
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String linea = br.readLine();
            br.close();
            fr.close();

            String[] partes = linea.split(";");
            this.host = partes[0];
            this.user = partes[1];
            this.password = partes[2];
            this.dataBase = partes[3];
            this.listSql = new ArrayList();
        } catch (Exception ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect() {
        try {
            if (Conexion.conexion == null) {
                Class.forName("com.mysql.jdbc.Driver");
                Conexion.conexion = DriverManager.getConnection("jdbc:mysql://" + this.host + "/" + this.dataBase, this.user, this.password);
                Conexion.conexion.setAutoCommit(false);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public void close() {
        try {
            Conexion.conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void commit() {
        try {
            Conexion.conexion.commit();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void rollback() {
        try {
            Conexion.conexion.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void query(JTextArea text) {
        Statement stat;
        int acciones = 1;
        int total = this.listSql.size();
        if (text != null) {
            text.setText("Se afectaran " + total + " Registros de la base de datos\n" + text.getText());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            stat = Conexion.conexion.createStatement();
            for (String sql : this.listSql) {
                try {
                    stat.executeUpdate(sql);
                    if (text != null) {
                        text.setText("Registro " + acciones + " afectado de " + total + "\n" + text.getText());
                        acciones++;
                    }
                } catch (Exception e) {
                    if (text != null) {
                        text.setText("El registro " + acciones + " presenta errores para el proceso " + total + "\n" + text.getText());
                        acciones++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            this.listSql.clear();
            stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> getListSql() {
        return listSql;
    }

    public void setListSql(List<String> listSql) {
        this.listSql = listSql;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        Conexion.conexion = conexion;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDataBase() {
        return dataBase;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
