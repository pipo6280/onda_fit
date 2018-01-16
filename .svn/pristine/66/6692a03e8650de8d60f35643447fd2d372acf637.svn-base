/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gimnasio.views;

import com.gimnasio.controller.Operaciones;
import com.gimnasio.model.FisioterapiaDto;
import com.gimnasio.model.enums.EGenero;
import com.gimnasio.util.Util;
import com.google.common.base.Joiner;
import java.awt.Font;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author rodolfo
 */
public final class frmFisioterapia extends javax.swing.JInternalFrame {

    private final Operaciones operacion;
    private FisioterapiaDto fisioterapiaDto;

    /**
     * Creates new form frmFisioterapia
     *
     * @param operacion
     * @param documento
     */
    public frmFisioterapia(Operaciones operacion, String documento) {
        initComponents();
        this.operacion = operacion;
        try {
            this.getAsignarFisioterapia(documento);
        } catch (SQLException ex) {
            Logger.getLogger(frmFisioterapia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param documento
     * @throws java.sql.SQLException
     */
    public void getAsignarFisioterapia(String documento) throws SQLException {
        this.fisioterapiaDto = this.operacion.getFisioterapiaDto(documento);

        this.txtMMSS.setText(String.valueOf(fisioterapiaDto.getTest_mmss()));
        this.txtMMII.setText(String.valueOf(fisioterapiaDto.getTest_mmii()));
        
        this.txtTension_arterial.setText(String.valueOf(fisioterapiaDto.getTension_arterial()));
        this.txtFrecuencia_cardiaca.setText(String.valueOf(fisioterapiaDto.getFrecuencia_cardiaca()));
        this.txtPeak_air.setText(String.valueOf(fisioterapiaDto.getPeak_air()));

        this.txtTest_uno.setText(String.valueOf(fisioterapiaDto.getTest_uno()));
        this.txtTest_dos.setText(String.valueOf(fisioterapiaDto.getTest_dos()));
        this.txtTest_tres.setText(String.valueOf(fisioterapiaDto.getTest_tres()));
        this.getTestFlexibilidad();

        this.txtTriceps.setText(String.valueOf(fisioterapiaDto.getTriceps()));
        this.txtSiliaco.setText(String.valueOf(fisioterapiaDto.getSiliaco()));
        this.txtAbdomen.setText(String.valueOf(fisioterapiaDto.getAbdomen()));
        this.txtMuslo_ant.setText(String.valueOf(fisioterapiaDto.getMuslo_ant()));
        this.txtPectoral.setText(String.valueOf(fisioterapiaDto.getPectoral()));
        this.getPorcentajeGrasa();

        this.txtPeso.setText(String.valueOf(fisioterapiaDto.getPeso()));
        this.txtTalla.setText(String.valueOf(fisioterapiaDto.getTalla()));
        this.getIMC();

        this.txtObservaciones.setText(fisioterapiaDto.getObservaciones());

        this.lblNombre.setText(this.fisioterapiaDto.getClienteDto().getPersonaDto().getNombreCompleto());
        this.lblDocumento.setText(this.fisioterapiaDto.getClienteDto().getPersonaDto().getNumeroIdentificacion());

        if (this.fisioterapiaDto.getClienteDto().getPersonaDto().getEdad() > 0) {
            this.lblEdad.setText(String.valueOf(this.fisioterapiaDto.getClienteDto().getPersonaDto().getEdad()));
        }

        if (this.fisioterapiaDto.getClienteDto().getPersonaDto().getGenero() == EGenero.FEMENIMO.getId()) {
            this.txtPectoral.setEditable(false);
            this.txtAbdomen.setEditable(false);
        } else if (this.fisioterapiaDto.getClienteDto().getPersonaDto().getGenero() == EGenero.MASCULINO.getId()) {
            this.txtTriceps.setEditable(false);
            this.txtSiliaco.setEditable(false);
        }
    }

    /**
     *
     */
    public void setLLenarFisiterapiaDto() {

        this.fisioterapiaDto.setTest_mmss(!Util.getVacio(this.txtMMSS.getText()) ? Double.valueOf(this.txtMMSS.getText()) : 0);
        this.fisioterapiaDto.setTest_mmii(!Util.getVacio(this.txtMMII.getText()) ? Double.valueOf(this.txtMMII.getText()) : 0);

        this.fisioterapiaDto.setTension_arterial(!Util.getVacio(this.txtTension_arterial.getText()) ? Double.valueOf(this.txtTension_arterial.getText()) : 0);
        this.fisioterapiaDto.setFrecuencia_cardiaca(!Util.getVacio(this.txtFrecuencia_cardiaca.getText()) ? Double.valueOf(this.txtFrecuencia_cardiaca.getText()) : 0);
        this.fisioterapiaDto.setPeak_air(!Util.getVacio(this.txtPeak_air.getText()) ? Double.valueOf(this.txtPeak_air.getText()) : 0);
        
        this.fisioterapiaDto.setTest_uno(!Util.getVacio(this.txtTest_uno.getText()) ? Double.valueOf(this.txtTest_uno.getText()) : 0);
        this.fisioterapiaDto.setTest_dos(!Util.getVacio(this.txtTest_dos.getText()) ? Double.valueOf(this.txtTest_dos.getText()) : 0);
        this.fisioterapiaDto.setTest_tres(!Util.getVacio(this.txtTest_tres.getText()) ? Double.valueOf(this.txtTest_tres.getText()) : 0);

        this.fisioterapiaDto.setTriceps(!Util.getVacio(this.txtTriceps.getText()) ? Double.valueOf(this.txtTriceps.getText()) : 0);
        this.fisioterapiaDto.setSiliaco(!Util.getVacio(this.txtSiliaco.getText()) ? Double.valueOf(this.txtSiliaco.getText()) : 0);
        this.fisioterapiaDto.setAbdomen(!Util.getVacio(this.txtAbdomen.getText()) ? Double.valueOf(this.txtAbdomen.getText()) : 0);
        this.fisioterapiaDto.setMuslo_ant(!Util.getVacio(this.txtMuslo_ant.getText()) ? Double.valueOf(this.txtMuslo_ant.getText()) : 0);
        this.fisioterapiaDto.setPectoral(!Util.getVacio(this.txtPectoral.getText()) ? Double.valueOf(this.txtPectoral.getText()) : 0);

        this.fisioterapiaDto.setPeso(!Util.getVacio(this.txtPeso.getText()) ? Double.valueOf(this.txtPeso.getText()) : 0);
        this.fisioterapiaDto.setTalla(!Util.getVacio(this.txtTalla.getText()) ? Double.valueOf(this.txtTalla.getText()) : 0);

        this.fisioterapiaDto.setObservaciones(this.txtObservaciones.getText());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblEdad = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lblDocumento = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtMMSS = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtMMII = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtTest_uno = new javax.swing.JTextField();
        txtTest_dos = new javax.swing.JTextField();
        txtTest_tres = new javax.swing.JTextField();
        txtPromedio_flexibilidad = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtTriceps = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtPectoral = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtMuslo_ant = new javax.swing.JTextField();
        txtSiliaco = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtAbdomen = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtDensidad_grasa = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtPorcentaje_grasa = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txtFrecuencia_cardiaca = new javax.swing.JTextField();
        txtTension_arterial = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtPeak_air = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        txtPeso = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtTalla = new javax.swing.JTextField();
        txtIMC = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        jLabel18 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CLIENTE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Nombre:");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Edad:");

        lblNombre.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNombre.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        lblEdad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblEdad.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Documento:");

        lblDocumento.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDocumento.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDocumento, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                    .addComponent(lblEdad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(lblDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(lblEdad, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "TEST DE MMSS y MMII", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        txtMMSS.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMMSS.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMMSS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMMSSActionPerformed(evt);
            }
        });
        txtMMSS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMMSSKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                validarSoloNumero(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Test de MMSS");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Test de MMII");

        txtMMII.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMMII.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMMII.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMMIIActionPerformed(evt);
            }
        });
        txtMMII.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMMIIKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMMIIKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMMII, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMMSS, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMMSS, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMMII, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "TEST FLEXIBILIDAD", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        txtTest_uno.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTest_uno.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTest_uno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTest_unoFocusLost(evt);
            }
        });
        txtTest_uno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTest_unoActionPerformed(evt);
            }
        });
        txtTest_uno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTest_unoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTest_unoKeyTyped(evt);
            }
        });

        txtTest_dos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTest_dos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTest_dos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTest_dosFocusLost(evt);
            }
        });
        txtTest_dos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTest_dosActionPerformed(evt);
            }
        });
        txtTest_dos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTest_dosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTest_dosKeyTyped(evt);
            }
        });

        txtTest_tres.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTest_tres.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTest_tres.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTest_tresFocusLost(evt);
            }
        });
        txtTest_tres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTest_tresActionPerformed(evt);
            }
        });
        txtTest_tres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTest_tresKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTest_tresKeyTyped(evt);
            }
        });

        txtPromedio_flexibilidad.setEditable(false);
        txtPromedio_flexibilidad.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtPromedio_flexibilidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPromedio_flexibilidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPromedio_flexibilidadActionPerformed(evt);
            }
        });
        txtPromedio_flexibilidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPromedio_flexibilidadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPromedio_flexibilidadKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Test uno");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Test dos");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Test tres");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Promedio");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTest_tres, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTest_dos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTest_uno, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPromedio_flexibilidad)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTest_uno, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTest_dos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTest_tres, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPromedio_flexibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PORCENTAJE DE GRASA (JACKSON/POLLOCK)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Triceps");

        txtTriceps.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTriceps.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTriceps.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTricepsFocusLost(evt);
            }
        });
        txtTriceps.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTricepsActionPerformed(evt);
            }
        });
        txtTriceps.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTricepsKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTricepsKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Pectoral");

        txtPectoral.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPectoral.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPectoral.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPectoralFocusLost(evt);
            }
        });
        txtPectoral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPectoralActionPerformed(evt);
            }
        });
        txtPectoral.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPectoralKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPectoralKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Muslo-ant");

        txtMuslo_ant.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMuslo_ant.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMuslo_ant.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMuslo_antFocusLost(evt);
            }
        });
        txtMuslo_ant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMuslo_antActionPerformed(evt);
            }
        });
        txtMuslo_ant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMuslo_antKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMuslo_antKeyTyped(evt);
            }
        });

        txtSiliaco.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtSiliaco.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSiliaco.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSiliacoFocusLost(evt);
            }
        });
        txtSiliaco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSiliacoActionPerformed(evt);
            }
        });
        txtSiliaco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSiliacoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSiliacoKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Siliaco");

        txtAbdomen.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtAbdomen.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAbdomen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAbdomenFocusLost(evt);
            }
        });
        txtAbdomen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAbdomenActionPerformed(evt);
            }
        });
        txtAbdomen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAbdomenKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAbdomenKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Abdomen");

        txtDensidad_grasa.setEditable(false);
        txtDensidad_grasa.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtDensidad_grasa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDensidad_grasa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDensidad_grasaActionPerformed(evt);
            }
        });
        txtDensidad_grasa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDensidad_grasaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDensidad_grasaKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Densidad");

        txtPorcentaje_grasa.setEditable(false);
        txtPorcentaje_grasa.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtPorcentaje_grasa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPorcentaje_grasa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPorcentaje_grasaActionPerformed(evt);
            }
        });
        txtPorcentaje_grasa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPorcentaje_grasaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPorcentaje_grasaKeyTyped(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Porcentaje");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTriceps, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPectoral, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSiliaco, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAbdomen, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMuslo_ant, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDensidad_grasa, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPorcentaje_grasa, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTriceps, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPectoral, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSiliaco, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(txtAbdomen, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMuslo_ant, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDensidad_grasa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPorcentaje_grasa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "INFORMACIÓN CARDIO-RESPIRATORIA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("F.C. (rep)");

        txtFrecuencia_cardiaca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtFrecuencia_cardiaca.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFrecuencia_cardiaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFrecuencia_cardiacaActionPerformed(evt);
            }
        });
        txtFrecuencia_cardiaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFrecuencia_cardiacaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFrecuencia_cardiacavalidarSoloNumero(evt);
            }
        });

        txtTension_arterial.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTension_arterial.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTension_arterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTension_arterialActionPerformed(evt);
            }
        });
        txtTension_arterial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTension_arterialKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTension_arterialvalidarSoloNumero(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("T.A. ");

        txtPeak_air.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPeak_air.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPeak_air.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPeak_airActionPerformed(evt);
            }
        });
        txtPeak_air.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPeak_airKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPeak_airvalidarSoloNumero(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Peak Air");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFrecuencia_cardiaca, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTension_arterial, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPeak_air, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPeak_air, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel25))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTension_arterial, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFrecuencia_cardiaca, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "INDICE DE MASA CORPORAL", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        txtPeso.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPeso.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPeso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPesoFocusLost(evt);
            }
        });
        txtPeso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesoActionPerformed(evt);
            }
        });
        txtPeso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesoKeyTyped(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Peso (Kg)");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Talla (Cm)");

        txtTalla.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTalla.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTalla.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTallaFocusLost(evt);
            }
        });
        txtTalla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTallaActionPerformed(evt);
            }
        });
        txtTalla.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTallaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTallaKeyTyped(evt);
            }
        });

        txtIMC.setEditable(false);
        txtIMC.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtIMC.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtIMC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIMCActionPerformed(evt);
            }
        });
        txtIMC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIMCKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIMCKeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("I.M.C");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTalla, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPeso, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtIMC)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIMC, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPeso, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTalla, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))))
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ANTECEDENTES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        txtObservaciones.setColumns(20);
        txtObservaciones.setRows(5);
        jScrollPane1.setViewportView(txtObservaciones);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Observaciones");

        btnGuardar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gimnasio/files/floppy-icon.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addComponent(btnGuardar)
                        .addGap(23, 23, 23))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 4, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMMSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMMSSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMMSSActionPerformed

    private void txtMMSSKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMMSSKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtMMSSKeyReleased

    private void validarSoloNumero(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_validarSoloNumero
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            getToolkit().beep();
            evt.consume();
            JOptionPane.showMessageDialog(this, "Ingresa solo numeros", "Error de datos", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_validarSoloNumero

    private void txtMMIIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMMIIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMMIIActionPerformed

    private void txtMMIIKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMMIIKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMMIIKeyReleased

    private void txtMMIIKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMMIIKeyTyped
        // TODO add your handling code here:
        this.validarSoloNumero(evt);
    }//GEN-LAST:event_txtMMIIKeyTyped

    private void txtTest_unoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTest_unoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTest_unoActionPerformed

    private void txtTest_unoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTest_unoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTest_unoKeyReleased

    private void txtTest_unoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTest_unoKeyTyped
        // TODO add your handling code here:
        this.validarSoloNumero(evt);
    }//GEN-LAST:event_txtTest_unoKeyTyped

    private void txtTest_dosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTest_dosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTest_dosActionPerformed

    private void txtTest_dosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTest_dosKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTest_dosKeyReleased

    private void txtTest_dosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTest_dosKeyTyped
        // TODO add your handling code here:
        this.validarSoloNumero(evt);
    }//GEN-LAST:event_txtTest_dosKeyTyped

    private void txtTest_tresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTest_tresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTest_tresActionPerformed

    private void txtTest_tresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTest_tresKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTest_tresKeyReleased

    private void txtTest_tresKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTest_tresKeyTyped
        // TODO add your handling code here:
        this.validarSoloNumero(evt);
    }//GEN-LAST:event_txtTest_tresKeyTyped

    private void txtPromedio_flexibilidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPromedio_flexibilidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPromedio_flexibilidadActionPerformed

    private void txtPromedio_flexibilidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPromedio_flexibilidadKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPromedio_flexibilidadKeyReleased

    private void txtPromedio_flexibilidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPromedio_flexibilidadKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPromedio_flexibilidadKeyTyped

    private void txtTricepsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTricepsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTricepsActionPerformed

    private void txtTricepsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTricepsKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTricepsKeyReleased

    private void txtTricepsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTricepsKeyTyped
        // TODO add your handling code here:
        this.validarSoloNumero(evt);
    }//GEN-LAST:event_txtTricepsKeyTyped

    private void txtPectoralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPectoralActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPectoralActionPerformed

    private void txtPectoralKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPectoralKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPectoralKeyReleased

    private void txtPectoralKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPectoralKeyTyped
        // TODO add your handling code here:
        this.validarSoloNumero(evt);
    }//GEN-LAST:event_txtPectoralKeyTyped

    private void txtMuslo_antActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMuslo_antActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMuslo_antActionPerformed

    private void txtMuslo_antKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMuslo_antKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMuslo_antKeyReleased

    private void txtMuslo_antKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMuslo_antKeyTyped
        // TODO add your handling code here:
        this.validarSoloNumero(evt);
    }//GEN-LAST:event_txtMuslo_antKeyTyped

    private void txtSiliacoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSiliacoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSiliacoActionPerformed

    private void txtSiliacoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSiliacoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSiliacoKeyReleased

    private void txtSiliacoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSiliacoKeyTyped
        // TODO add your handling code here:
        this.validarSoloNumero(evt);
    }//GEN-LAST:event_txtSiliacoKeyTyped

    private void txtAbdomenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAbdomenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAbdomenActionPerformed

    private void txtAbdomenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAbdomenKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAbdomenKeyReleased

    private void txtAbdomenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAbdomenKeyTyped
        // TODO add your handling code here:
        this.validarSoloNumero(evt);
    }//GEN-LAST:event_txtAbdomenKeyTyped

    private void txtDensidad_grasaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDensidad_grasaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDensidad_grasaActionPerformed

    private void txtDensidad_grasaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDensidad_grasaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDensidad_grasaKeyReleased

    private void txtDensidad_grasaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDensidad_grasaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDensidad_grasaKeyTyped

    private void txtPesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesoActionPerformed

    private void txtPesoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesoKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtPesoKeyReleased

    private void txtPesoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesoKeyTyped
        // TODO add your handling code here:
        this.validarSoloNumero(evt);
    }//GEN-LAST:event_txtPesoKeyTyped

    private void txtTallaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTallaActionPerformed
        // TODO add your handling code here:        
    }//GEN-LAST:event_txtTallaActionPerformed

    private void txtTallaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTallaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTallaKeyReleased

    private void txtTallaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTallaKeyTyped
        // TODO add your handling code here:
        this.validarSoloNumero(evt);
        this.getIMC();
    }//GEN-LAST:event_txtTallaKeyTyped

    private void txtIMCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIMCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIMCActionPerformed

    private void txtIMCKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIMCKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIMCKeyReleased

    private void txtIMCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIMCKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIMCKeyTyped

    /**
     *
     */
    public void getIMC() {
        double imc = this.fisioterapiaDto.setCalcularIMC();
        if (imc > 0) {
            this.txtIMC.setText(String.valueOf(imc));
        }
    }

    /**
     *
     */
    public void getTestFlexibilidad() {
        double test = this.fisioterapiaDto.setCalculaTesFlexibilidad();
        if (test > 0) {
            this.txtPromedio_flexibilidad.setText(String.valueOf(test));
        }
    }

    /**
     *
     */
    public void getPorcentajeGrasa() {
        this.fisioterapiaDto.getJacksonPollock();
        if (this.fisioterapiaDto.getDensidad() > 0) {
            this.txtDensidad_grasa.setText(String.valueOf(this.fisioterapiaDto.getDensidad()));
        } else {
            this.txtDensidad_grasa.setText("");
        }
        if (this.fisioterapiaDto.getPorcentaje() > 0) {
            this.txtPorcentaje_grasa.setText(String.valueOf(this.fisioterapiaDto.getPorcentaje()));
        } else {
            this.txtPorcentaje_grasa.setText("");
        }
    }

    /**
     *
     * @param evt
     */
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        this.setLLenarFisiterapiaDto();
        List<String> listMessage = this.operacion.setGuardarFisioterapia(this.fisioterapiaDto);
        if (listMessage.size() < 1) {
            JLabel label = new JLabel("<html>Los datos del cliente: <b>" + this.fisioterapiaDto.getClienteDto().getPersonaDto().getNombreCompleto() + "</b>, fueron guardados correctamente</html>");
            label.setFont(new Font("consolas", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(this, label, "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JLabel label = new JLabel("<html>Verífique la siguiente lista de campos obligatorios:\n<ul>" + Joiner.on("\n").join(listMessage) + "</ul></html>");
            label.setFont(new Font("consolas", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(this, label, "Alerta de verificación de datos", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtPesoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPesoFocusLost
        // TODO add your handling code here:
        double peso = 0;
        if (!Util.getVacio(this.txtPeso.getText())) {
            peso = Double.valueOf(this.txtPeso.getText());
        }
        this.fisioterapiaDto.setPeso(peso);
        this.getIMC();
    }//GEN-LAST:event_txtPesoFocusLost

    /**
     *
     * @param evt
     */
    private void txtTallaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTallaFocusLost
        // TODO add your handling code here:
        double talla = 0;
        if (!Util.getVacio(this.txtTalla.getText())) {
            talla = Double.valueOf(this.txtTalla.getText());
        }
        this.fisioterapiaDto.setTalla(talla);
        this.getIMC();
    }//GEN-LAST:event_txtTallaFocusLost

    /**
     *
     * @param evt
     */
    private void txtTest_unoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTest_unoFocusLost
        // TODO add your handling code here:
        double test = 0;
        if (!Util.getVacio(this.txtTest_uno.getText())) {
            test = Double.valueOf(this.txtTest_uno.getText());
        }
        this.fisioterapiaDto.setTest_uno(test);
        this.getTestFlexibilidad();
    }//GEN-LAST:event_txtTest_unoFocusLost

    /**
     *
     * @param evt
     */
    private void txtTest_dosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTest_dosFocusLost
        // TODO add your handling code here:
        double test = 0;
        if (!Util.getVacio(this.txtTest_dos.getText())) {
            test = Double.valueOf(this.txtTest_dos.getText());
        }
        this.fisioterapiaDto.setTest_dos(test);
        this.getTestFlexibilidad();
    }//GEN-LAST:event_txtTest_dosFocusLost

    /**
     *
     * @param evt
     */
    private void txtTest_tresFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTest_tresFocusLost
        // TODO add your handling code here:
        double test = 0;
        if (!Util.getVacio(this.txtTest_tres.getText())) {
            test = Double.valueOf(this.txtTest_tres.getText());
        }
        this.fisioterapiaDto.setTest_tres(test);
        this.getTestFlexibilidad();
    }//GEN-LAST:event_txtTest_tresFocusLost

    private void txtPorcentaje_grasaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPorcentaje_grasaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPorcentaje_grasaActionPerformed

    private void txtPorcentaje_grasaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPorcentaje_grasaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPorcentaje_grasaKeyReleased

    private void txtPorcentaje_grasaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPorcentaje_grasaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPorcentaje_grasaKeyTyped

    /**
     *
     * @param evt
     */
    private void txtTricepsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTricepsFocusLost
        // TODO add your handling code here:
        double tricep = 0;
        if (!Util.getVacio(this.txtTriceps.getText())) {
            tricep = Double.valueOf(this.txtTriceps.getText());
        }
        this.fisioterapiaDto.setTriceps(tricep);
        this.getPorcentajeGrasa();
    }//GEN-LAST:event_txtTricepsFocusLost

    /**
     *
     * @param evt
     */
    private void txtSiliacoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSiliacoFocusLost
        // TODO add your handling code here:
        double siliaco = 0;
        if (!Util.getVacio(this.txtSiliaco.getText())) {
            siliaco = Double.valueOf(this.txtSiliaco.getText());
        }
        this.fisioterapiaDto.setSiliaco(siliaco);
        this.getPorcentajeGrasa();
    }//GEN-LAST:event_txtSiliacoFocusLost

    /**
     *
     * @param evt
     */
    private void txtPectoralFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPectoralFocusLost
        // TODO add your handling code here:
        double pectoral = 0;
        if (!Util.getVacio(this.txtPectoral.getText())) {
            pectoral = Double.valueOf(this.txtPectoral.getText());
        }
        this.fisioterapiaDto.setPectoral(pectoral);
        this.getPorcentajeGrasa();
    }//GEN-LAST:event_txtPectoralFocusLost

    /**
     *
     * @param evt
     */
    private void txtAbdomenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAbdomenFocusLost
        // TODO add your handling code here:
        double abdomen = 0;
        if (!Util.getVacio(this.txtAbdomen.getText())) {
            abdomen = Double.valueOf(this.txtAbdomen.getText());
        }
        this.fisioterapiaDto.setAbdomen(abdomen);
        this.getPorcentajeGrasa();
    }//GEN-LAST:event_txtAbdomenFocusLost

    /**
     *
     * @param evt
     */
    private void txtMuslo_antFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMuslo_antFocusLost
        // TODO add your handling code here:
        double muslo = 0;
        if (!Util.getVacio(this.txtMuslo_ant.getText())) {
            muslo = Double.valueOf(this.txtMuslo_ant.getText());
        }
        this.fisioterapiaDto.setMuslo_ant(muslo);
        this.getPorcentajeGrasa();
    }//GEN-LAST:event_txtMuslo_antFocusLost

    private void txtFrecuencia_cardiacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFrecuencia_cardiacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFrecuencia_cardiacaActionPerformed

    private void txtFrecuencia_cardiacaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFrecuencia_cardiacaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFrecuencia_cardiacaKeyReleased

    private void txtFrecuencia_cardiacavalidarSoloNumero(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFrecuencia_cardiacavalidarSoloNumero
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFrecuencia_cardiacavalidarSoloNumero

    private void txtTension_arterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTension_arterialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTension_arterialActionPerformed

    private void txtTension_arterialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTension_arterialKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTension_arterialKeyReleased

    private void txtTension_arterialvalidarSoloNumero(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTension_arterialvalidarSoloNumero
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTension_arterialvalidarSoloNumero

    private void txtPeak_airActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPeak_airActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPeak_airActionPerformed

    private void txtPeak_airKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPeak_airKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPeak_airKeyReleased

    private void txtPeak_airvalidarSoloNumero(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPeak_airvalidarSoloNumero
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPeak_airvalidarSoloNumero


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDocumento;
    private javax.swing.JLabel lblEdad;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JTextField txtAbdomen;
    private javax.swing.JTextField txtDensidad_grasa;
    private javax.swing.JTextField txtFrecuencia_cardiaca;
    private javax.swing.JTextField txtIMC;
    private javax.swing.JTextField txtMMII;
    private javax.swing.JTextField txtMMSS;
    private javax.swing.JTextField txtMuslo_ant;
    private javax.swing.JTextArea txtObservaciones;
    private javax.swing.JTextField txtPeak_air;
    private javax.swing.JTextField txtPectoral;
    private javax.swing.JTextField txtPeso;
    private javax.swing.JTextField txtPorcentaje_grasa;
    private javax.swing.JTextField txtPromedio_flexibilidad;
    private javax.swing.JTextField txtSiliaco;
    private javax.swing.JTextField txtTalla;
    private javax.swing.JTextField txtTension_arterial;
    private javax.swing.JTextField txtTest_dos;
    private javax.swing.JTextField txtTest_tres;
    private javax.swing.JTextField txtTest_uno;
    private javax.swing.JTextField txtTriceps;
    // End of variables declaration//GEN-END:variables
}
