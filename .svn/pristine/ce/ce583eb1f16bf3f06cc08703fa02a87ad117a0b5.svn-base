package com.gimnasio.model.enums;

/**
 * @author Eminson Mendoza Martinez
 */
public enum ETipoPlan {

    DIA {
                @Override
                public String getNombre() {
                    return "Día";
                }

                @Override
                public short getId() {
                    return 1;
                }
            },
    MES {
                @Override
                public String getNombre() {
                    return "Mes";
                }

                @Override
                public short getId() {
                    return 2;
                }
            },
    BIMESTRE {
                @Override
                public String getNombre() {
                    return "Bimestre";
                }

                @Override
                public short getId() {
                    return 3;
                }
            },
    TRIMETESTRE {
                @Override
                public String getNombre() {
                    return "Trimestre";
                }

                @Override
                public short getId() {
                    return 4;
                }
            },
    CUATRIMETESTRE {
                @Override
                public String getNombre() {
                    return "Cuatrimestre";
                }

                @Override
                public short getId() {
                    return 5;
                }
            },
    SEMESTRE {
                @Override
                public String getNombre() {
                    return "Semestre";
                }

                @Override
                public short getId() {
                    return 6;
                }
            },
    ANIO {
                @Override
                public String getNombre() {
                    return "Año";
                }

                @Override
                public short getId() {
                    return 7;
                }
            };

    /**
     * @param val
     * @return
     */
    public static ETipoPlan getResult(short val) {
        for (ETipoPlan obj : ETipoPlan.getValues()) {
            if (obj.getId() == val) {
                return obj;
            }
        }
        return null;
    }

    /**
     * @return
     */
    public static ETipoPlan[] getValues() {
        return ETipoPlan.values();
    }

    public abstract String getNombre();

    public abstract short getId();
}
