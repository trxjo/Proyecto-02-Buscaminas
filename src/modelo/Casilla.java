package modelo;

/**
 * Representa una casilla del tablero del buscaminas.
 * Cada casilla puede contener una mina, estar descubierta o tener una bandera,
 * y conserva el número de minas situadas en sus casillas vecinas.
 */
public class Casilla 
{
    private boolean tieneMina;
    private boolean descubierta;
    private boolean tieneBandera;

    private int minasVecinas;

    /**
     * Indica si la casilla contiene una mina.
     *
     * @return true si contiene una mina; false en caso contrario
     */
    public boolean tieneMina() 
    {
        return tieneMina;
    }

    /**
     * Indica si la casilla ya fue descubierta.
     *
     * @return true si está descubierta; false en caso contrario
     */
    public boolean estaDescubierta() 
    {
        return descubierta;
    }

    /**
     * Indica si la casilla tiene una bandera colocada.
     *
     * @return true si tiene bandera; false en caso contrario
     */
    public boolean tieneBandera() 
    {
        return tieneBandera;
    }

    /**
     * Obtiene la cantidad de minas presentes en las casillas vecinas.
     *
     * @return número de minas vecinas
     */
    public int getMinasVecinas() 
    {
        return minasVecinas;
    }

    /**
     * Coloca una mina en la casilla.
     */
    public void colocarMina() 
    {
        tieneMina = true;
    }

    /**
     * Establece la cantidad de minas vecinas.
     *
     * @param cantidad número de minas vecinas
     */
    public void setMinasVecinas(int cantidad) 
    {
        minasVecinas = cantidad;
    }

    /**
     * Marca la casilla como descubierta.
     */
    public void descubrir() 
    {
        descubierta = true;
    }

    /**
     * Coloca una bandera si no existe o la elimina si ya estaba colocada.
     */
    public void alternarBandera() 
    {
        tieneBandera = !tieneBandera;
    }
}