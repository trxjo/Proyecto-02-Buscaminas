package modelo;

/**
 * Estados posibles de una partida del buscaminas.
 */
public enum EstadoPartida 
{
    /** La partida fue creada y todavía no se ha realizado ninguna acción. */
    PREPARADA,
    /** La partida comenzó y continúa en desarrollo. */
    EN_CURSO,
    /** Todas las casillas seguras fueron descubiertas. */
    GANADA,
    /** El jugador descubrió una casilla que contiene una mina. */
    PERDIDA
}