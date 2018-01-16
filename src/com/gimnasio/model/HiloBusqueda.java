package com.gimnasio.model;

import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.gimnasio.views.frmClientesIngresos;
import com.gimnasio.controller.Operaciones;
import com.gimnasio.model.enums.ESiNo;
import com.gimnasio.views.frmHuella;
import com.gimnasio.views.frmRegistrarPagos;
import java.awt.AWTException;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrador
 */
public class HiloBusqueda extends Thread {

    private frmClientesIngresos frmClienteIngreso;
    private DPFPFeatureSet featuresverificacion;
    private List<ClienteDto> listPlantillas;
    private List<HiloBusqueda> listHilos;
    private UsuarioDto usuarioSessionDto;
    private boolean ingresaAsistencia;
    private Operaciones operacion;
    private JLabel lblEstudiante;
    private boolean corriendo;
    private JLabel lblCodigo;
    private boolean continua;
    private frmHuella cerrar;

    /**
     *
     * @param cerrar
     * @param operacion
     * @param frmClienteIngreso
     */
    public HiloBusqueda(frmHuella cerrar, Operaciones operacion, frmClientesIngresos frmClienteIngreso) {
        this.frmClienteIngreso = frmClienteIngreso;
        this.operacion = operacion;
        this.corriendo = true;
        this.cerrar = cerrar;
        this.continua = true;
    }

    @Override
    public void run() {
        DPFPVerificationResult result;
        DPFPVerification verificator;
        boolean entra = false;
        try {
            verificator = DPFPGlobal.getVerificationFactory().createVerification();
            verificator.setFARRequested(DPFPVerification.LOW_SECURITY_FAR);
        } catch (Exception e) {
            for (HiloBusqueda h : this.listHilos) {
                if (h != this) {
                    h.setContinua(false);
                }
            }
            this.cerrar.setEnviarTexto("Error al iniciar verificador");
            System.err.println("Error al iniciar verificador");
            verificator = null;
        }
        for (ClienteDto dto : this.listPlantillas) {
            if (entra || !this.continua) {
                break;
            }
            if (this.continua) {
                try {
                    if (verificator != null && this.featuresverificacion != null && dto != null && dto.getPersonaDto().getTemplateHuella() != null) {
                        result = verificator.verify(featuresverificacion, dto.getPersonaDto().getTemplateHuella());
                        if (result.isVerified()) {
                            entra = true;
                            if (!this.ingresaAsistencia) {
                                this.lblCodigo.setText(dto.getId().toString());
                                this.lblEstudiante.setText(dto.getPersonaDto().getNombreCompleto());
                            } else if (this.ingresaAsistencia) {
                                for (HiloBusqueda h : this.listHilos) {
                                    if (h != this) {
                                        h.setContinua(false);
                                    }
                                }
                                if (dto.getId() > 0 && !dto.getId().equals("")) {
                                    this.setGuardaAsistencia(dto);
                                }
                                break;
                            }
                        }
                    } else {
//                        for (HiloBusqueda h : this.listHilos) {
//                            if (h != this) {
//                                h.setContinua(false);
//                            }
//                        }
//                        this.continua = false;
//                        break;

                    }
                } catch (Exception e) {
                    this.cerrar.setEnviarTexto("Error verificando la huella del cliente, detale del error: " + e.getMessage());
                    for (HiloBusqueda h : this.listHilos) {
                        if (h != this) {
                            h.setContinua(false);
                        }
                    }
                    this.continua = false;
                    break;
                }
            }
        }
        if (!this.ingresaAsistencia) {
            this.corriendo = false;
            if (entra) {
                this.cerrar.setCambia(true);
                autoclose();
                JLabel label = new JLabel("El cliente ha sido identificado");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this.cerrar, label, "Alerta de verificación de huellas", JOptionPane.WARNING_MESSAGE);
                this.cerrar.dispose();
            } else if (!this.cerrar.isCambia()) {
                int contador = 0;
                for (HiloBusqueda h : this.listHilos) {
                    if (!h.isCorriendo() && h.isContinua()) {
                        contador++;
                    }
                }
                if (contador == this.listHilos.size()) {
                    this.cerrar.setCambia(true);
                    autoclose();
                    JLabel label = new JLabel("No se han encontrado huellas");
                    label.setFont(new Font("consolas", Font.PLAIN, 14));
                    JOptionPane.showMessageDialog(this.cerrar, label, "Alerta de verificación de huellas", JOptionPane.WARNING_MESSAGE);
                    this.cerrar.dispose();
                }
            }
        } else if (!entra) {
            if (!this.cerrar.isCambia()) {
                this.continua = false;
                int cant = 0;
                for (HiloBusqueda h : this.listHilos) {
                    if (!h.isContinua()) {
                        cant++;
                    }
                }
                if (cant == this.listHilos.size()) {
                    this.cerrar.setCambia(true);
                    autoclose();
                    JLabel label = new JLabel("El usuario no se encuentra registrado con esta huella");
                    label.setFont(new Font("consolas", Font.PLAIN, 14));
                    JOptionPane.showMessageDialog(this.cerrar, label, "Alerta de verificación de huellas", JOptionPane.WARNING_MESSAGE);
                    this.cerrar.start();
                    this.cerrar.getBtnCancelar().setEnabled(true);
                }
            }
        } else {
            this.cerrar.setCambia(true);
            for (HiloBusqueda h : this.listHilos) {
                h.setContinua(false);
            }
        }
        System.gc();
    }

    /**
     *
     * @param clienteDto
     * @throws java.text.ParseException
     */
    public void setGuardaAsistencia(ClienteDto clienteDto) throws ParseException {
        try {
            boolean ingresa = false;
            boolean existe = false;
            this.lblCodigo.setText(clienteDto.getId().toString());
            this.lblEstudiante.setText(clienteDto.getPersonaDto().getNombreCompleto());
            //List<ClienteIngresoDto> listIngresos = this.operacion.getClientesIngresosDia(clienteDto.getId().toString());
            if (this.operacion.setInsertarIngresoCliente(clienteDto, this.getUsuarioSessionDto().getId())) {
                ingresa = true;
                List<TablaDto> listCliente = this.operacion.getClientesDatosTablaDto(null, null, clienteDto.getPersonaDto().getNumeroIdentificacion());
                this.frmClienteIngreso.setAsignarDatoTabla(listCliente.get(0));
            }
            
            /*
            if (listIngresos.size() < 1) {
                if (this.operacion.setInsertarIngresoCliente(clienteDto, this.getUsuarioSessionDto().getId())) {
                    ingresa = true;
                    List<TablaDto> listCliente = this.operacion.getClientesDatosTablaDto(null, null, clienteDto.getPersonaDto().getNumeroIdentificacion());
                    this.frmClienteIngreso.setAsignarDatoTabla(listCliente.get(0));
                }
            } else {
                existe = true;
            }
            */
            
            if (existe) {
                //JLabel label = new JLabel("El usuario ya cumplió con la rutina para el día de hoy");
                //label.setFont(new Font("consolas", Font.PLAIN, 14));
                //JOptionPane.showMessageDialog(this.frmClienteIngreso, label, "Mensaje de Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (ingresa) {
                ClientePaqueteDto paqueteDto = this.operacion.getPaqueteActivoCliente(clienteDto.getId().toString(), null, null);
                String message = "El registro de ingreso se realizó de forma correcta. <br><br>";
                if (paqueteDto.getId() != null && paqueteDto.getId() > 0) {
                    message += "Cliente: <b>" + paqueteDto.getClienteDto().getPersonaDto().getNombreCompleto() + "</b><br>"
                            + "Plan o paquete: <b>" + paqueteDto.getPaqueteDto().getNombre() + "</b><br>";
                    if (paqueteDto.getPaqueteDto().getYnTiquetera() == ESiNo.SI.getId()) {
                        message += "Días tiquetera: <b>" + paqueteDto.getNumeroDiasTiquetera() + "</b><br>";
                        message += "Días cumplidos: <b>" + paqueteDto.getDiasUsadosTiquetera() + "</b><br>";
                        message += "Días restantes: <b>" + (paqueteDto.getNumeroDiasTiquetera() - paqueteDto.getDiasUsadosTiquetera()) + "</b><br>";
                    } else {
                        try {
                            SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                            Date fechaTemp = new SimpleDateFormat("yyyy-MM-dd").parse(paqueteDto.getFechaFinalizaPaquete());
                            String fecha = formateador.format(fechaTemp);
                            message += "Fecha de finalización: <b>" + fecha + "</b><br>";
                        } catch (ParseException ex) {
                            Logger.getLogger(HiloBusqueda.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    message += "El plan o paquete vence el día de hoy para el cliente: <b>" + clienteDto.getPersonaDto().getNombreCompleto().toUpperCase() + "</b><br>";
                }
                
                //Must schedule the close before the dialog becomes visible
                autoclose();                        
                JLabel label = new JLabel("<html>" + message + "</html>");
                label.setFont(new Font("consolas", Font.PLAIN, 16));
                JOptionPane.showMessageDialog(this.frmClienteIngreso, label, "Mensaje de Advertencia", JOptionPane.INFORMATION_MESSAGE);
            } else {
                //Must schedule the close before the dialog becomes visible
                autoclose();
                JLabel label = new JLabel("El cliente no cuenta con un paquete o plan activo, para el día de hoy");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this.frmClienteIngreso, label, "Mensaje de Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | HeadlessException e) {
            JLabel label = new JLabel("Error no controlado, intente nuevamente");
            label.setFont(new Font("consolas", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(this.frmClienteIngreso, label, "Mensaje de Advertencia", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(HiloBusqueda.class.getName()).log(Level.SEVERE, null, e);
        }
        this.cerrar.start();
        this.cerrar.getBtnCancelar().setEnabled(true);
    }
    
    
    public void autoclose() {
        //Must schedule the close before the dialog becomes visible
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();     
        s.schedule(new Runnable() {
            public void run() {                                
                try {
                    Robot robot = new Robot();
                    //se presiona la tecla escape
                    robot.keyPress(KeyEvent.VK_ESCAPE);
                } catch (AWTException ex) {
                    Logger.getLogger(frmRegistrarPagos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 3, TimeUnit.SECONDS);
    }

    public List<ClienteDto> getListPlantillas() {
        return listPlantillas;
    }

    public void setListPlantillas(List<ClienteDto> listPlantillas) {
        this.listPlantillas = listPlantillas;
    }

    public List<HiloBusqueda> getListHilos() {
        return listHilos;
    }

    public void setListHilos(List<HiloBusqueda> listHilos) {
        this.listHilos = listHilos;
    }

    public JLabel getLblCodigo() {
        return lblCodigo;
    }

    public void setLblCodigo(JLabel lblCodigo) {
        this.lblCodigo = lblCodigo;
    }

    public JLabel getLblEstudiante() {
        return lblEstudiante;
    }

    public void setLblEstudiante(JLabel lblEstudiante) {
        this.lblEstudiante = lblEstudiante;
    }

    public DPFPFeatureSet getFeaturesverificacion() {
        return featuresverificacion;
    }

    public void setFeaturesverificacion(DPFPFeatureSet featuresverificacion) {
        this.featuresverificacion = featuresverificacion;
    }

    public boolean isContinua() {
        return continua;
    }

    public void setContinua(boolean continua) {
        this.continua = continua;
    }

    public frmHuella getCerrar() {
        return cerrar;
    }

    public void setCerrar(frmHuella cerrar) {
        this.cerrar = cerrar;
    }

    public boolean isCorriendo() {
        return corriendo;
    }

    public void setCorriendo(boolean corriendo) {
        this.corriendo = corriendo;
    }

    public Operaciones getOperacion() {
        return operacion;
    }

    public void setOperacion(Operaciones operacion) {
        this.operacion = operacion;
    }

    public frmClientesIngresos getFrmClienteIngreso() {
        return frmClienteIngreso;
    }

    public void setFrmClienteIngreso(frmClientesIngresos frmClienteIngreso) {
        this.frmClienteIngreso = frmClienteIngreso;
    }

    public UsuarioDto getUsuarioSessionDto() {
        return usuarioSessionDto;
    }

    public void setUsuarioSessionDto(UsuarioDto usuarioSessionDto) {
        this.usuarioSessionDto = usuarioSessionDto;
    }

    public boolean isIngresaAsistencia() {
        return ingresaAsistencia;
    }

    public void setIngresaAsistencia(boolean ingresaAsistencia) {
        this.ingresaAsistencia = ingresaAsistencia;
    }

}
