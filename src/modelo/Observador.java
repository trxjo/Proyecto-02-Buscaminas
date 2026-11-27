package modelo;

/**
 * Clase que seria una iterfaz del patrón Observador.
 * Define el método que se ejecutará cuando
 * ocurra un cambio en la partida.
 */
public interface Observador 
{
    void actualizar(Partida partida);
}