package strategy;

/**
 * Clase que implementa la estrategia de dificultad "Intermedio" para el juego de buscaminas.
 * Define un tablero de 16 filas, 16 columnas y 40 minas.
 */
public class IntermedioStrategy implements DificultadStrategy 
{
    /**
     * Obtiene la cantidad de filas del tablero para la dificultad "Intermedio".
     *
     * @return número de filas
     */
    public int getFilas() 
    { 
        return 16; 
    }

    /**
     * Obtiene la cantidad de columnas del tablero para la dificultad "Intermedio".
     *
     * @return número de columnas
     */
    public int getColumnas() 
    { 
        return 16; 
    }

    /**
     * Obtiene la cantidad de minas del tablero para la dificultad "Intermedio".
     *
     * @return número de minas
     */
    public int getConteoMinas() 
    { 
        return 40; 
    }

    /**
     * Obtiene el nombre de la dificultad.
     *
     * @return nombre de la dificultad
     */
    public String getNombre() 
    { 
        return "Medio"; 
    }

    /**
     * Devuelve una representación en cadena de la dificultad.
     *
     * @return nombre de la dificultad
     */
    @Override public String toString() 
    { 
        return getNombre(); 
    }
}
