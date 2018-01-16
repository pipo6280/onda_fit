package com.gimnasio.model.enums;

/**
 * @author Eminson Mendoza Martinez
 */
public enum ESiNo {

    SI {
                @Override
                public String getNombre() {
                    return "SI";
                }

                @Override
                public short getId() {
                    return 1;
                }
            },
    NO {
                @Override
                public String getNombre() {
                    return "NO";
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
    public static ESiNo getResult(short val) {
        for (ESiNo obj : ESiNo.getValues()) {
            if (obj.getId() == val) {
                return obj;
            }
        }
        return null;
    }

    /**
     * @return
     */
    public static ESiNo[] getValues() {
        return ESiNo.values();
    }

    public abstract String getNombre();

    public abstract short getId();
}
