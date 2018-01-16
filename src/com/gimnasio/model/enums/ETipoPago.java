package com.gimnasio.model.enums;

/**
 * @author Eminson Mendoza Martinez
 */
public enum ETipoPago {

    EFECTIVO {
                @Override
                public String getNombre() {
                    return "Efectivo";
                }

                @Override
                public short getId() {
                    return 1;
                }
            },
    TARJETA {
                @Override
                public String getNombre() {
                    return "Tarjeta";
                }

                @Override
                public short getId() {
                    return 2;
                }
            };

    /**
     * @param val
     * @return
     */
    public static ETipoPago getResult(short val) {
        for (ETipoPago obj : ETipoPago.getValues()) {
            if (obj.getId() == val) {
                return obj;
            }
        }
        return null;
    }

    /**
     * @return
     */
    public static ETipoPago[] getValues() {
        return ETipoPago.values();
    }

    public abstract String getNombre();

    public abstract short getId();
}
