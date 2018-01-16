package com.gimnasio.model.enums;

/**
 * @author Eminson Mendoza Martinez
 */
public enum EGenero {

    FEMENIMO {
                @Override
                public String getNombre() {
                    return "Femenino";
                }

                @Override
                public short getId() {
                    return 1;
                }
            },
    MASCULINO {
                @Override
                public String getNombre() {
                    return "Masculino";
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
    public static EGenero getResult(short val) {
        for (EGenero obj : EGenero.getValues()) {
            if (obj.getId() == val) {
                return obj;
            }
        }
        return null;
    }

    /**
     * @return
     */
    public static EGenero[] getValues() {
        return EGenero.values();
    }

    public abstract String getNombre();

    public abstract short getId();
}
