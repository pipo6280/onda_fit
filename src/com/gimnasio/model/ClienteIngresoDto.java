/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gimnasio.model;

/**
 *
 * @author emimaster16
 */
public class ClienteIngresoDto implements java.io.Serializable {

    private Long id;
    private ClienteDto clienteDto;
    private UsuarioDto usuarioDto;
    private ClientePaqueteDto clientePaqueteDto;

    private Long clientePaqueteId;
    private Long clienteId;
    private Long usuarioId;
    private String fechaIngreso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClienteDto getClienteDto() {
        return clienteDto;
    }

    public void setClienteDto(ClienteDto clienteDto) {
        this.clienteDto = clienteDto;
    }

    public UsuarioDto getUsuarioDto() {
        return usuarioDto;
    }

    public void setUsuarioDto(UsuarioDto usuarioDto) {
        this.usuarioDto = usuarioDto;
    }

    public ClientePaqueteDto getClientePaqueteDto() {
        return clientePaqueteDto;
    }

    public void setClientePaqueteDto(ClientePaqueteDto clientePaqueteDto) {
        this.clientePaqueteDto = clientePaqueteDto;
    }

    public Long getClientePaqueteId() {
        return clientePaqueteId;
    }

    public void setClientePaqueteId(Long clientePaqueteId) {
        this.clientePaqueteId = clientePaqueteId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

}
