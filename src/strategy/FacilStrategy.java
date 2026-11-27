package strategy;

/**
 * Clase que implementa la estrategia de dificultad "Fácil" para el juego de buscaminas.
 * Define un tablero de 9 filas, 9 columnas y 10 minas.
 */
public class FacilStrategy implements DificultadStrategy 
{
    /**
     * Obtiene la cantidad de filas del tablero para la dificultad "Fácil".
     *
     * @return número de filas
     */
    public int getFilas() 
    { 
        return 9; 
    }

    /**
     * Obtiene la cantidad de columnas del tablero para la dificultad "Fácil".
     *
     * @return número de columnas
     */
    public int getColumnas() 
    { 
        return 9; 
    }

    /**
     * Obtiene la cantidad de minas del tablero para la dificultad "Fácil".
     *
     * @return número de minas
     */
    public int getConteoMinas() 
    { 
        return 10; 
    }

    /**
     * Obtiene el nombre de la dificultad.
     *
     * @return nombre de la dificultad
     */
    public String getNombre() 
    { 
        return "Fácil"; 
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