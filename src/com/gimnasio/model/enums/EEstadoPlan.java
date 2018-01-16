package com.gimnasio.model.enums;

/**
 * @author Eminson Mendoza Martinez
 */
public enum EEstadoPlan {

    ACTIVO {
                @Override
                public String getNombre() {
                    return "SI";
                }

                @Override
                public short getId() {
                    return 1;
                }
            },
    VENCIDO {
                @Override
                public String getNombre() {
                    return "VENCIDO";
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
    public static EEstadoPlan getResult(short val) {
        for (EEstadoPlan obj : EEstadoPlan.getValues()) {
            if (obj.getId() == val) {
                return obj;
            }
        }
        return null;
    }

    /**
     * @return
     */
    public static EEstadoPlan[] getValues() {
        return EEstadoPlan.values();
    }

    public abstract String getNombre();

    public abstract short getId();
}
