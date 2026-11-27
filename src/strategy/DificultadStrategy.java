package strategy;

/**
 * Interfaz que define la estrategia de dificultad para el juego de buscaminas.
 */
public interface DificultadStrategy 
{
    int getFilas();
    int getColumnas();
    int getConteoMinas();
    String getNombre();
}
