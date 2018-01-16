package com.gimnasio.controller;

import com.gimnasio.model.*;
import com.gimnasio.model.enums.*;
import com.gimnasio.util.Util;
import java.awt.Font;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author emimaster16
 */
public class Operaciones {
    
    private Model model;
    private Conexion conexion;
    
    public Operaciones() {
        this.conexion = new Conexion();
        this.conexion.connect();
        this.model = new Model();
        this.model.setConexion(this.conexion);
    }

    /**
     *
     * @param nombres
     * @param apellidos
     * @param documento
     * @param limite
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getUsuariosDatosTablaDto(String nombres, String apellidos, String documento, String limite) throws SQLException {
        return this.getUsuariosDatosTablaDto(nombres, apellidos, documento, limite, null);
    }

    /**
     *
     * @param nombres
     * @param apellidos
     * @param documento
     * @param limite
     * @param llaves
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getUsuariosDatosTablaDto(String nombres, String apellidos, String documento, String limite, List<String> llaves) throws SQLException {
        List<TablaDto> listTable = new ArrayList();
        List<UsuarioDto> listUsuarios = this.model.getDatosUsuariosSistema(nombres, apellidos, documento, limite, llaves);
        listUsuarios.stream().map((usuario) -> new TablaDto(
                String.valueOf(usuario.getPersonaDto().getNumeroIdentificacion()),
                Util.getQuitaNULL(usuario.getPersonaDto().getNombreCompleto().toUpperCase()),
                Util.getQuitaNULL(EGenero.getResult(usuario.getPersonaDto().getGenero()).getNombre()),
                String.valueOf(Util.getQuitaNULL(usuario.getPersonaDto().getMovil())) + (Util.getVacio(usuario.getPersonaDto().getTelefono()) ? "" : (" - " + String.valueOf(Util.getQuitaNULL(usuario.getPersonaDto().getTelefono())))),
                String.valueOf(Util.getQuitaNULL(usuario.getPersonaDto().getEmail())),
                Util.getQuitaNULL(EPerfiles.getResult(usuario.getTipoUsuario()).getNombre()),
                String.valueOf(Util.getQuitaNULL(usuario.getLoggin())),
                Util.getQuitaNULL(ESiNo.getResult(usuario.getYnActivo()).getNombre())
        )
        ).forEach((tabla) -> {
            listTable.add(tabla);
        });
        return listTable;
    }
    
    public void setCambiarEstadosPaquetes(UsuarioDto userDto) throws SQLException {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fechaActual = format.format(new Date());
            List<ClientePaqueteDto> listPaquetes = this.model.getListPaquetesActivosClientes(null, fechaActual, true);
            Statement stat = this.conexion.getConexion().createStatement();
            for (ClientePaqueteDto paquete : listPaquetes) {
                stat.execute("UPDATE cliente_paquete  SET estado = '" + EEstadoPlan.VENCIDO.getId() + "', usuario_id = '" + userDto.getId() + "', fecha_modificacion = NOW()  WHERE id = '" + paquete.getId() + "'");
            }
            stat.close();
        } catch (SQLException ex) {
            this.conexion.rollback();
            throw ex;
        } finally {
            this.conexion.commit();
        }
    }
    
    public boolean setCambiarPassword(UsuarioDto usu, String password) throws SQLException {
        boolean correct = false;
        try {
            Statement stat = this.conexion.getConexion().createStatement();
            stat.execute("UPDATE usuarios  SET password = '" + Util.getEncriptarMD5(password) + "', fecha_modificacion=NOW() WHERE id = " + usu.getId());
            correct = true;
        } catch (SQLException ex) {
            this.conexion.rollback();
            correct = false;
            throw ex;
        } finally {
            this.conexion.commit();
        }
        return correct;
    }

    /**
     *
     * @param clienteDto
     * @param idUsuario
     * @return
     * @throws SQLException
     * @throws java.text.ParseException
     */
    public boolean setInsertarIngresoCliente(ClienteDto clienteDto, long idUsuario) throws SQLException, ParseException {
        boolean correct = false;
        if (clienteDto.getId() > 0 && !clienteDto.getId().equals("")) {
            ClientePaqueteDto paqueteDto = this.model.getPaqueteActivoCliente(clienteDto.getId().toString(), null, null);
            if (paqueteDto.getId() != null && paqueteDto.getId() > 0) {
                try {
                    Statement stat = this.conexion.getConexion().createStatement();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date fechaActual = format.parse(format.format(new Date()));
                    Date fechaInitPaquete = format.parse(paqueteDto.getFechaIniciaPaquete());
                    Date fechaFinalPaquete = null;
                    if (paqueteDto.getFechaFinalizaPaquete() != null) {
                        fechaFinalPaquete = format.parse(paqueteDto.getFechaFinalizaPaquete());
                    }
                    if (paqueteDto.getPaqueteDto().getYnTiquetera() == ESiNo.SI.getId()) {
                        if ((fechaInitPaquete.equals(fechaActual) || fechaInitPaquete.before(fechaActual)) && paqueteDto.getPaqueteDto().getYnTiquetera() == ESiNo.SI.getId()) {
                            int diasUsados = paqueteDto.getDiasUsadosTiquetera() + 1;
                            String fechaFinaliza = null;
                            if (diasUsados == paqueteDto.getNumeroDiasTiquetera()) {
                                fechaFinaliza = format.format(fechaActual);
                                stat.execute("UPDATE cliente_paquete  SET dias_usados_tiquetera = '" + diasUsados + "', estado = '" + EEstadoPlan.VENCIDO.getId() + "', fecha_finaliza_paquete = '" + fechaFinaliza + "', usuario_id = '" + idUsuario + "', fecha_modificacion = NOW()  WHERE id = '" + paqueteDto.getId() + "'");
                                correct = true;
                            } else {
                                stat.execute("UPDATE cliente_paquete  SET dias_usados_tiquetera = '" + diasUsados + "', estado = '" + EEstadoPlan.ACTIVO.getId() + "', usuario_id = '" + idUsuario + "', fecha_modificacion = NOW()  WHERE id = '" + paqueteDto.getId() + "'");
                                correct = true;
                            }
                        } else if (fechaFinalPaquete != null && fechaFinalPaquete.equals(fechaActual)) {
                            stat.execute("UPDATE  cliente_paquete  SET estado = '" + EEstadoPlan.VENCIDO.getId() + "', usuario_id = '" + idUsuario + "', fecha_modificacion = NOW()  WHERE id = '" + paqueteDto.getId() + "'");
                            correct = true;
                        } else {
                            correct = false;
                        }
                    } else if (fechaInitPaquete != null && (fechaInitPaquete.before(fechaActual) || fechaInitPaquete.equals(fechaActual))) {
                        if (fechaFinalPaquete.equals(fechaActual) || fechaFinalPaquete.before(fechaActual)) {
                            stat.execute("UPDATE  cliente_paquete  SET estado = '" + EEstadoPlan.VENCIDO.getId() + "', usuario_id = '" + idUsuario + "', fecha_modificacion = NOW()  WHERE id = '" + paqueteDto.getId() + "'");
                            correct = true;
                        }
                        // se pone true porque tiene plazo para que realice el registro diario
                        correct = true;
                    }
                    if (correct) {
                        paqueteDto.setClienteDto(clienteDto);
                        correct = this.model.setInsertarIngresoCliente(paqueteDto, idUsuario);
                    }
                    stat.close();
                } catch (ParseException ex) {
                    this.conexion.rollback();
                    throw ex;
                }
                if (correct == true) {
                    this.conexion.commit();
                }
            }
            return correct;
        }
        return correct;
    }

    /**
     *
     * @param fecha
     * @param tipo
     * @param horas
     * @return
     */
    public Date sumarRestarHorasFecha(Date fecha, int tipo, int horas) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(tipo, horas); // numero de horas a añadir, o restar en caso de horas<0
        return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
    }

    /**
     *
     * @param idCliente
     * @return
     * @throws SQLException
     */
    public List<ClienteIngresoDto> getClientesIngresosDia(String idCliente) throws SQLException {
        return this.model.getClientesIngresosDia(idCliente);
    }
    
    public List<TablaDto> getClientesIngresoTableDto() throws SQLException {
        List<TablaDto> result = new ArrayList();
        List<ClienteIngresoDto> list = this.model.getClientesIngresosDia(null);
        if (list.size() > 0) {
            List<String> llaves = new ArrayList();
            for (ClienteIngresoDto clienteDto : list) {
                llaves.add(clienteDto.getId().toString());
            }
            return this.getClientesDatosTablaDto(llaves);
        }
        
        return result;
    }

    /**
     *
     * @return List
     */
    public List<ComboDto> getLimiteConsulta() {
        List<ComboDto> lista = new ArrayList();
        lista.add(new ComboDto("20", "20"));
        lista.add(new ComboDto("50", "50"));
        lista.add(new ComboDto("100", "100"));
        lista.add(new ComboDto("200", "200"));
        lista.add(new ComboDto("500", "500"));
        lista.add(new ComboDto("1000", "1000"));
        lista.add(new ComboDto("todos", "Todos"));
        return lista;
    }

    /**
     *
     * @return
     */
    public List<ComboDto> getListMeses() {
        List<ComboDto> lista = new ArrayList();
        lista.add(new ComboDto("1", "Enero"));
        lista.add(new ComboDto("2", "Febrero"));
        lista.add(new ComboDto("3", "Marzo"));
        lista.add(new ComboDto("4", "Abril"));
        lista.add(new ComboDto("5", "Mayo"));
        lista.add(new ComboDto("6", "Junio"));
        lista.add(new ComboDto("7", "Julio"));
        lista.add(new ComboDto("8", "Agosto"));
        lista.add(new ComboDto("9", "Septiembre"));
        lista.add(new ComboDto("10", "Octubre"));
        lista.add(new ComboDto("11", "Noviembre"));
        lista.add(new ComboDto("12", "Diciembre"));
        return lista;
    }

    /**
     *
     * @param clientePaqueteDto
     * @param registraAsistencia
     * @return
     * @throws SQLException
     */
    public boolean setGuardaPagoPaqueteCliente(ClientePaqueteDto clientePaqueteDto, boolean registraAsistencia) throws SQLException {
        return this.model.setGuardaPagoPaqueteCliente(clientePaqueteDto, registraAsistencia);
    }

    /**
     *
     * @return List
     * @throws Exception
     */
    public List<ComboDto> getTipoPlanEnumerado() throws Exception {
        List<ComboDto> lista = new ArrayList();
        for (ETipoPlan tip : ETipoPlan.getValues()) {
            ComboDto dto = new ComboDto(String.valueOf(tip.getId()), tip.getNombre());
            lista.add(dto);
        }
        return lista;
    }
    
    public List<ComboDto> getYnActivo() throws Exception {
        List<ComboDto> lista = new ArrayList();
        for (ESiNo tip : ESiNo.getValues()) {
            ComboDto dto = new ComboDto(String.valueOf(tip.getId()), tip.getNombre());
            lista.add(dto);
        }
        return lista;
    }

    /**
     *
     * @return List
     * @throws Exception
     */
    public List<ComboDto> getDescuentosEnumerado() throws Exception {
        List<ComboDto> lista = new ArrayList();
        List<DescuentoDto> listDescuentos = this.model.getDatosDescuentos(null);
        for (DescuentoDto descuento : listDescuentos) {
            ComboDto dto = new ComboDto(String.valueOf(descuento.getId()), descuento.getNombre() + " - " + String.valueOf(descuento.getPorcentaje()) + "%", String.valueOf(descuento.getPorcentaje()));
            lista.add(dto);
        }
        return lista;
    }

    /**
     *
     * @return List
     * @throws Exception
     */
    public List<ComboDto> getPaquetesEnumerado() throws Exception {
        List<ComboDto> lista = new ArrayList();
        List<PaqueteDto> listPaquetes = this.model.getPaquetesDatos(null);
        for (PaqueteDto paquete : listPaquetes) {
            ComboDto dto = new ComboDto(String.valueOf(paquete.getId()), paquete.getNombre(), String.valueOf(paquete.getYnTiquetera()), String.valueOf(paquete.getPrecioBase()), String.valueOf(paquete.getTipo()));
            lista.add(dto);
        }
        return lista;
    }
    
    public List<PaqueteDto> getPaquetesDatos(String idPaquete) throws SQLException {
        return this.model.getPaquetesDatos(idPaquete);
    }

    /**
     *
     * @param fisioterapia
     * @return
     */
    public List<String> setGuardarFisioterapia(FisioterapiaDto fisioterapia) {
        List<String> listMessages = new ArrayList();
        if (fisioterapia.getTest_mmss() > 0) {
        } else {
            listMessages.add("<li>TEST DE MMSS: Número de repeticiones</li>");
        }
        if (fisioterapia.getTest_mmii() > 0) {
        } else {
            listMessages.add("<li>TEST DE MMII: Número de repeticiones</li>");
        }
        
        if (fisioterapia.getTest_uno() > 0) {
        } else {
            listMessages.add("<li>TEST DE FLEXIBILIDAD: Test uno</li>");
        }
        if (fisioterapia.getTest_dos() > 0) {
        } else {
            listMessages.add("<li>TEST DE FLEXIBILIDAD: Test dos</li>");
        }
        if (fisioterapia.getTest_tres() > 0) {
        } else {
            listMessages.add("<li>TEST DE FLEXIBILIDAD: Test tres</li>");
        }
        
        if (fisioterapia.getClienteDto().getPersonaDto().getGenero() == EGenero.FEMENIMO.getId()) { //HOMBRE
            if (fisioterapia.getTriceps() > 0) {
            } else {
                listMessages.add("<li>PORCENTAJE DE GRASA: Triceps</li>");
            }
            if (fisioterapia.getSiliaco() > 0) {
            } else {
                listMessages.add("<li>PORCENTAJE DE GRASA: Siliaco</li>");
            }
        }
        
        if (fisioterapia.getClienteDto().getPersonaDto().getGenero() == EGenero.MASCULINO.getId()) { //HOMBRE
            if (fisioterapia.getPectoral() > 0) {
            } else {
                listMessages.add("<li>PORCENTAJE DE GRASA: Pectoral</li>");
            }
            if (fisioterapia.getAbdomen() > 0) {
            } else {
                listMessages.add("<li>PORCENTAJE DE GRASA: Abdomen</li>");
            }
        }
        if (fisioterapia.getMuslo_ant() > 0) {
        } else {
            listMessages.add("<li>PORCENTAJE DE GRASA: Muslo-ant</li>");
        }
        if (fisioterapia.getFrecuencia_cardiaca() > 0) {
        } else {
            listMessages.add("<li>INFORMACIÓN CARDIO-RESPIRATORIA: F.C. (rep)</li>");
        }
        if (fisioterapia.getPeso() > 0) {
        } else {
            listMessages.add("<li>INDICE DE MASA CORPORAL: Peso</li>");
        }
        if (fisioterapia.getPeso() > 0) {
        } else {
            listMessages.add("<li>INDICE DE MASA CORPORAL: Talla</li>");
        }
        if (listMessages.size() < 1) {
            try {
                this.model.getSaveFisioterapia(fisioterapia);
                
            } catch (SQLException ex) {
                Logger.getLogger(Operaciones.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listMessages;
    }

    /**
     *
     * @param documento
     * @return
     * @throws SQLException
     */
    public FisioterapiaDto getFisioterapiaDto(String documento) throws SQLException {
        return this.model.getFisioterapiaDto(documento);
    }

    /**
     *
     * @param llaves
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getClientesDatosTablaDto(List<String> llaves) throws SQLException {
        return this.getClientesDatosTablaDto(null, null, null, null, llaves);
    }

    /**
     *
     * @param nombres
     * @param apellidos
     * @param documento
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getClientesDatosTablaDto(String nombres, String apellidos, String documento) throws SQLException {
        return this.getClientesDatosTablaDto(nombres, apellidos, documento, null, null);
    }

    /**
     *
     * @param nombres
     * @param apellidos
     * @param documento
     * @param limite
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getClientesDatosTablaDto(String nombres, String apellidos, String documento, String limite) throws SQLException {
        return this.getClientesDatosTablaDto(nombres, apellidos, documento, limite, null);
    }

    /**
     *
     * @param nombres
     * @param apellidos
     * @param documento
     * @param limite
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getReporteFisioterapiaTablaDto(String nombres, String apellidos, String documento, String limite) throws SQLException {
        return this.getReporteFisioterapiaTablaDto(nombres, apellidos, documento, limite, null);
    }

    /**
     *
     * @param mes
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getClientesDatosTablaDto(String mes) throws SQLException {
        return this.getClientesCumpleanosTablaDto(mes);
    }

    /**
     *
     * @param nombres
     * @param apellidos
     * @param documento
     * @param limite
     * @param llaves
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getClientesDatosTablaDto(String nombres, String apellidos, String documento, String limite, List<String> llaves) throws SQLException {
        List<TablaDto> listTable = new ArrayList();
        //"Documento", "Nombres", "Apellidos", "Edad", "Genero", "Movil", "Fijo", "Correo"
        List<ClienteDto> listClientes = this.model.getDatosClientes(nombres, apellidos, documento, limite, llaves);
        listClientes.stream().map((cliente) -> new TablaDto(
                String.valueOf(cliente.getPersonaDto().getNumeroIdentificacion()),
                Util.getQuitaNULL(cliente.getPersonaDto().getPrimerNombre() + " " + Util.getQuitaNULL(cliente.getPersonaDto().getSegundoNombre())),
                Util.getQuitaNULL(cliente.getPersonaDto().getPrimerApellido() + " " + Util.getQuitaNULL(cliente.getPersonaDto().getSegundoApellido())),
                String.valueOf(cliente.getPersonaDto().getEdad()),
                Util.getQuitaNULL(EGenero.getResult(cliente.getPersonaDto().getGenero()).getNombre()),
                String.valueOf(Util.getQuitaNULL(cliente.getPersonaDto().getMovil())),
                String.valueOf(Util.getQuitaNULL(cliente.getPersonaDto().getTelefono())),
                String.valueOf(Util.getQuitaNULL(cliente.getPersonaDto().getEmail()))
        )).forEach((tabla) -> {
            listTable.add(tabla);
        });
        return listTable;
    }

    /**
     *
     * @param nombres
     * @param apellidos
     * @param documento
     * @param limite
     * @param llaves
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getReporteFisioterapiaTablaDto(String nombres, String apellidos, String documento, String limite, List<String> llaves) throws SQLException {
        List<TablaDto> listTable = new ArrayList();
        //"Documento", "Nombres", "Apellidos", "Edad", "Genero", "Movil", "Fijo", "Correo"
        List<FisioterapiaDto> listFisioterapia = this.model.getFisioterapiaDto(nombres, apellidos, documento, limite, llaves);
        
        listFisioterapia.stream().forEach((fisioterapia) -> {
            fisioterapia.getJacksonPollock();
        });
        
        listFisioterapia.stream().map((fisioterapia) -> new TablaDto(
                String.valueOf(fisioterapia.getClienteDto().getPersonaDto().getNumeroIdentificacion()),
                Util.getQuitaNULL(fisioterapia.getClienteDto().getPersonaDto().getPrimerNombre() + " " + Util.getQuitaNULL(fisioterapia.getClienteDto().getPersonaDto().getSegundoNombre())),
                Util.getQuitaNULL(fisioterapia.getClienteDto().getPersonaDto().getPrimerApellido() + " " + Util.getQuitaNULL(fisioterapia.getClienteDto().getPersonaDto().getSegundoApellido())),
                String.valueOf(fisioterapia.getClienteDto().getPersonaDto().getEdad()),
                Util.getQuitaNULL(EGenero.getResult(fisioterapia.getClienteDto().getPersonaDto().getGenero()).getNombre()),
                String.valueOf(fisioterapia.setCalculaTesFlexibilidad()),
                String.valueOf(fisioterapia.getDensidad()),
                String.valueOf(fisioterapia.getPorcentaje()),
                String.valueOf(fisioterapia.setCalcularIMC())
        )).forEach((tabla) -> {
            listTable.add(tabla);
        });
        
        return listTable;
    }

    /**
     *
     * @param mes
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getClientesCumpleanosTablaDto(String mes) throws SQLException {
        List<TablaDto> listTable = new ArrayList();
        //"Documento", "Nombres", "Apellidos", "Edad", "Genero", "Movil", "Fijo", "Correo"
        List<ClienteDto> listClientes = this.model.getDatosClientes(mes);
        listClientes.stream().map((cliente) -> new TablaDto(
                String.valueOf(cliente.getPersonaDto().getNumeroIdentificacion()),
                Util.getQuitaNULL(cliente.getPersonaDto().getPrimerNombre() + " " + Util.getQuitaNULL(cliente.getPersonaDto().getSegundoNombre())),
                Util.getQuitaNULL(cliente.getPersonaDto().getPrimerApellido() + " " + Util.getQuitaNULL(cliente.getPersonaDto().getSegundoApellido())),
                String.valueOf(cliente.getPersonaDto().getFechaNacimiento()),
                String.valueOf(cliente.getPersonaDto().getEdad()),
                Util.getQuitaNULL(EGenero.getResult(cliente.getPersonaDto().getGenero()).getNombre()),
                String.valueOf(Util.getQuitaNULL(cliente.getPersonaDto().getMovil())),
                String.valueOf(Util.getQuitaNULL(cliente.getPersonaDto().getTelefono())),
                String.valueOf(Util.getQuitaNULL(cliente.getPersonaDto().getEmail()))
        )).forEach((tabla) -> {
            listTable.add(tabla);
        });
        return listTable;
    }

    /**
     *
     * @param idDescuento
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getDescuentosDatosTablaDto(String idDescuento) throws SQLException {
        List<TablaDto> listTable = new ArrayList();
        List<DescuentoDto> listDescuentos = this.model.getDatosDescuentos(idDescuento);
        listDescuentos.stream().map((descuento) -> new TablaDto(
                String.valueOf(descuento.getId()),
                Util.getQuitaNULL(descuento.getNombre()),
                String.valueOf(descuento.getPorcentaje()))).forEach((tabla) -> {
                    listTable.add(tabla);
                });
        return listTable;
    }

    /**
     *
     * @param idCafeteria
     * @param idProducto
     * @param idUsuario
     * @param cantidad
     * @param valor_total
     * @param tipo_pago
     * @return
     */
    public boolean setSaveUpdateCafeteria(int idCafeteria, int idProducto, long idUsuario, String cantidad, String valor_total, int tipo_pago) {
        boolean guarda = false;
        try {
            if (idProducto > 0 && idUsuario > 0 && !cantidad.equals("") && !valor_total.equals("")) {
                java.util.Date fecha = new Date();
                if(idCafeteria > 0) {
                    guarda = this.model.setUpdateCafeteria(idCafeteria, idProducto, idUsuario, cantidad, valor_total, fecha, tipo_pago);                   
                } else {
                    guarda = this.model.setGuardarCafeteria(idProducto, idUsuario, cantidad, valor_total, fecha, tipo_pago);                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operaciones.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return guarda;
    }

    /**
     *
     * @param descuento
     * @return
     * @throws java.sql.SQLException
     */
    public boolean setSaveUpdateDescuentos(DescuentoDto descuento) throws SQLException {
        boolean guarda = false;
        if (Util.getVacio(descuento.getNombre())) {
            JOptionPane.showMessageDialog(null, "El campo para el nombre del descuento es obligatorio");
        } else if (Util.getVacio(String.valueOf(descuento.getPorcentaje()))) {
            JOptionPane.showMessageDialog(null, "El campo para el porcentaje del descuento es obligatorio");
        } else {
            guarda = this.model.setGuardarDescuento(descuento);
        }
        return guarda;
    }

    /**
     *
     * @return @throws SQLException
     */
    public List<ComboDto> getProductosDatosComboDto() throws SQLException {
        List<ComboDto> listTable = new ArrayList();
        List<ProductoDto> listProductos = this.getProductosDatosDto(null);
        listProductos.stream().map((producto) -> new ComboDto(String.valueOf(producto.getId()),
                Util.getQuitaNULL(producto.getNombre()))).forEach((tabla) -> {
                    listTable.add(tabla);
                });
        return listTable;
    }

    /**
     *
     * @param idProducto
     * @return
     * @throws SQLException
     */
    public List<ProductoDto> getProductosDatosDto(String idProducto) throws SQLException {
        List<ProductoDto> listProductos = this.model.getDatosProductos(idProducto);
        return listProductos;
    }

    /**
     *
     * @param producto
     * @return
     * @throws SQLException
     */
    public boolean setSaveUpdateProductos(ProductoDto producto) throws SQLException {
        boolean guarda = false;
        if (Util.getVacio(producto.getNombre())) {
            JOptionPane.showMessageDialog(null, "El campo para el nombre del descuento es obligatorio");
        } else if (Util.getVacio(String.valueOf(producto.getPrecio()))) {
            JOptionPane.showMessageDialog(null, "El campo para el porcentaje del descuento es obligatorio");
        } else {
            guarda = this.model.setGuardarProducto(producto);
        }
        return guarda;
    }

    /**
     *
     * @param idProducto
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getProductosDatosTablaDto(String idProducto) throws SQLException {
        List<TablaDto> listTable = new ArrayList();
        List<ProductoDto> listProductos = this.model.getDatosProductos(idProducto);
        listProductos.stream().map((producto) -> new TablaDto(
                String.valueOf(producto.getId()),
                Util.getQuitaNULL(producto.getNombre()),
                String.valueOf(producto.getPrecio()))).forEach((tabla) -> {
                    listTable.add(tabla);
                });
        return listTable;
    }

    /**
     *
     * @param idPersona
     * @param numeroDocuemento
     * @return
     * @throws SQLException
     */
    public boolean setValidaDocumentoCliene(String idPersona, String numeroDocuemento) throws SQLException {
        boolean correcto = true;
        List<PersonaDto> listPersonas = this.model.getDatosPersona(numeroDocuemento);
        for (PersonaDto dto : listPersonas) {
            if (!Util.getVacio(idPersona)) {
                if (!idPersona.equals(String.valueOf(dto.getId())) && numeroDocuemento.equals(dto.getNumeroIdentificacion())) {
                    correcto = false;
                    break;
                }
            } else if (numeroDocuemento.equals(dto.getNumeroIdentificacion())) {
                correcto = false;
                break;
            }
        }
        return correcto;
    }
    
    public List<ClienteDto> getClienteHuellasDatos(String idCliente) throws SQLException {
        List<ClienteDto> list = new ArrayList();
        List<ClienteDto> listClientes = this.model.getClienteDatos(idCliente, null);
        for (ClienteDto dto : listClientes) {
            if (dto.getPersonaDto().getHuellaDactilar() != null) {
                list.add(dto);
            }
        }
        return list;
    }
    
    public List<ClienteDto> getClienteDatos(String idCliente) throws SQLException {
        List<ClienteDto> list = this.model.getClienteDatos(idCliente, null);
        return list;
    }

    /**
     * @tutorial Method Description: consulta los datos del cliente
     * @author Eminson Mendoza ~~ emimaster16@gmail.com
     * @date 09/07/2016
     * @param idUsuario
     * @param numeroDocuemnto
     * @throws java.sql.SQLException
     * @return
     */
    public List<UsuarioDto> getUsuarioPersonaDatos(String idUsuario, String numeroDocuemnto) throws SQLException {
        return this.model.getUsuarioPersonaDatos(idUsuario, numeroDocuemnto);
    }

    /**
     * @tutorial Method Description: consulta los datos del cliente
     * @author Eminson Mendoza ~~ emimaster16@gmail.com
     * @date 09/07/2016
     * @param idCliente
     * @param numeroDocuemnto
     * @throws java.sql.SQLException
     * @return
     */
    public List<ClienteDto> getClienteDatos(String idCliente, String numeroDocuemnto) throws SQLException {
        List<ClienteDto> list = this.model.getClienteDatos(idCliente, numeroDocuemnto);
        return list;
    }

    /**
     * @tutorial Method Description: valida que la informacion este correcta
     * @author Eminson Mendoza ~~ emimaster16@gmail.com
     * @date 08/07/2016
     * @param usuarioDto
     * @return
     * @throws SQLException
     */
    public List<String> setValidarDatosUsuario(UsuarioDto usuarioDto) throws SQLException {
        List<String> listMessages = new ArrayList();
        if (Util.getVacio(usuarioDto.getPersonaDto().getPrimerNombre())) {
            listMessages.add("<li>Primer nombre</li>");
        }
        if (Util.getVacio(usuarioDto.getPersonaDto().getPrimerApellido())) {
            listMessages.add("<li>Primer appelido</li>");
        }
        if (Util.getVacio(String.valueOf(usuarioDto.getPersonaDto().getTipoIdentificacion())) || usuarioDto.getPersonaDto().getTipoIdentificacion() == 0) {
            listMessages.add("<li>Tipo documento</li>");
        }
        if (Util.getVacio(usuarioDto.getPersonaDto().getNumeroIdentificacion())) {
            listMessages.add("<li>Número de documento</li>");
        }
        if (usuarioDto.getPersonaDto().getGenero() == 0) {
            listMessages.add("<li>Género</li>");
        }
        if (Util.getVacio(usuarioDto.getPersonaDto().getFechaNacimiento())) {
            listMessages.add("<li>Fecha de nacimiento</li>");
        }
        if (Util.getVacio(usuarioDto.getPersonaDto().getMovil())) {
            listMessages.add("<li>Número móvil</li>");
        }
        if (Util.getVacio(usuarioDto.getPersonaDto().getDireccion())) {
            listMessages.add("<li>Dirección domicilio</li>");
        }
        if (Util.getVacio(usuarioDto.getPersonaDto().getBarrio())) {
            listMessages.add("<li>Barrio domicilio</li>");
        }
        if (Util.getVacio(String.valueOf(usuarioDto.getTipoUsuario())) || usuarioDto.getTipoUsuario() == 0) {
            listMessages.add("<li>Perfil del usuario</li>");
        }
        if (Util.getVacio(String.valueOf(usuarioDto.getYnActivo())) || usuarioDto.getYnActivo() == 0) {
            listMessages.add("<li>Campo activo</li>");
        }
        return listMessages;
    }

    /**
     *
     * @param usuarioDto
     * @return
     * @throws SQLException
     */
    public String setGuardarDatosUsuario(UsuarioDto usuarioDto) throws SQLException {
        String password = "";
        if (this.model.setGuardarDatosUsuario(usuarioDto)) {
            if (Util.getVacio(usuarioDto.getFechaModificacion())) {
                password = usuarioDto.getPassword();
            }
        }
        return password;
    }

    /**
     * @tutorial Method Description: valida que la informacion este correcta
     * @author Eminson Mendoza ~~ emimaster16@gmail.com
     * @date 08/07/2016
     * @param clienteDto
     * @param guarda
     * @return
     * @throws SQLException
     */
    public List<String> setGuardarCliente(ClienteDto clienteDto, boolean guarda) throws SQLException {
        List<String> listMessages = new ArrayList();
        if (Util.getVacio(clienteDto.getPersonaDto().getPrimerNombre())) {
            listMessages.add("<li>Primer nombre</li>");
        }
        if (Util.getVacio(clienteDto.getPersonaDto().getPrimerApellido())) {
            listMessages.add("<li>Primer appelido</li>");
        }
        if (Util.getVacio(String.valueOf(clienteDto.getPersonaDto().getTipoIdentificacion())) || clienteDto.getPersonaDto().getTipoIdentificacion() == 0) {
            listMessages.add("<li>Tipo documento</li>");
        }
        if (Util.getVacio(clienteDto.getPersonaDto().getNumeroIdentificacion())) {
            listMessages.add("<li>Número de documento</li>");
        }
        
        if (clienteDto.getPersonaDto().getGenero() == 0) {
            listMessages.add("<li>Género</li>");
        }
        if (Util.getVacio(clienteDto.getPersonaDto().getFechaNacimiento())) {
            listMessages.add("<li>Fecha de nacimiento</li>");
        }
        if (Util.getVacio(clienteDto.getPersonaDto().getMovil())) {
            listMessages.add("<li>Número móvil</li>");
        }
        /*
        if (Util.getVacio(clienteDto.getPersonaDto().getDireccion())) {
            listMessages.add("<li>Dirección domicilio</li>");
        }
        
        if (Util.getVacio(clienteDto.getPersonaDto().getBarrio())) {
            listMessages.add("<li>Barrio domicilio</li>");
        }
        */
        if (clienteDto.getPersonaDto().getHuellaDactilar() == null && guarda) {//HABILITAR CUANDO SE TENGA EL HUELLERO
            listMessages.add("<li>Huella dactilar</li>");
        }
        
        if (listMessages.size() < 1 && guarda) {
            this.model.setGuardarCliente(clienteDto);
        }
        return listMessages;
    }
    
    public List<ComboDto> getPerfiles() throws Exception {
        List<ComboDto> lista = new ArrayList();
        for (EPerfiles tip : EPerfiles.getValues()) {
            ComboDto dto = new ComboDto(String.valueOf(tip.getId()), tip.getNombre());
            lista.add(dto);
        }
        return lista;
    }
    
    public List<ComboDto> getTipoDocumentos() throws Exception {
        List<ComboDto> lista = new ArrayList();
        for (ETipoDocumento tip : ETipoDocumento.getValues()) {
            ComboDto dto = new ComboDto(String.valueOf(tip.getId()), tip.getNombre());
            lista.add(dto);
        }
        return lista;
    }

    /**
     *
     * @param paquete
     * @return
     * @throws SQLException
     */
    public boolean setGuardarPaquete(PaqueteDto paquete) throws SQLException {
        return this.model.setGuardarPaquete(paquete);
    }

    /**
     * @throws java.sql.SQLException
     * @tutorial valida el ingreso de los usuarios a ala plataforma
     * @param loggin
     * @param password
     * @return
     */
    public UsuarioDto setValidateIngreso(String loggin, String password) throws SQLException {
        UsuarioDto user = new UsuarioDto();
        List<UsuarioDto> listUsuarios = this.model.getUsuariosDatos(loggin);
        if (listUsuarios.size() > 0) {
            user = listUsuarios.get(0);
            if (Util.getEncriptarMD5(password).equals(user.getPassword())) {
                return listUsuarios.get(0);
            } else {
                user = new UsuarioDto();
                JLabel label = new JLabel("Usuario o contraseña incorrecta");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(null, label, "Mensaje de Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JLabel label = new JLabel("Usuario o contraseña incorrecta");
            label.setFont(new Font("consolas", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(null, label, "Mensaje de Advertencia", JOptionPane.WARNING_MESSAGE);
        }
        return user;
    }

    /**
     *
     * @param idPaquete
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getPaquetesDatosTablaDto(String idPaquete) throws SQLException {
        String tipoPlan;
        List<TablaDto> listTable = new ArrayList();
        List<PaqueteDto> listPaquetes = this.model.getPaquetesDatos(idPaquete);
        for (PaqueteDto paquete : listPaquetes) {
            tipoPlan = paquete.getTipo() > 0 ? ETipoPlan.getResult(paquete.getTipo()).getNombre() : null;
            TablaDto tabla = new TablaDto(
                    String.valueOf(paquete.getId()),
                    Util.getQuitaNULL(paquete.getNombre()),
                    tipoPlan,
                    String.valueOf(paquete.getPrecioBase()),
                    (paquete.getYnTiquetera() == ESiNo.SI.getId() ? "Si" : "No"),
                    String.valueOf(paquete.getDiasAplazamiento())
            );
            listTable.add(tabla);
        }
        return listTable;
    }

    /**
     *
     * @param idCliente
     * @param documento
     * @param idClientePaquete
     * @return
     * @throws java.sql.SQLException
     */
    public ClientePaqueteDto getPaqueteActivoCliente(String idCliente, String documento, String idClientePaquete) throws SQLException {
        return this.model.getPaqueteActivoCliente(idCliente, documento, idClientePaquete);
    }

    /**
     *
     * @param gasto
     * @param usuario_id
     * @return
     * @throws SQLException
     */
    public boolean setSaveGastos(GastoDto gasto, String usuario_id) throws SQLException {
        boolean guarda = false;
        if (Util.getVacio(gasto.getDescripcion())) {
            JOptionPane.showMessageDialog(null, "El campo para la Descripcón del gasto es obligatorio");
        } else if (Util.getVacio(String.valueOf(gasto.getValor()))) {
            JOptionPane.showMessageDialog(null, "El campo para el Valor del gasto es obligatorio");
        } else {
            guarda = this.model.setGuardarGastos(gasto, usuario_id);
        }
        return guarda;
    }

    /**
     *
     * @param idGasto
     * @return
     * @throws SQLException
     */
    public List<TablaDto> getGastosDatosTablaDto(String idGasto) throws SQLException {
        List<TablaDto> listTable = new ArrayList();
        List<GastoDto> listGastos = this.model.getDatosGastos(idGasto, true);
        listGastos.stream().map((gasto) -> new TablaDto(
                String.valueOf(gasto.getId()),
                Util.getQuitaNULL(gasto.getDescripcion()),
                String.valueOf(gasto.getValor()))).forEach((tabla) -> {
                    listTable.add(tabla);
                });
        return listTable;
    }
    
    /**
     * 
     * @param idCafeteria
     * @return
     * @throws SQLException 
     */
    public List<TablaDto> getCafeteriaDatosTablaDto(String fecha) throws SQLException {
        List<TablaDto> listTable = new ArrayList();
        List<ProductoDto> listGastos = this.model.getDatosCafeteria(fecha);
        listGastos.stream().map((producto) -> new TablaDto(
                String.valueOf(producto.getId()),
                Util.getQuitaNULL(producto.getNombre()),
                String.valueOf(producto.getCantidad()),
                String.valueOf(producto.getPago()),
                String.valueOf(producto.getPrecio()))).forEach((tabla) -> {
                    listTable.add(tabla);
                });
        return listTable;
    }
    
    /**
     * 
     * @param idCafeteria
     * @return
     * @throws SQLException 
     */
    public ProductoDto getCafeteriaDto(String idCafeteria) throws SQLException {       
        ProductoDto producto = this.model.getDatosCafeteriaDto(idCafeteria, true);     
        return producto;
    }
    
    /**
     * 
     * @param idCafeteria
     * @return
     * @throws SQLException 
     */
    public String getFechaCierre() throws SQLException {       
        String fecha = this.model.getFechaCierre();     
        return fecha;
    }
    
    /**
     * 
     * @return
     * @throws SQLException 
     */
    public boolean setFechaCierre() throws SQLException {       
        return this.model.setFechaCierre();             
    }
    /**
     * 
     * @param idCliente
     * @param documento
     * @return
     * @throws SQLException 
     */
    public List<TablaDto> getPaquetesClienteDatosTablaDto(String idCliente, String documento) throws SQLException {
        List<TablaDto> listTable = new ArrayList();
        List<ClientePaqueteDto> listPaquetes = this.model.getListPaqueteActivoCliente(idCliente, documento);                
        
        listPaquetes.stream().map((paquete) -> new TablaDto(
                String.valueOf(paquete.getId()), 
                Util.getQuitaNULL(paquete.getPaqueteDto().getNombre()), 
                String.valueOf(paquete.getValorTotal()),
                String.valueOf(paquete.getFechaIniciaPaquete()), 
                String.valueOf(paquete.getFechaFinalizaPaquete()))).forEach((tabla) -> {
                    listTable.add(tabla);
                });
        
        return listTable;
    }
    
    /**
     * 
     * @param idCliente
     * @return 
     */
    public Long getLastIdClientePaquete(String idCliente) {
        
        Long idClintePaquete = (long) 0;
        
        try {
            idClintePaquete = this.model.getLastIdClientePaquete(idCliente);
            
        } catch (SQLException ex) {
            Logger.getLogger(Operaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return idClintePaquete;
        
    }
    
    public Long getLastIdCafeteria() {        
        Long idClintePaquete = (long) 0;        
        try {
            idClintePaquete = this.model.getLastIdCafetaria();
            
        } catch (SQLException ex) {
            Logger.getLogger(Operaciones.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return idClintePaquete;
        
    }
    
    /**
     * 
     * @param idPaquete 
     * @return  
     * @throws java.sql.SQLException 
     */
    public boolean deleteClientePaquete(String idPaquete) throws SQLException {
        return this.model.deleteClientePaquete(idPaquete);
    }
    
    /**
     * 
     * @param idPaquete 
     * @return  
     * @throws java.sql.SQLException 
     */
    public boolean deleteGasto(String idPaquete) throws SQLException {
        return this.model.deleteGasto(idPaquete);
    }
    
    public boolean deleteVenta(String idPaquete) throws SQLException {
        return this.model.deleteVenta(idPaquete);
    }
    
    public Model getModel() {
        return model;
    }
    
    public void setModel(Model model) {
        this.model = model;
    }
    
    public Conexion getConexion() {
        return conexion;
    }
    
    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }
    
}
