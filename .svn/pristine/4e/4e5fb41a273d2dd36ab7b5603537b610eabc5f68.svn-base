package com.gimnasio.model.enums;

/**
 * @author Eminson Mendoza Martinez
 */
public enum ETipoDocumento {

    TARJETA_IDENTIDAD {
                @Override
                public String getNombre() {
                    return "Tarjeta identidad";
                }

                @Override
                public short getId() {
                    return 1;
                }
            },
    CEDULA {
                @Override
                public String getNombre() {
                    return "Cédula de ciudadanía";
                }

                @Override
                public short getId() {
                    return 2;
                }
            },
    CEDULA_EXTRANJERIA {
                @Override
                public String getNombre() {
                    return "Cédula de extranjería";
                }

                @Override
                public short getId() {
                    return 3;
                }
            },
    NUIP {
                @Override
                public String getNombre() {
                    return "Nuip";
                }

                @Override
                public short getId() {
                    return 4;
                }
            };

    /**
     * @param val
     * @return
     */
    public static ETipoDocumento getResult(short val) {
        for (ETipoDocumento obj : ETipoDocumento.getValues()) {
            if (obj.getId() == val) {
                return obj;
            }
        }
        return null;
    }

    /**
     * @return
     */
    public static ETipoDocumento[] getValues() {
        return ETipoDocumento.values();
    }

    public abstract String getNombre();

    public abstract short getId();
}
