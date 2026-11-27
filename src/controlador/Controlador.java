package controlador;

import modelo.Partida;

public class Controlador 
{
    private Partida partida;

    /**
     * Metodo constructor de la clase.
     *
     * @param partida Recibe como parametro la partida que sera controlada.
     */
    public Controlador(Partida partida) 
    {
        if (partida == null) 
        {
            throw new IllegalArgumentException(
                "La partida no puede ser null"
            );
        }

        this.partida = partida;
    }

    /**
     * Metodo que permite al usuario destapar una casilla del tablero.
     *
     * @param fila Fila de la casilla seleccionada.
     * @param columna Columna de la casilla seleccionada.
     */
    public void destaparCasilla(int fila, int columna) 
    {
        partida.destaparCasilla(fila, columna);
    }

    /**
     * Metodo que permite al usuario colocar o quitar una bandera.
     *
     * @param fila Fila de la casilla seleccionada.
     * @param columna Columna de la casilla seleccionada.
     */
    public void alternarBandera(int fila, int columna) 
    {
        partida.alternarBandera(fila, columna);
    }

    /**
     * Metodo que permite obtener la partida que controla este objeto.
     *
     * @return Regresa la partida actual.
     */
    public Partida getPartida() 
    {
        return partida;
    }

    /**
     * Metodo que permite consultar si la partida ya termino.
     *
     * @return Regresa true si el usuario gano o perdio.
     */
    public boolean estaTerminada() 
    {
        return partida.estaTerminada();
    }
}