package com.gimnasio.model;
// Generated ago 3, 2016 11:34:53 p.m. by Hibernate Tools 4.3.1

/**
 * DescuentoDto generated by hbm2java
 */
public class GastoDto implements java.io.Serializable {

    private Integer id;
    private String descripcion;
    private double  valor;
    private String fechaRegistro;

    public GastoDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
