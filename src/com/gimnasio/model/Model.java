package com.gimnasio.model;

import com.gimnasio.model.enums.EEstadoPlan;
import com.gimnasio.model.enums.ESiNo;
import com.gimnasio.util.Util;
import com.google.common.base.Joiner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author emimaster16
 */
public class Model {

    private List<Object> listPersist;
    private Conexion conexion;

    public List<UsuarioDto> getDatosUsuariosSistema(String nombres, String apellidos, String documento, String limite, List<String> llaves) throws SQLException {
        List<UsuarioDto> list = new ArrayList();
        try (Statement stat = this.conexion.getConexion().createStatement()) {
            String sql = "SELECT usu.*, ps.*, usu.id AS idUsuario, ps.id AS idPersona "
                    + " FROM usuarios usu"
                    + " INNER JOIN personas ps"
                    + " ON usu.persona_id = ps.id "
                    + " WHERE 1 ";
            if (!Util.getVacio(nombres)) {
                sql += " AND UPPER(CONCAT(ps.primer_nombre,' ',COALESCE(ps.segundo_nombre,''))) LIKE '%" + nombres.toUpperCase() + "%' ";
            }
            if (!Util.getVacio(apellidos)) {
                sql += " AND UPPER(CONCAT(ps.primer_apellido,' ',COALESCE(ps.segundo_apellido,''))) LIKE '%" + apellidos.toUpperCase() + "%' ";
            }
            if (!Util.getVacio(documento)) {
                sql += " AND ps.numero_identificacion LIKE '%" + documento + "%' ";
            }
            if (llaves != null && llaves.size() > 0) {
                sql += " AND usu.id IN (" + Joiner.on(",").join(llaves) + ")";
            }
            sql += " ORDER BY ps.fecha_registro DESC, ps.primer_nombre, ps.segundo_nombre, ps.primer_apellido,ps.segundo_apellido ";
            if (limite != null && !limite.toLowerCase().equals("todos")) {
                sql += "LIMIT " + limite;
            }
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                UsuarioDto dto = new UsuarioDto();
                dto.setId(res.getLong("idUsuario"));
                dto.setTipoUsuario(res.getShort("tipo_usuario"));
                dto.setLoggin(res.getString("loggin"));
                dto.setPassword(res.getString("password"));
                dto.setPersonaId(res.getLong("persona_id"));
                dto.setYnActivo(res.getShort("yn_activo"));
                dto.setFechaRegistro(res.getString("fecha_registro"));
                dto.setFechaModificacion(res.getString("fecha_modificacion"));
                dto.getPersonaDto().setId(res.getLong("idPersona"));
                dto.getPersonaDto().setPrimerNombre(res.getString("primer_nombre"));
                dto.getPersonaDto().setSegundoNombre(res.getString("segundo_nombre"));
                dto.getPersonaDto().setPrimerApellido(res.getString("primer_apellido"));
                dto.getPersonaDto().setSegundoApellido(res.getString("segundo_apellido"));
                dto.getPersonaDto().setNumeroIdentificacion(res.getString("numero_identificacion"));
                dto.getPersonaDto().setFechaNacimiento(res.getString("fecha_nacimiento"));
                dto.getPersonaDto().setGenero(res.getShort("genero"));
                dto.getPersonaDto().setMovil(res.getString("movil"));
                dto.getPersonaDto().setTelefono(res.getString("telefono"));
                dto.getPersonaDto().setEmail(res.getString("email"));
                list.add(dto);
            }
        }
        return list;
    }

    /**
     *
     * @param paqueteDto
     * @param idUsuario
     * @return
     * @throws SQLException
     */
    public boolean setInsertarIngresoCliente(ClientePaqueteDto paqueteDto, Long idUsuario) throws SQLException {
        boolean correct;
        try {
            Statement stat = this.conexion.getConexion().createStatement();
            stat.execute("INSERT INTO cliente_ingresos ( cliente_paquete_id, cliente_id, fecha_ingreso, usuario_id )  VALUES ( '" + paqueteDto.getId() + "', '" + paqueteDto.getClienteDto().getId() + "', NOW(), '" + idUsuario + "' )");
            stat.close();
        } catch (SQLException ex) {
            correct = false;
            this.conexion.rollback();
            throw ex;
        } finally {
            correct = true;
            this.conexion.commit();
        }
        return correct;
    }

    /**
     *
     * @param idCliente
     * @return
     * @throws SQLException
     */
    public List<ClienteIngresoDto> getClientesIngresosDia(String idCliente) throws SQLException {
        List<ClienteIngresoDto> listIngresos = new ArrayList();
        try {
            Statement stat;
            ClienteIngresoDto clienteDto;
            stat = this.conexion.getConexion().createStatement();
            String sql = "SELECT * FROM cliente_ingresos WHERE DATE_FORMAT(fecha_ingreso, '%Y-%m-%d') = DATE_FORMAT(NOW(), '%Y-%m-%d') ";
            if (!Util.getVacio(idCliente)) {
                sql += " AND cliente_id = " + idCliente;
            }
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                clienteDto = new ClienteIngresoDto();
                clienteDto.setId(res.getLong("id"));
                clienteDto.setClientePaqueteId(res.getLong("cliente_paquete_id"));
                clienteDto.setClienteId(res.getLong("cliente_id"));
                clienteDto.setFechaIngreso(res.getString("fecha_ingreso"));
                clienteDto.setUsuarioId(res.getLong("usuario_id"));
                listIngresos.add(clienteDto);
            }
            stat.close();
        } catch (SQLException e) {
            this.conexion.rollback();
            throw e;
        } finally {
            this.conexion.commit();

        }
        return listIngresos;
    }

    /**
     *
     * @param fechaInicio
     * @param fechaFin
     * @param soloFin
     * @return
     * @throws SQLException
     */
    public List<ClientePaqueteDto> getListPaquetesActivosClientes(String fechaInicio, String fechaFin, boolean soloFin) throws SQLException {
        List<ClientePaqueteDto> listPaquete = new ArrayList();
        ClientePaqueteDto paquete;
        try {
            Statement stat;
            stat = this.conexion.getConexion().createStatement();
            String sql = "SELECT cp.*, pqt.id AS idPaquete, pqt.nombre AS nombrePaquete, pqt.tipo, pqt.precio_base AS precioBasePaquete, pqt.yn_tiquetera, pqt.dias_aplazamiento, "
                    + " per.id AS idPersona, per.primer_nombre, per.segundo_nombre, per.primer_apellido, per.segundo_apellido "
                    + " FROM cliente_paquete cp "
                    + " INNER JOIN clientes cl ON cp.cliente_id = cl.id "
                    + " INNER JOIN personas per ON cl.persona_id = per.id "
                    + " INNER JOIN paquetes pqt ON cp.paquete_id = pqt.id "
                    + " WHERE cp.estado=" + EEstadoPlan.ACTIVO.getId() + " "
                    + " AND pqt.yn_tiquetera <> " + ESiNo.SI.getId() + " ";
            if (!Util.getVacio(fechaInicio)) {
                sql += " AND cp.fecha_inicia_paquete ='" + fechaInicio + "' ";
            }
            if (!Util.getVacio(fechaFin) && soloFin) {
                sql += " AND cp.fecha_finaliza_paquete < '" + fechaFin + "' ";
            }
            sql += "ORDER BY cp.fecha_inicia_paquete DESC ";
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                paquete = new ClientePaqueteDto();
                paquete.setId(res.getLong("id"));
                paquete.setClienteId(res.getLong("cliente_id"));
                paquete.setPaqueteId(res.getLong("paquete_id"));
                paquete.setDescuentoId(res.getLong("descuento_id"));
                paquete.setNumeroDiasTiquetera(res.getShort("numero_dias_tiquetera"));
                paquete.setDiasUsadosTiquetera(res.getShort("dias_usados_tiquetera"));

                paquete.setPrecioBase(res.getDouble("precio_base"));
                paquete.setValorTotal(res.getDouble("valor_total"));
                paquete.setEstado(res.getShort("estado"));
                paquete.setFechaIniciaPaquete(res.getString("fecha_inicia_paquete"));
                paquete.setFechaFinalizaPaquete(res.getString("fecha_finaliza_paquete"));
                listPaquete.add(paquete);
            }
            stat.close();
        } catch (SQLException e) {
            this.conexion.rollback();
            throw e;
        } finally {
            this.conexion.commit();
        }
        return listPaquete;
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
        ClientePaqueteDto paquete = new ClientePaqueteDto();
        try {
            Statement stat;
            stat = this.conexion.getConexion().createStatement();
            String sql = "SELECT cp.*, pqt.id AS idPaquete, pqt.nombre AS nombrePaquete, pqt.tipo, pqt.precio_base AS precioBasePaquete, pqt.yn_tiquetera, pqt.dias_aplazamiento, "
                    + " per.id AS idPersona, per.primer_nombre, per.segundo_nombre, per.primer_apellido, per.segundo_apellido "
                    + " FROM cliente_paquete cp "
                    + " INNER JOIN clientes cl ON cp.cliente_id = cl.id "
                    + " INNER JOIN personas per ON cl.persona_id = per.id "
                    + " INNER JOIN paquetes pqt ON cp.paquete_id = pqt.id "
                    + " WHERE cp.estado=" + EEstadoPlan.ACTIVO.getId() + " ";
            if (!Util.getVacio(idCliente)) {
                sql += " AND cl.id =" + idCliente + " ";
            }
            if (!Util.getVacio(documento)) {
                sql += " AND per.numero_identificacion ='" + documento + "' ";
            }
            if (!Util.getVacio(idClientePaquete)) {
                sql += " AND cp.id ='" + idClientePaquete + "' ";
            }
            sql += "ORDER BY cp.fecha_inicia_paquete ASC ";
            sql += "LIMIT 1";
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                paquete.setId(res.getLong("id"));
                paquete.setTipoPago(res.getShort("tipo_pago"));
                paquete.setClienteId(res.getLong("cliente_id"));
                paquete.setPaqueteId(res.getLong("paquete_id"));
                paquete.setDescuentoId(res.getLong("descuento_id"));
                paquete.setNumeroDiasTiquetera(res.getShort("numero_dias_tiquetera"));
                paquete.setDiasUsadosTiquetera(res.getShort("dias_usados_tiquetera"));

                paquete.setPrecioBase(res.getDouble("precio_base"));
                paquete.setValorTotal(res.getDouble("valor_total"));
                paquete.setEstado(res.getShort("estado"));
                paquete.setFechaIniciaPaquete(res.getString("fecha_inicia_paquete"));
                paquete.setFechaFinalizaPaquete(res.getString("fecha_finaliza_paquete"));

                paquete.getPaqueteDto().setId(res.getInt("idPaquete"));
                paquete.getPaqueteDto().setNombre(res.getString("nombrePaquete"));
                paquete.getPaqueteDto().setTipo(res.getShort("tipo"));
                paquete.getPaqueteDto().setPrecioBase(res.getDouble("precioBasePaquete"));
                paquete.getPaqueteDto().setYnTiquetera(res.getShort("yn_tiquetera"));
                paquete.getPaqueteDto().setDiasAplazamiento(res.getShort("dias_aplazamiento"));

                paquete.getClienteDto().getPersonaDto().setId(res.getLong("idPersona"));
                paquete.getClienteDto().getPersonaDto().setPrimerNombre(res.getString("primer_nombre"));
                paquete.getClienteDto().getPersonaDto().setSegundoNombre(res.getString("segundo_nombre"));
                paquete.getClienteDto().getPersonaDto().setPrimerApellido(res.getString("primer_apellido"));
                paquete.getClienteDto().getPersonaDto().setSegundoApellido(res.getString("segundo_apellido"));

            }
            stat.close();
        } catch (SQLException e) {
            this.conexion.rollback();
            throw e;
        } finally {
            this.conexion.commit();
        }
        return paquete;
    }
    
    /**
     *
     * @param clientePaqueteDto
     * @param registraAsistencia
     * @return
     * @throws SQLException
     */
    public boolean setGuardaPagoPaqueteCliente(ClientePaqueteDto clientePaqueteDto, boolean registraAsistencia) throws SQLException {
        boolean correct;
        try {
            String sql = "";
            if (clientePaqueteDto.getId() != null && clientePaqueteDto.getId() > 0) {
                sql = "UPDATE  cliente_paquete  SET "
                        + "cliente_id = '" + clientePaqueteDto.getClienteId() + "', paquete_id = '" + clientePaqueteDto.getPaqueteId() + "', ";
                sql += "descuento_id =" + ((clientePaqueteDto.getDescuentoId() == null || clientePaqueteDto.getDescuentoId() == 0) ? "(NULL)" : clientePaqueteDto.getDescuentoId()) + ", ";
                sql += "numero_dias_tiquetera = '" + clientePaqueteDto.getNumeroDiasTiquetera() + "',  "
                        + "precio_base = '" + clientePaqueteDto.getPrecioBase() + "', valor_total = '" + clientePaqueteDto.getValorTotal() + "', "
                        + "estado = '" + clientePaqueteDto.getEstado() + "', fecha_inicia_paquete = '" + clientePaqueteDto.getFechaIniciaPaquete() + "', "
                        + "fecha_finaliza_paquete = " + (clientePaqueteDto.getFechaFinalizaPaquete() == null ? "NULL" : "'" + clientePaqueteDto.getFechaFinalizaPaquete() + "'") + ", usuario_id = '" + clientePaqueteDto.getUsuarioId() + "', "
                        + "fecha_modificacion = NOW(), "
                        + "tipo_pago = " + clientePaqueteDto.getTipoPago()
                        + " WHERE id = '" + clientePaqueteDto.getId() + "' ;";
            } else {
                sql = "INSERT INTO cliente_paquete "
                        + "(cliente_id, paquete_id, ";
                if (clientePaqueteDto.getDescuentoId() != null) {
                    sql += "descuento_id, ";
                }
                sql += "numero_dias_tiquetera, "
                        + "precio_base, valor_total, tipo_pago, "
                        + "estado, fecha_inicia_paquete, "
                        + "fecha_finaliza_paquete, usuario_id, "
                        + "fecha_registro, fecha_modificacion )  "
                        + "VALUES ('" + clientePaqueteDto.getClienteId() + "','" + clientePaqueteDto.getPaqueteId() + "',";
                if (clientePaqueteDto.getDescuentoId() != null) {
                    sql += "'" + (clientePaqueteDto.getDescuentoId() == null ? "" : clientePaqueteDto.getDescuentoId()) + "',";
                }
                sql += "'" + clientePaqueteDto.getNumeroDiasTiquetera() + "',"
                        + "'" + clientePaqueteDto.getPrecioBase() + "',"
                        + "'" + clientePaqueteDto.getValorTotal() + "',"
                        + "'" + clientePaqueteDto.getTipoPago() + "',"
                        + "'" + clientePaqueteDto.getEstado() + "',"
                        + "'" + clientePaqueteDto.getFechaIniciaPaquete() + "',"
                        + (clientePaqueteDto.getFechaFinalizaPaquete() == null ? "NULL" : "'" + clientePaqueteDto.getFechaFinalizaPaquete() + "'") + ","
                        + "'" + clientePaqueteDto.getUsuarioId() + "',"
                        + "NOW(), NOW())";

            }
            Statement stat = this.conexion.getConexion().createStatement();
            stat.execute(sql);
            stat.close();
        } catch (SQLException ex) {
            correct = false;
            this.conexion.rollback();
            throw ex;
        } finally {
            correct = true;
            this.conexion.commit();
        }
        return correct;
    }

    /**
     *
     * @param fisioterapia
     * @return
     * @throws java.sql.SQLException
     */
    public boolean getSaveFisioterapia(FisioterapiaDto fisioterapia) throws SQLException {
        PreparedStatement stat;
        boolean correct;
        try {
            stat = this.conexion.getConexion().prepareStatement("UPDATE clientes SET peso = ?, talla =?, muslo_ant =?, triceps =?, pectoral =?, siliaco =?, abdomen =?, test_mmss =?, test_mmii =?, test_uno=?, test_dos =?, test_tres =?, frecuencia_cardiaca = ? , tension_arterial = ?, peak_air = ?, observaciones =? WHERE id =? ");
            stat.setDouble(1, fisioterapia.getPeso());
            stat.setDouble(2, fisioterapia.getTalla());
            stat.setDouble(3, fisioterapia.getMuslo_ant());
            stat.setDouble(4, fisioterapia.getTriceps());
            stat.setDouble(5, fisioterapia.getPectoral());
            stat.setDouble(6, fisioterapia.getSiliaco());
            stat.setDouble(7, fisioterapia.getAbdomen());
            stat.setDouble(8, fisioterapia.getTest_mmss());
            stat.setDouble(9, fisioterapia.getTest_mmii());
            stat.setDouble(10, fisioterapia.getTest_uno());
            stat.setDouble(11, fisioterapia.getTest_dos());
            stat.setDouble(12, fisioterapia.getTest_tres());
            stat.setDouble(13, fisioterapia.getFrecuencia_cardiaca());
            stat.setDouble(14, fisioterapia.getTension_arterial());
            stat.setDouble(15, fisioterapia.getPeak_air());
            stat.setString(16, fisioterapia.getObservaciones());
            stat.setLong(17, fisioterapia.getClienteDto().getId());
            stat.execute();
            stat.close();
        } catch (SQLException ex) {
            correct = false;
            this.conexion.rollback();
            throw ex;
        } finally {
            correct = true;
            this.conexion.commit();
        }
        return correct;
    }

    /**
     *
     * @param documento
     * @return
     * @throws java.sql.SQLException
     */
    public FisioterapiaDto getFisioterapiaDto(String documento) throws SQLException {
        FisioterapiaDto fisioterapia = new FisioterapiaDto();
        try (Statement stat = this.conexion.getConexion().createStatement()) {
            String sql = "SELECT "
                    + "cl.id as id_cliente, "
                    + "cl.peso, "
                    + "cl.talla, "
                    + "cl.muslo_ant, "
                    + "cl.triceps, "
                    + "cl.pectoral, "
                    + "cl.siliaco, "
                    + "cl.abdomen, "
                    + "cl.test_mmss, "
                    + "cl.test_mmii, "
                    + "cl.test_uno, "
                    + "cl.test_dos, "
                    + "cl.test_tres, "
                    + "cl.frecuencia_cardiaca, "
                    + "cl.tension_arterial, "
                    + "cl.peak_air, "
                    + "cl.observaciones, "
                    + "ps.id, "
                    + "ps.numero_identificacion, "
                    + "ps.primer_nombre, "
                    + "ps.segundo_nombre, "
                    + "ps.primer_apellido, "
                    + "ps.segundo_apellido, "
                    + "ps.genero,"
                    + "ps.fecha_nacimiento"
                    + " FROM clientes cl"
                    + " INNER JOIN personas ps"
                    + " ON cl.persona_id = ps.id "
                    + " WHERE 1=1";
            if (!Util.getVacio(documento)) {
                sql += " AND ps.numero_identificacion LIKE '%" + documento + "%' ";
            }
            sql += " ORDER BY ps.id ASC ";
            ResultSet res = stat.executeQuery(sql);
            ClienteDto dto = new ClienteDto();
            PersonaDto persona = new PersonaDto();
            while (res.next()) {
                fisioterapia.setPeso(res.getDouble("peso"));
                fisioterapia.setTalla(res.getDouble("talla"));
                fisioterapia.setMuslo_ant(res.getDouble("muslo_ant"));
                fisioterapia.setTriceps(res.getDouble("triceps"));
                fisioterapia.setPectoral(res.getDouble("pectoral"));
                fisioterapia.setSiliaco(res.getDouble("siliaco"));
                fisioterapia.setAbdomen(res.getDouble("abdomen"));
                fisioterapia.setTest_uno(res.getDouble("test_uno"));
                fisioterapia.setTest_dos(res.getDouble("test_dos"));
                fisioterapia.setTest_tres(res.getDouble("test_tres"));
                fisioterapia.setTest_mmii(res.getDouble("test_mmii"));
                fisioterapia.setTest_mmss(res.getDouble("test_mmss"));
                fisioterapia.setFrecuencia_cardiaca(res.getDouble("frecuencia_cardiaca"));
                fisioterapia.setTension_arterial(res.getDouble("tension_arterial"));
                fisioterapia.setPeak_air(res.getDouble("peak_air"));
                fisioterapia.setObservaciones(res.getString("observaciones"));
                dto.setId(res.getLong("id_cliente"));
                persona.setId(res.getLong("id"));
                persona.setPrimerNombre(res.getString("primer_nombre"));
                persona.setSegundoNombre(res.getString("segundo_nombre"));
                persona.setPrimerApellido(res.getString("primer_apellido"));
                persona.setSegundoApellido(res.getString("segundo_apellido"));
                persona.setNumeroIdentificacion(res.getString("numero_identificacion"));
                persona.setFechaNacimiento(res.getString("fecha_nacimiento"));
                persona.setGenero(res.getShort("genero"));
            }
            dto.setPersonaDto(persona);
            fisioterapia.setClienteDto(dto);
        }
        return fisioterapia;
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
    public List<FisioterapiaDto> getFisioterapiaDto(String nombres, String apellidos, String documento, String limite, List<String> llaves) throws SQLException {
        List<FisioterapiaDto> list = new ArrayList<>();
        try (Statement stat = this.conexion.getConexion().createStatement()) {
            String sql = "SELECT "
                    + "cl.id as id_cliente, "
                    + "cl.peso, "
                    + "cl.talla, "
                    + "cl.muslo_ant, "
                    + "cl.triceps, "
                    + "cl.pectoral, "
                    + "cl.siliaco, "
                    + "cl.abdomen, "
                    + "cl.test_mmss, "
                    + "cl.test_mmii, "
                    + "cl.test_uno, "
                    + "cl.test_dos, "
                    + "cl.test_tres, "
                    + "cl.frecuencia_cardiaca, "
                    + "cl.tension_arterial, "
                    + "cl.peak_air, "
                    + "cl.observaciones, "
                    + "ps.id, "
                    + "ps.numero_identificacion, "
                    + "ps.primer_nombre, "
                    + "ps.segundo_nombre, "
                    + "ps.primer_apellido, "
                    + "ps.segundo_apellido, "
                    + "ps.genero,"
                    + "ps.fecha_nacimiento"
                    + " FROM clientes cl"
                    + " INNER JOIN personas ps"
                    + " ON cl.persona_id = ps.id "
                    + " WHERE 1=1";
            if (!Util.getVacio(nombres)) {
                sql += " AND UPPER(CONCAT(ps.primer_nombre,' ',COALESCE(ps.segundo_nombre,''))) LIKE '%" + nombres.toUpperCase() + "%' ";
            }
            if (!Util.getVacio(apellidos)) {
                sql += " AND UPPER(CONCAT(ps.primer_apellido,' ',COALESCE(ps.segundo_apellido,''))) LIKE '%" + apellidos.toUpperCase() + "%' ";
            }
            if (!Util.getVacio(documento)) {
                sql += " AND ps.numero_identificacion LIKE '%" + documento + "%' ";
            }
            if (llaves != null && llaves.size() > 0) {
                sql += " AND cl.id IN (" + Joiner.on(",").join(llaves) + ")";
            }
            sql += " ORDER BY ps.fecha_registro DESC, ps.primer_nombre, ps.segundo_nombre, ps.primer_apellido,ps.segundo_apellido ";
            if (limite != null && !limite.toLowerCase().equals("todos")) {
                sql += "LIMIT " + limite;
            }
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                FisioterapiaDto fisioterapia = new FisioterapiaDto();
                ClienteDto dto = new ClienteDto();
                PersonaDto persona = new PersonaDto();
                fisioterapia.setPeso(res.getDouble("peso"));
                fisioterapia.setTalla(res.getDouble("talla"));
                fisioterapia.setMuslo_ant(res.getDouble("muslo_ant"));
                fisioterapia.setTriceps(res.getDouble("triceps"));
                fisioterapia.setPectoral(res.getDouble("pectoral"));
                fisioterapia.setSiliaco(res.getDouble("siliaco"));
                fisioterapia.setAbdomen(res.getDouble("abdomen"));
                fisioterapia.setTest_uno(res.getDouble("test_uno"));
                fisioterapia.setTest_dos(res.getDouble("test_dos"));
                fisioterapia.setTest_tres(res.getDouble("test_tres"));
                fisioterapia.setTest_mmii(res.getDouble("test_mmii"));
                fisioterapia.setTest_mmss(res.getDouble("test_mmss"));
                fisioterapia.setFrecuencia_cardiaca(res.getDouble("frecuencia_cardiaca"));
                fisioterapia.setTension_arterial(res.getDouble("tension_arterial"));
                fisioterapia.setPeak_air(res.getDouble("peak_air"));
                fisioterapia.setObservaciones(res.getString("observaciones"));
                dto.setId(res.getLong("id_cliente"));
                persona.setId(res.getLong("id"));
                persona.setPrimerNombre(res.getString("primer_nombre"));
                persona.setSegundoNombre(res.getString("segundo_nombre"));
                persona.setPrimerApellido(res.getString("primer_apellido"));
                persona.setSegundoApellido(res.getString("segundo_apellido"));
                persona.setNumeroIdentificacion(res.getString("numero_identificacion"));
                persona.setFechaNacimiento(res.getString("fecha_nacimiento"));
                persona.setGenero(res.getShort("genero"));
                dto.setPersonaDto(persona);
                fisioterapia.setClienteDto(dto);
                list.add(fisioterapia);
            }
        }
        return list;
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
    public List<ClienteDto> getDatosClientes(String nombres, String apellidos, String documento, String limite, List<String> llaves) throws SQLException {
        List<ClienteDto> list = new ArrayList();
        try (Statement stat = this.conexion.getConexion().createStatement()) {
            String sql = "SELECT cl.*, ps.*, cl.id AS idCliente, ps.id AS idPersona "
                    + " FROM clientes cl"
                    + " INNER JOIN personas ps"
                    + " ON cl.persona_id = ps.id "
                    + " WHERE 1 ";
            if (!Util.getVacio(nombres)) {
                sql += " AND UPPER(CONCAT(ps.primer_nombre,' ',COALESCE(ps.segundo_nombre,''))) LIKE '%" + nombres.toUpperCase() + "%' ";
            }
            if (!Util.getVacio(apellidos)) {
                sql += " AND UPPER(CONCAT(ps.primer_apellido,' ',COALESCE(ps.segundo_apellido,''))) LIKE '%" + apellidos.toUpperCase() + "%' ";
            }
            if (!Util.getVacio(documento)) {
                sql += " AND ps.numero_identificacion LIKE '%" + documento + "%' ";
            }
            if (llaves != null && llaves.size() > 0) {
                sql += " AND cl.id IN (" + Joiner.on(",").join(llaves) + ")";
            }
            sql += " ORDER BY ps.fecha_registro DESC, ps.primer_nombre, ps.segundo_nombre, ps.primer_apellido,ps.segundo_apellido ";
            if (limite != null && !limite.toLowerCase().equals("todos")) {
                sql += "LIMIT " + limite;
            }
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                ClienteDto dto = new ClienteDto();
                PersonaDto persona = new PersonaDto();
                dto.setId(res.getLong("idCliente"));
                persona.setId(res.getLong("idPersona"));
                persona.setPrimerNombre(res.getString("primer_nombre"));
                persona.setSegundoNombre(res.getString("segundo_nombre"));
                persona.setPrimerApellido(res.getString("primer_apellido"));
                persona.setSegundoApellido(res.getString("segundo_apellido"));
                persona.setNumeroIdentificacion(res.getString("numero_identificacion"));
                persona.setFechaNacimiento(res.getString("fecha_nacimiento"));
                persona.setGenero(res.getShort("genero"));
                persona.setMovil(res.getString("movil"));
                persona.setTelefono(res.getString("telefono"));
                persona.setEmail(res.getString("email"));
                dto.setPersonaDto(persona);
                list.add(dto);
            }
        }
        return list;
    }

    /**
     *
     * @param idProducto
     * @param idUsuario
     * @param cantidad
     * @param valor_total
     * @param fecha
     * @param tipo_pago
     * @return
     * @throws SQLException
     */
    public boolean setGuardarCafeteria(int idProducto, long idUsuario, String cantidad, String valor_total, Date fecha, int tipo_pago) throws SQLException {
        boolean correct = false;
        try {
            PreparedStatement stat;
            stat = this.conexion.getConexion().prepareStatement("INSERT INTO producto_ventas (productos_id, cantidad, valor_total, fecha_registro, usuario_id, tipo_pago) VALUES (?,?,?,?,?,?)");
            stat.setInt(1, idProducto);
            stat.setDouble(2, Double.valueOf(cantidad));
            stat.setDouble(3, Double.valueOf(valor_total));
            stat.setTimestamp(4, new java.sql.Timestamp(fecha.getTime()));
            stat.setInt(5, (int) idUsuario);
            stat.setInt(6, tipo_pago);
            correct = stat.execute();
            stat.close();
        } catch (SQLException ex) {
            correct = false;
            this.conexion.rollback();
            throw ex;
        } finally {
            correct = true;
            this.conexion.commit();
        }
        return correct;
    }
    
    /**
     * 
     * @param idProducto
     * @param idUsuario
     * @param cantidad
     * @param valor_total
     * @param fecha
     * @param tipo_pago
     * @return
     * @throws SQLException 
     */
    public boolean setUpdateCafeteria(int idCafeteria, int idProducto, long idUsuario, String cantidad, String valor_total, Date fecha, int tipo_pago) throws SQLException {
        boolean correct = false;
        try {
            PreparedStatement stat;
            stat = this.conexion.getConexion().prepareStatement("UPDATE producto_ventas SET productos_id = ?, cantidad = ?, valor_total = ?, fecha_registro = ?, usuario_id = ?, tipo_pago =? WHERE id= ? ");
            stat.setInt(1, idProducto);
            stat.setDouble(2, Double.valueOf(cantidad));
            stat.setDouble(3, Double.valueOf(valor_total));
            stat.setTimestamp(4, new java.sql.Timestamp(fecha.getTime()));
            stat.setInt(5, (int) idUsuario);
            stat.setInt(6, tipo_pago);
            stat.setInt(7, idCafeteria);
            correct = stat.execute();
            stat.close();
        } catch (SQLException ex) {
            correct = false;
            this.conexion.rollback();
            throw ex;
        } finally {
            correct = true;
            this.conexion.commit();
        }
        return correct;
    }
    /**
     *
     * @param idDescuentos
     * @return
     * @throws SQLException
     */
    public List<DescuentoDto> getDatosDescuentos(String idDescuentos) throws SQLException {
        List<DescuentoDto> list = new ArrayList();
        Statement stat = this.conexion.getConexion().createStatement();
        String sql = "SELECT * FROM descuentos WHERE 1";
        if (!Util.getVacio(idDescuentos)) {
            sql += " AND id=" + idDescuentos;

        }
        sql += " ORDER BY id ASC ";
        ResultSet res = stat.executeQuery(sql);
        while (res.next()) {
            DescuentoDto dto = new DescuentoDto();
            dto.setId(res.getInt("id"));
            dto.setNombre(res.getString("nombre"));
            dto.setPorcentaje(res.getBigDecimal("porcentaje"));
            list.add(dto);
        }

        return list;
    }

    /**
     *
     * @param descuento
     * @return
     * @throws SQLException
     */
    public boolean setGuardarDescuento(DescuentoDto descuento) throws SQLException {
        PreparedStatement stat;
        try {
            if (descuento.getId() != null && descuento.getId() > 0) {
                stat = this.conexion.getConexion().prepareStatement("UPDATE descuentos SET nombre = ?, porcentaje = ? WHERE id=?");
                stat.setInt(3, descuento.getId());
            } else {
                stat = this.conexion.getConexion().prepareStatement("INSERT INTO descuentos (nombre, porcentaje) VALUES (?,?)");
            }
            stat.setString(1, descuento.getNombre());
            stat.setDouble(2, descuento.getPorcentaje().doubleValue());
            stat.execute();
            stat.close();
        } catch (SQLException ex) {
            this.conexion.rollback();
            throw ex;
        } finally {
            this.conexion.commit();
        }
        return true;
    }

    /**
     *
     * @param gasto
     * @param usuario_id
     * @return
     * @throws SQLException
     */
    public boolean setGuardarGastos(GastoDto gasto, String usuario_id) throws SQLException {
        PreparedStatement stat;
        try {
            if (gasto.getId() != null && gasto.getId() > 0) {
                stat = this.conexion.getConexion().prepareStatement("UPDATE gastos SET descripcion = ?, valor = ?, usuario_id = ? WHERE id=?");
                stat.setInt(4, gasto.getId());
            } else {
                stat = this.conexion.getConexion().prepareStatement("INSERT INTO gastos (descripcion, valor, usuario_id) VALUES (?,?,?)");
            }
            stat.setString(1, gasto.getDescripcion());
            stat.setDouble(2, gasto.getValor());
            stat.setString(3, usuario_id);
            stat.execute();
            stat.close();
        } catch (SQLException ex) {
            this.conexion.rollback();
            throw ex;
        } finally {
            this.conexion.commit();
        }
        return true;
    }

    /**
     *
     * @param producto
     * @return
     * @throws SQLException
     */
    public boolean setGuardarProducto(ProductoDto producto) throws SQLException {
        PreparedStatement stat;
        try {
            if (producto.getId() != null && producto.getId() > 0) {
                stat = this.conexion.getConexion().prepareStatement("UPDATE productos SET nombre = ?, precio = ? WHERE id=?");
                stat.setInt(3, producto.getId());
            } else {
                stat = this.conexion.getConexion().prepareStatement("INSERT INTO productos (nombre, precio) VALUES (?,?)");
            }
            stat.setString(1, producto.getNombre());
            stat.setDouble(2, producto.getPrecio());
            stat.execute();
            stat.close();
        } catch (SQLException ex) {
            this.conexion.rollback();
            throw ex;
        } finally {
            this.conexion.commit();
        }
        return true;
    }

    /**
     *
     * @param idProducto
     * @return
     * @throws SQLException
     */
    public List<ProductoDto> getDatosProductos(String idProducto) throws SQLException {
        List<ProductoDto> list = new ArrayList();
        try (Statement stat = this.conexion.getConexion().createStatement()) {
            String sql = "SELECT * FROM productos WHERE 1";
            if (!Util.getVacio(idProducto)) {
                sql += " AND id=" + idProducto;
            }
            sql += " ORDER BY id ASC ";
            ResultSet res = stat.executeQuery(sql);
            ProductoDto dto;
            while (res.next()) {
                dto = new ProductoDto();
                dto.setId(res.getInt("id"));
                dto.setNombre(res.getString("nombre"));
                dto.setPrecio(res.getDouble("precio"));
                list.add(dto);
            }
        }
        return list;
    }
    
    
    public List<ClientePaqueteDto> getListPaqueteActivoCliente(String idCliente, String documento) throws SQLException {        
        List<ClientePaqueteDto> list = new ArrayList();
        try {
            Statement stat;
            stat = this.conexion.getConexion().createStatement();
            String sql = "SELECT "                    
                    + "cp.id, "
                    + "cp.valor_total, "
                    + "cp.fecha_inicia_paquete, "
                    + "cp.fecha_finaliza_paquete, "
                    + "pqt.id AS idPaquete, "
                    + "pqt.nombre AS nombrePaquete "
                    + " FROM cliente_paquete cp "
                    + " INNER JOIN clientes cl ON cp.cliente_id = cl.id "
                    + " INNER JOIN personas per ON cl.persona_id = per.id "
                    + " INNER JOIN paquetes pqt ON cp.paquete_id = pqt.id "
                    + " WHERE 1 ";
                    //+ " WHERE 1  cp.estado =" + EEstadoPlan.ACTIVO.getId() + " ";
            if (!Util.getVacio(idCliente)) {
                sql += " AND cl.id =" + idCliente + " ";
            }
            if (!Util.getVacio(documento)) {
                sql += " AND per.numero_identificacion ='" + documento + "' ";
            }
            sql += "ORDER BY cp.fecha_inicia_paquete DESC ";            
            ResultSet res = stat.executeQuery(sql);
            ClientePaqueteDto paquete;
            while (res.next()) {
                paquete = new ClientePaqueteDto();
                paquete.setId(res.getLong("id"));                            
                paquete.setValorTotal(res.getDouble("valor_total"));
                paquete.setFechaIniciaPaquete(res.getString("fecha_inicia_paquete"));
                paquete.setFechaFinalizaPaquete(res.getString("fecha_finaliza_paquete")); 
                
                paquete.getPaqueteDto().setId(res.getInt("idPaquete"));
                paquete.getPaqueteDto().setNombre(res.getString("nombrePaquete"));
                
                list.add(paquete);
            }
            stat.close();
        } catch (SQLException e) {
            this.conexion.rollback();
            throw e;
        } finally {
            this.conexion.commit();
        }
        return list;
    }

    /**
     *
     * @param idUsuario
     * @param numeroDocuemnto
     * @return
     * @throws SQLException
     */
    public List<UsuarioDto> getUsuarioPersonaDatos(String idUsuario, String numeroDocuemnto) throws SQLException {
        List<UsuarioDto> list = new ArrayList();
        Statement stat;
        stat = this.conexion.getConexion().createStatement();
        String sql = "SELECT usu.*, per.* FROM usuarios usu INNER JOIN personas per ON usu.persona_id=per.id WHERE 1 ";
        if (!Util.getVacio(idUsuario)) {
            sql += " AND usu.id=" + idUsuario;
        }
        if (!Util.getVacio(numeroDocuemnto)) {
            sql += " AND per.numero_identificacion='" + numeroDocuemnto + "'";
        }
        sql += " ORDER BY usu.id ASC ";
        ResultSet res = stat.executeQuery(sql);
        while (res.next()) {
            UsuarioDto dto = new UsuarioDto();
            dto.setId(res.getLong("id"));
            dto.setLoggin(res.getString("loggin"));
            dto.setPassword(res.getString("password"));
            dto.setPersonaId(res.getLong("persona_id"));
            dto.setTipoUsuario(res.getShort("tipo_usuario"));
            dto.setYnActivo(res.getShort("yn_activo"));
            dto.setFechaRegistro(res.getString("fecha_registro"));
            dto.setFechaModificacion(res.getString("fecha_modificacion"));

            dto.getPersonaDto().setId(res.getLong("persona_id"));
            dto.getPersonaDto().setPrimerNombre(res.getString("primer_nombre"));
            dto.getPersonaDto().setSegundoNombre(res.getString("segundo_nombre"));
            dto.getPersonaDto().setPrimerApellido(res.getString("primer_apellido"));
            dto.getPersonaDto().setSegundoApellido(res.getString("segundo_apellido"));
            dto.getPersonaDto().setTipoIdentificacion(res.getShort("tipo_identificacion"));
            dto.getPersonaDto().setNumeroIdentificacion(res.getString("numero_identificacion"));
            dto.getPersonaDto().setGenero(res.getShort("genero"));
            dto.getPersonaDto().setFechaNacimiento(res.getString("fecha_nacimiento"));
            dto.getPersonaDto().setDireccion(res.getString("direccion"));
            dto.getPersonaDto().setBarrio(res.getString("barrio"));
            dto.getPersonaDto().setTelefono(res.getString("telefono"));
            dto.getPersonaDto().setMovil(res.getString("movil"));
            dto.getPersonaDto().setEmail(res.getString("email"));
            dto.getPersonaDto().setHuellaDactilar(res.getBytes("huella_dactilar"));
            dto.getPersonaDto().setFotoPerfil(res.getString("foto_perfil"));
            dto.getPersonaDto().setFechaRegistro(res.getString("fecha_registro"));
            dto.getPersonaDto().setFechaModificacion(res.getString("fecha_modificacion"));
            list.add(dto);
        }
        stat.close();
        return list;
    }

    /**
     *
     * @tutorial Method Description: valida que la informacion este correcta
     * @author Eminson Mendoza ~~ emimaster16@gmail.com
     * @date 08/07/2016
     * @param usuarioDto
     * @return
     * @throws java.sql.SQLException
     */
    public boolean setGuardarDatosUsuario(UsuarioDto usuarioDto) throws SQLException {
        PreparedStatement stat;
        ResultSet res;
        boolean correct = false;
        try {
            if (usuarioDto.getId() != null && !Util.getVacio(String.valueOf(usuarioDto.getId()))) {
                stat = this.conexion.getConexion().prepareStatement("UPDATE personas SET primer_nombre = ?, segundo_nombre = ?, "
                        + "primer_apellido = ?, segundo_apellido = ?, "
                        + "tipo_identificacion = ?, numero_identificacion = ?, "
                        + "genero = ?, fecha_nacimiento = ?, "
                        + "direccion = ?, barrio = ?, "
                        + "telefono = ?, movil = ?, "
                        + "email = ?, foto_perfil = ?, fecha_modificacion=NOW() "
                        + "WHERE id=? ");
                stat.setString(1, usuarioDto.getPersonaDto().getPrimerNombre());
                stat.setString(2, usuarioDto.getPersonaDto().getSegundoNombre());
                stat.setString(3, usuarioDto.getPersonaDto().getPrimerApellido());
                stat.setString(4, usuarioDto.getPersonaDto().getSegundoApellido());
                stat.setShort(5, usuarioDto.getPersonaDto().getTipoIdentificacion());
                stat.setString(6, usuarioDto.getPersonaDto().getNumeroIdentificacion());
                stat.setShort(7, usuarioDto.getPersonaDto().getGenero());
                stat.setString(8, usuarioDto.getPersonaDto().getFechaNacimiento());
                stat.setString(9, usuarioDto.getPersonaDto().getDireccion());
                stat.setString(10, usuarioDto.getPersonaDto().getBarrio());
                stat.setString(11, usuarioDto.getPersonaDto().getTelefono());
                stat.setString(12, usuarioDto.getPersonaDto().getMovil());
                stat.setString(13, usuarioDto.getPersonaDto().getEmail());
                stat.setString(14, usuarioDto.getPersonaDto().getFotoPerfil());
                stat.setLong(15, usuarioDto.getPersonaDto().getId());
                correct = stat.executeUpdate() > 0;
            } else {
                stat = this.conexion.getConexion().prepareStatement("INSERT INTO personas (primer_nombre, segundo_nombre, "
                        + "primer_apellido, segundo_apellido, "
                        + "tipo_identificacion, numero_identificacion, "
                        + "genero, fecha_nacimiento, "
                        + "direccion, barrio, "
                        + "telefono, movil, "
                        + "email, foto_perfil, fecha_registro, "
                        + "fecha_modificacion) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW())", Statement.RETURN_GENERATED_KEYS);
                stat.setString(1, usuarioDto.getPersonaDto().getPrimerNombre());
                stat.setString(2, usuarioDto.getPersonaDto().getSegundoNombre());
                stat.setString(3, usuarioDto.getPersonaDto().getPrimerApellido());
                stat.setString(4, usuarioDto.getPersonaDto().getSegundoApellido());
                stat.setShort(5, usuarioDto.getPersonaDto().getTipoIdentificacion());
                stat.setString(6, usuarioDto.getPersonaDto().getNumeroIdentificacion());
                stat.setShort(7, usuarioDto.getPersonaDto().getGenero());
                stat.setString(8, usuarioDto.getPersonaDto().getFechaNacimiento());
                stat.setString(9, usuarioDto.getPersonaDto().getDireccion());
                stat.setString(10, usuarioDto.getPersonaDto().getBarrio());
                stat.setString(11, usuarioDto.getPersonaDto().getTelefono());
                stat.setString(12, usuarioDto.getPersonaDto().getMovil());
                stat.setString(13, usuarioDto.getPersonaDto().getEmail());
                stat.setString(14, usuarioDto.getPersonaDto().getFotoPerfil());
                if (stat.executeUpdate() > 0) {
                    res = stat.getGeneratedKeys();
                    if (res.next()) {
                        correct = true;
                        usuarioDto.getPersonaDto().setId(res.getLong(1));
                    }
                }
            }
            if (correct) {
                if (usuarioDto.getId() != null && !Util.getVacio(String.valueOf(usuarioDto.getId()))) {
                    stat = this.conexion.getConexion().prepareStatement("UPDATE  usuarios SET tipo_usuario = ?, persona_id = ?, loggin = ?, yn_activo = ?, fecha_modificacion=NOW() WHERE id = ?");
                    stat.setShort(1, usuarioDto.getTipoUsuario());
                    stat.setLong(2, usuarioDto.getPersonaDto().getId());
                    stat.setString(3, usuarioDto.getPersonaDto().getNumeroIdentificacion());
                    stat.setShort(4, usuarioDto.getYnActivo());
                    stat.setLong(5, usuarioDto.getId());
                    stat.executeUpdate();
                } else {
                    String password = String.valueOf(Util.setRandom(1000, 9999));
                    stat = this.conexion.getConexion().prepareStatement("INSERT INTO usuarios(tipo_usuario, persona_id, loggin, password, yn_activo, fecha_registro ) VALUES(?, ?, ?, ?, ?, NOW()) ", Statement.RETURN_GENERATED_KEYS);
                    stat.setShort(1, usuarioDto.getTipoUsuario());
                    stat.setLong(2, usuarioDto.getPersonaDto().getId());
                    stat.setString(3, usuarioDto.getPersonaDto().getNumeroIdentificacion());
                    stat.setString(4, Util.getEncriptarMD5(password));
                    stat.setShort(5, usuarioDto.getYnActivo());
                    if (stat.executeUpdate() > 0) {
                        usuarioDto.setPassword(password);
                        res = stat.getGeneratedKeys();
                        if (res.next()) {
                            correct = true;
                            usuarioDto.setId(res.getLong(1));
                        }
                    }
                }
            }
            stat.close();
        } catch (SQLException ex) {
            this.conexion.rollback();
            throw ex;
        } finally {
            this.conexion.commit();
        }
        return correct;
    }

    /**
     *
     * @param idCliente
     * @param numeroDocuemnto
     * @return
     * @throws SQLException
     */
    public List<ClienteDto> getClienteDatos(String idCliente, String numeroDocuemnto) throws SQLException {
        List<ClienteDto> list = new ArrayList();
        Statement stat;
        stat = this.conexion.getConexion().createStatement();
        String sql = "SELECT cli.*, per.* FROM clientes cli INNER JOIN personas per ON cli.persona_id=per.id WHERE 1 ";
        if (!Util.getVacio(idCliente)) {
            sql += " AND cli.id=" + idCliente;
        }
        if (!Util.getVacio(numeroDocuemnto)) {
            sql += " AND per.numero_identificacion='" + numeroDocuemnto + "'";
        }
        sql += " ORDER BY cli.id ASC ";
        ResultSet res = stat.executeQuery(sql);
        while (res.next()) {
            ClienteDto dto = new ClienteDto();
            dto.setId(res.getLong("id"));
            dto.getPersonaDto().setId(res.getLong("persona_id"));
            dto.getPersonaDto().setPrimerNombre(res.getString("primer_nombre"));
            dto.getPersonaDto().setSegundoNombre(res.getString("segundo_nombre"));
            dto.getPersonaDto().setPrimerApellido(res.getString("primer_apellido"));
            dto.getPersonaDto().setSegundoApellido(res.getString("segundo_apellido"));
            dto.getPersonaDto().setTipoIdentificacion(res.getShort("tipo_identificacion"));
            dto.getPersonaDto().setNumeroIdentificacion(res.getString("numero_identificacion"));
            dto.getPersonaDto().setGenero(res.getShort("genero"));
            dto.getPersonaDto().setFechaNacimiento(res.getString("fecha_nacimiento"));
            dto.getPersonaDto().setDireccion(res.getString("direccion"));
            dto.getPersonaDto().setBarrio(res.getString("barrio"));
            dto.getPersonaDto().setTelefono(res.getString("telefono"));
            dto.getPersonaDto().setMovil(res.getString("movil"));
            dto.getPersonaDto().setEmail(res.getString("email"));
            dto.getPersonaDto().setHuellaDactilar(res.getBytes("huella_dactilar"));
            dto.getPersonaDto().setFotoPerfil(res.getString("foto_perfil"));
            dto.getPersonaDto().setFechaRegistro(res.getString("fecha_registro"));
            dto.getPersonaDto().setFechaModificacion(res.getString("fecha_modificacion"));
            list.add(dto);
        }
        stat.close();
        return list;
    }

    /**
     *
     * @param numeroDocuemnto
     * @return
     * @throws SQLException
     */
    public List<PersonaDto> getDatosPersona(String numeroDocuemnto) throws SQLException {
        List<PersonaDto> list = new ArrayList();
        Statement stat;
        stat = this.conexion.getConexion().createStatement();
        String sql = "SELECT per.* FROM personas per WHERE 1 ";
        if (!Util.getVacio(numeroDocuemnto)) {
            sql += " AND per.numero_identificacion='" + numeroDocuemnto + "'";
        }
        ResultSet res = stat.executeQuery(sql);
        while (res.next()) {
            PersonaDto personaDto = new PersonaDto();
            personaDto.setId(res.getLong("id"));
            personaDto.setPrimerNombre(res.getString("primer_nombre"));
            personaDto.setSegundoNombre(res.getString("segundo_nombre"));
            personaDto.setPrimerApellido(res.getString("primer_apellido"));
            personaDto.setSegundoApellido(res.getString("segundo_apellido"));
            personaDto.setTipoIdentificacion(res.getShort("tipo_identificacion"));
            personaDto.setNumeroIdentificacion(res.getString("numero_identificacion"));
            personaDto.setGenero(res.getShort("genero"));
            personaDto.setFechaNacimiento(res.getString("fecha_nacimiento"));
            personaDto.setDireccion(res.getString("direccion"));
            personaDto.setBarrio(res.getString("barrio"));
            personaDto.setTelefono(res.getString("telefono"));
            personaDto.setMovil(res.getString("movil"));
            personaDto.setEmail(res.getString("email"));
            personaDto.setHuellaDactilar(res.getBytes("huella_dactilar"));
            personaDto.setFotoPerfil(res.getString("foto_perfil"));
            personaDto.setFechaRegistro(res.getString("fecha_registro"));
            personaDto.setFechaModificacion(res.getString("fecha_modificacion"));
            list.add(personaDto);
        }
        stat.close();
        return list;
    }

    /**
     *
     * @tutorial Method Description: valida que la informacion este correcta
     * @author Eminson Mendoza ~~ emimaster16@gmail.com
     * @date 08/07/2016
     * @param clienteDto
     * @throws java.sql.SQLException
     */
    public void setGuardarCliente(ClienteDto clienteDto) throws SQLException {
        PreparedStatement stat;
        ResultSet res;
        boolean correct = false;
        try {
            if (clienteDto.getId() != null && !Util.getVacio(String.valueOf(clienteDto.getId()))) {
                stat = this.conexion.getConexion().prepareStatement("UPDATE personas SET primer_nombre = ?, segundo_nombre = ?, "
                        + "primer_apellido = ?, segundo_apellido = ?, "
                        + "tipo_identificacion = ?, numero_identificacion = ?, "
                        + "genero = ?, fecha_nacimiento = ?, "
                        + "direccion = ?, barrio = ?, "
                        + "telefono = ?, movil = ?, "
                        + "email = ?, huella_dactilar = ?, "
                        + "foto_perfil = ?,fecha_modificacion=NOW() "
                        + "WHERE id=? ");
                stat.setString(1, clienteDto.getPersonaDto().getPrimerNombre());
                stat.setString(2, clienteDto.getPersonaDto().getSegundoNombre());
                stat.setString(3, clienteDto.getPersonaDto().getPrimerApellido());
                stat.setString(4, clienteDto.getPersonaDto().getSegundoApellido());
                stat.setShort(5, clienteDto.getPersonaDto().getTipoIdentificacion());
                stat.setString(6, clienteDto.getPersonaDto().getNumeroIdentificacion());
                stat.setShort(7, clienteDto.getPersonaDto().getGenero());
                stat.setString(8, clienteDto.getPersonaDto().getFechaNacimiento());
                stat.setString(9, clienteDto.getPersonaDto().getDireccion());
                stat.setString(10, clienteDto.getPersonaDto().getBarrio());
                stat.setString(11, clienteDto.getPersonaDto().getTelefono());
                stat.setString(12, clienteDto.getPersonaDto().getMovil());
                stat.setString(13, clienteDto.getPersonaDto().getEmail());
                stat.setBytes(14, clienteDto.getPersonaDto().getHuellaDactilar());
                stat.setString(15, clienteDto.getPersonaDto().getFotoPerfil());
                stat.setLong(16, clienteDto.getPersonaDto().getId());
                correct = stat.executeUpdate() > 0;
            } else {
                stat = this.conexion.getConexion().prepareStatement("INSERT INTO personas (primer_nombre, segundo_nombre, "
                        + "primer_apellido, segundo_apellido, "
                        + "tipo_identificacion, numero_identificacion, "
                        + "genero, fecha_nacimiento, "
                        + "direccion, barrio, "
                        + "telefono, movil, "
                        + "email, huella_dactilar, "
                        + "foto_perfil, fecha_registro, "
                        + "fecha_modificacion) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW())", Statement.RETURN_GENERATED_KEYS);
                stat.setString(1, clienteDto.getPersonaDto().getPrimerNombre());
                stat.setString(2, clienteDto.getPersonaDto().getSegundoNombre());
                stat.setString(3, clienteDto.getPersonaDto().getPrimerApellido());
                stat.setString(4, clienteDto.getPersonaDto().getSegundoApellido());
                stat.setShort(5, clienteDto.getPersonaDto().getTipoIdentificacion());
                stat.setString(6, clienteDto.getPersonaDto().getNumeroIdentificacion());
                stat.setShort(7, clienteDto.getPersonaDto().getGenero());
                stat.setString(8, clienteDto.getPersonaDto().getFechaNacimiento());
                stat.setString(9, clienteDto.getPersonaDto().getDireccion());
                stat.setString(10, clienteDto.getPersonaDto().getBarrio());
                stat.setString(11, clienteDto.getPersonaDto().getTelefono());
                stat.setString(12, clienteDto.getPersonaDto().getMovil());
                stat.setString(13, clienteDto.getPersonaDto().getEmail());
                stat.setBytes(14, clienteDto.getPersonaDto().getHuellaDactilar());
                stat.setString(15, clienteDto.getPersonaDto().getFotoPerfil());
                if (stat.executeUpdate() > 0) {
                    res = stat.getGeneratedKeys();
                    if (res.next()) {
                        correct = true;
                        clienteDto.getPersonaDto().setId(res.getLong(1));
                    }
                }
            }
            if (correct) {
                if (clienteDto.getId() != null && !Util.getVacio(String.valueOf(clienteDto.getId()))) {
                    stat = this.conexion.getConexion().prepareStatement("UPDATE  clientes SET persona_id = ? WHERE id = ?");
                    stat.setLong(1, clienteDto.getPersonaDto().getId());
                    stat.setLong(2, clienteDto.getId());
                    stat.executeUpdate();
                } else {
                    stat = this.conexion.getConexion().prepareStatement("INSERT INTO clientes(persona_id) " + "VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                    stat.setLong(1, clienteDto.getPersonaDto().getId());
                    if (stat.executeUpdate() > 0) {
                        res = stat.getGeneratedKeys();
                        if (res.next()) {
                            correct = true;
                            clienteDto.setId(res.getLong(1));
                        }
                    }
                }
            }
            stat.close();
        } catch (SQLException ex) {
            this.conexion.rollback();
            throw ex;
        } finally {
            this.conexion.commit();
        }
    }

    public boolean setGuardarPaquete(PaqueteDto paquete) throws SQLException {
        boolean correct = false;
        try {
            PreparedStatement stat;
            if (paquete.getId() > 0) {
                stat = this.conexion.getConexion().prepareStatement("UPDATE paquetes SET nombre = ?, tipo = ?, precio_base = ?, yn_tiquetera = ?, dias_aplazamiento = ? WHERE id=?");
                stat.setInt(6, paquete.getId());
            } else {
                stat = this.conexion.getConexion().prepareStatement("INSERT INTO paquetes (nombre, tipo, precio_base, yn_tiquetera, dias_aplazamiento) VALUES (?,?,?,?,?)");
            }
            stat.setString(1, paquete.getNombre());
            stat.setShort(2, paquete.getTipo());
            stat.setDouble(3, paquete.getPrecioBase());
            stat.setShort(4, paquete.getYnTiquetera());
            stat.setShort(5, paquete.getDiasAplazamiento());
            correct = stat.execute();
            stat.close();
        } catch (SQLException ex) {
            correct = false;
            this.conexion.rollback();
            throw ex;
        } finally {
            correct = true;
            this.conexion.commit();
        }
        return correct;
    }

    public List<UsuarioDto> getUsuariosDatos(String loggin) throws SQLException {
        List<UsuarioDto> list = new ArrayList();
        Statement stat = this.conexion.getConexion().createStatement();
        String sql = "SELECT usu.*, per.*, per.id as idPersona FROM usuarios usu INNER JOIN personas per ON usu.persona_id=per.id WHERE loggin='" + loggin + "'";
        ResultSet res = stat.executeQuery(sql);
        while (res.next()) {
            UsuarioDto dto = new UsuarioDto();
            dto.setId(res.getLong("id"));
            dto.setLoggin(res.getString("loggin"));
            dto.setPassword(res.getString("password"));
            dto.setPersonaId(res.getLong("persona_id"));
            dto.setTipoUsuario(res.getShort("tipo_usuario"));
            dto.setYnActivo(res.getShort("yn_activo"));

            dto.getPersonaDto().setId(res.getLong("idPersona"));
            dto.getPersonaDto().setPrimerNombre(res.getString("primer_nombre"));
            dto.getPersonaDto().setSegundoNombre(res.getString("segundo_nombre"));
            dto.getPersonaDto().setPrimerApellido(res.getString("primer_apellido"));
            dto.getPersonaDto().setSegundoApellido(res.getString("segundo_apellido"));
            dto.getPersonaDto().setTipoIdentificacion(res.getShort("tipo_identificacion"));
            dto.getPersonaDto().setNumeroIdentificacion(res.getString("tipo_identificacion"));
            dto.getPersonaDto().setGenero(res.getShort("genero"));
            dto.getPersonaDto().setGenero(res.getShort("tipo_identificacion"));
            dto.getPersonaDto().setFechaNacimiento(res.getString("fecha_nacimiento"));
            dto.getPersonaDto().setDireccion(res.getString("direccion"));
            dto.getPersonaDto().setBarrio(res.getString("barrio"));
            dto.getPersonaDto().setTelefono(res.getString("telefono"));
            dto.getPersonaDto().setMovil(res.getString("movil"));
            dto.getPersonaDto().setFotoPerfil(res.getString("foto_perfil"));
            dto.getPersonaDto().setFechaRegistro(res.getString("fecha_registro"));
            dto.getPersonaDto().setFechaModificacion(res.getString("fecha_modificacion"));
            list.add(dto);
        }
        stat.close();
        return list;
    }

    /**
     *
     * @param idPaquete
     * @return
     * @throws SQLException
     */
    public List<PaqueteDto> getPaquetesDatos(String idPaquete) throws SQLException {
        List<PaqueteDto> list = new ArrayList();
        Statement stat = this.conexion.getConexion().createStatement();
        String sql = "SELECT * FROM paquetes WHERE 1";
        if (!Util.getVacio(idPaquete)) {
            sql += " AND id=" + idPaquete;

        }
        sql += " ORDER BY id ASC ";
        ResultSet res = stat.executeQuery(sql);
        while (res.next()) {
            PaqueteDto dto = new PaqueteDto();
            dto.setId(res.getInt("id"));
            dto.setNombre(res.getString("nombre"));
            dto.setTipo(res.getShort("tipo"));
            dto.setPrecioBase(res.getDouble("precio_base"));
            dto.setYnTiquetera(res.getShort("yn_tiquetera"));
            dto.setDiasAplazamiento(res.getShort("dias_aplazamiento"));
            list.add(dto);
        }
        stat.close();
        return list;
    }

    public List<ClienteDto> getDatosClientes(String mes) throws SQLException {
        List<ClienteDto> list = new ArrayList();
        try (Statement stat = this.conexion.getConexion().createStatement()) {
            String sql = "SELECT cl.*, ps.*, cl.id AS idCliente, ps.id AS idPersona "
                    + " FROM clientes cl"
                    + " INNER JOIN personas ps"
                    + " ON cl.persona_id = ps.id "
                    + " WHERE 1 ";
            if (!Util.getVacio(mes)) {
                sql += " AND month(fecha_nacimiento) = " + mes;
            }
            sql += " ORDER BY day(fecha_nacimiento) ";
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                ClienteDto dto = new ClienteDto();
                PersonaDto persona = new PersonaDto();
                dto.setId(res.getLong("idCliente"));
                persona.setId(res.getLong("idPersona"));
                persona.setPrimerNombre(res.getString("primer_nombre"));
                persona.setSegundoNombre(res.getString("segundo_nombre"));
                persona.setPrimerApellido(res.getString("primer_apellido"));
                persona.setSegundoApellido(res.getString("segundo_apellido"));
                persona.setNumeroIdentificacion(res.getString("numero_identificacion"));
                persona.setFechaNacimiento(res.getString("fecha_nacimiento"));
                persona.setGenero(res.getShort("genero"));
                persona.setMovil(res.getString("movil"));
                persona.setTelefono(res.getString("telefono"));
                persona.setEmail(res.getString("email"));
                dto.setPersonaDto(persona);
                list.add(dto);
            }
        }
        return list;
    }

    /**
     * RP: se traen los gastos del dia
     *
     * @param idGastos
     * @param fecha
     * @return
     * @throws SQLException
     */
    public List<GastoDto> getDatosGastos(String idGastos, boolean fecha) throws SQLException {
        List<GastoDto> list = new ArrayList();
        Statement stat = this.conexion.getConexion().createStatement();

        String sql = "SELECT * FROM gastos WHERE 1 ";
        if (fecha) {
            sql += " AND CONCAT(year(fecha_registro),'-',month(fecha_registro),'-',day(fecha_registro)) = CONCAT(year(NOW()),'-',month(NOW()),'-',day(NOW())) ";
        }
        if (!Util.getVacio(idGastos)) {
            sql += " AND id=" + idGastos;

        }
        sql += " ORDER BY id ASC ";
        ResultSet res = stat.executeQuery(sql);
        while (res.next()) {
            GastoDto dto = new GastoDto();
            dto.setId(res.getInt("id"));
            dto.setDescripcion(res.getString("descripcion"));
            dto.setValor(res.getDouble("valor"));
            dto.setFechaRegistro(res.getString("fecha_registro"));
            list.add(dto);
        }

        return list;
    }
    
    /**
     * 
     * @param idGastos
     * @param fecha
     * @return
     * @throws SQLException 
     */
    public List<ProductoDto> getDatosCafeteria(String fecha) throws SQLException {
        List<ProductoDto> list = new ArrayList();
        Statement stat = this.conexion.getConexion().createStatement();

        String sql = "SELECT pv.*, p.nombre FROM producto_ventas pv "
                + " INNER JOIN productos p ON pv.productos_id = p.id "
                + " WHERE 1 ";
        if (!Util.getVacio(fecha)) {
            //AND pv.fecha_registro CONCAT(year(pv.fecha_registro),'-',month(pv.fecha_registro),'-',day(pv.fecha_registro)) = CONCAT(year(NOW()),'-',month(NOW()),'-',day(NOW())) 
            sql += " AND pv.fecha_registro >= '"+fecha +" 00:00:00' AND pv.fecha_registro  <= '"+fecha +" 23:59:59'";
        }

        sql += " ORDER BY pv.id ASC ";
        ResultSet res = stat.executeQuery(sql);
        while (res.next()) {
            ProductoDto dto = new ProductoDto();
            dto.setId(res.getInt("id"));
            dto.setNombre(res.getString("nombre"));
            dto.setCantidad(res.getInt("cantidad"));
            dto.setPrecio(res.getDouble("valor_total"));
            dto.setId_producto(res.getInt("productos_id"));            
            dto.setTipo_pago(res.getShort("tipo_pago"));
            dto.setPago(res.getInt("tipo_pago") == 1 ? "Efectivo" : "Tarjeta");
            list.add(dto);
        }

        return list;
    }
    
    /**
     * 
     * @param idGastos
     * @param fecha
     * @return
     * @throws SQLException 
     */
    public ProductoDto getDatosCafeteriaDto(String idGastos, boolean fecha) throws SQLException {
        Statement stat = this.conexion.getConexion().createStatement();

        String sql = "SELECT pv.*, p.nombre FROM producto_ventas pv "
                + " INNER JOIN productos p ON pv.productos_id = p.id "
                + " WHERE 1 ";

        if (!Util.getVacio(idGastos)) {
            sql += " AND pv.id= " + idGastos;

        }
        sql += " ORDER BY pv.id ASC ";
        ResultSet res = stat.executeQuery(sql);
        ProductoDto dto = new ProductoDto();
        while (res.next()) {            
            dto.setId(res.getInt("id"));
            dto.setNombre(res.getString("nombre"));
            dto.setCantidad(res.getInt("cantidad"));
            dto.setPrecio(res.getDouble("valor_total"));
            dto.setId_producto(res.getInt("productos_id"));   
            dto.setTipo_pago(res.getShort("tipo_pago"));
            dto.setPago(res.getInt("tipo_pago") == 1 ? "Efectivo" : "Tarjeta");
        }

        return dto;
    }
    
    /**
     * 
     * @return
     * @throws SQLException 
     */
    public String getFechaCierre() throws SQLException {
        Statement stat = this.conexion.getConexion().createStatement();
        String sql = "SELECT MAX(fecha_cierre) fecha FROM cierre_caja ";   
        ResultSet res = stat.executeQuery(sql);
        String ret = "";
        while (res.next()) {            
            ret = res.getString("fecha");
        }

        return ret;
    }
    
    
    /**
     * 
     * @return
     * @throws SQLException 
     */
    public boolean setFechaCierre() throws SQLException {
        PreparedStatement stat;
        try {                       
            stat = this.conexion.getConexion().prepareStatement("INSERT INTO cierre_caja (fecha_cierre) VALUES (NOW())");               
            stat.execute();
            stat.close();
        } catch (SQLException ex) {
            this.conexion.rollback();
            throw ex;
        } finally {
            this.conexion.commit();
        }
        return true;
    }
    
    /**
     * 
     * @param idCliente
     * @return
     * @throws SQLException 
     */
    public Long getLastIdClientePaquete(String idCliente) throws SQLException {        
        Long idCLientePaquete = (long) 0;
        try {
            Statement stat;
            stat = this.conexion.getConexion().createStatement();
            String sql = "SELECT "                    
                    + "Max(cp.id) id "
                    + " FROM cliente_paquete cp "              
                    + " WHERE cp.estado=" + EEstadoPlan.ACTIVO.getId() + " ";
            if (!Util.getVacio(idCliente)) {
                sql += " AND cp.cliente_id =" + idCliente + " ";
            }                        
            ResultSet res = stat.executeQuery(sql);            
            while (res.next()) {                
                idCLientePaquete = res.getLong("id");                                           
            }
            stat.close();
        } catch (SQLException e) {
            this.conexion.rollback();
            throw e;
        } finally {
            this.conexion.commit();
        }
        return idCLientePaquete;
    }
    
    
    public Long getLastIdCafetaria() throws SQLException {        
        Long idCLientePaquete = (long) 0;
        try {
            Statement stat;
            stat = this.conexion.getConexion().createStatement();
            String sql = "SELECT "                    
                    + "Max(cp.id) id "
                    + " FROM producto_ventas cp "              
                    + " WHERE 1 = 1 ";                         
            ResultSet res = stat.executeQuery(sql);            
            while (res.next()) {                
                idCLientePaquete = res.getLong("id");                                           
            }
            stat.close();
        } catch (SQLException e) {
            this.conexion.rollback();
            throw e;
        } finally {
            this.conexion.commit();
        }
        return idCLientePaquete;
    }
    /**
     * 
     * @param idPaquete
     * @return 
     * @throws SQLException 
     */
    public boolean deleteClientePaquete(String idPaquete) throws SQLException {        
        boolean correct;
        try {
            Statement stat = this.conexion.getConexion().createStatement();
            String sql = "DELETE FROM cliente_paquete WHERE 1";
            if (!Util.getVacio(idPaquete)) {
                sql += " AND id=" + idPaquete;
            }        
            stat.execute(sql);
            stat.close();  
        } catch (SQLException ex) {
            correct = false;
            this.conexion.rollback();
            throw ex;
        } finally {
            correct = true;
            this.conexion.commit();
        }        
        return correct;
    }
    
    
    /**
     * 
     * @param idPaquete
     * @return 
     * @throws SQLException 
     */
    public boolean deleteGasto(String idPaquete) throws SQLException {        
        boolean correct;
        try {
            Statement stat = this.conexion.getConexion().createStatement();
            String sql = "DELETE FROM gastos WHERE 1";
            if (!Util.getVacio(idPaquete)) {
                sql += " AND id=" + idPaquete;
            }        
            stat.execute(sql);
            stat.close();  
        } catch (SQLException ex) {
            correct = false;
            this.conexion.rollback();
            throw ex;
        } finally {
            correct = true;
            this.conexion.commit();
        }        
        return correct;
    }

    /**
     * 
     * @param idPaquete
     * @return
     * @throws SQLException 
     */
    public boolean deleteVenta(String idPaquete) throws SQLException {        
        boolean correct;
        try {
            Statement stat = this.conexion.getConexion().createStatement();
            String sql = "DELETE FROM producto_ventas WHERE 1";
            if (!Util.getVacio(idPaquete)) {
                sql += " AND id=" + idPaquete;
            }        
            stat.execute(sql);
            stat.close();  
        } catch (SQLException ex) {
            correct = false;
            this.conexion.rollback();
            throw ex;
        } finally {
            correct = true;
            this.conexion.commit();
        }        
        return correct;
    }

    public List<Object> getListPersist() {
        return listPersist;
    }

    public void setListPersist(List<Object> listPersist) {
        this.listPersist = listPersist;
    }

    public Conexion getConexion() {
        return conexion;
    }

    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }

}
