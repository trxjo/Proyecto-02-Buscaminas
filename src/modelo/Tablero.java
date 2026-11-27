package modelo;

/**
 * Representa el tablero del buscaminas y administra sus casillas.
 */
public class Tablero 
{
    private Casilla[][] casillas;

    private int filas;
    private int columnas;
    private int totalMinas;

    /**
     * Crea un tablero vacío con las dimensiones y cantidad de minas indicadas.
     *
     * @param filas cantidad de filas del tablero
     * @param columnas cantidad de columnas del tablero
     * @param totalMinas cantidad de minas que tendrá el tablero
     * @throws IllegalArgumentException si las dimensiones no son positivas o
     * las minas no caben dentro del tablero
     */
    public Tablero(int filas, int columnas, int totalMinas) 
    {
        if (filas <= 0 || columnas <= 0 || totalMinas < 0 || totalMinas >= filas * columnas)
        {
            throw new IllegalArgumentException("Configuración de tablero inválida.");
        }

        this.filas = filas;
        this.columnas = columnas;
        this.totalMinas = totalMinas;

        casillas = new Casilla[filas][columnas];

        inicializar();
    }

    private void inicializar() 
    {
        for (int i = 0; i < filas; i++) 
        {
            for (int j = 0; j < columnas; j++)
            {
                casillas[i][j] = new Casilla();
            }
        }
    }

    /**
     * Obtiene la casilla ubicada en la fila y columna indicadas.
     *
     * @param fila posición de fila
     * @param columna posición de columna
     * @return casilla correspondiente
     * @throws IllegalArgumentException si la posición está fuera del tablero
     */
    public Casilla obtenerCasilla(int fila, int columna) 
    {
        if (!estaDentro(fila, columna)) 
        {
            throw new IllegalArgumentException(
                "Posición fuera del tablero."
            );
        }

        return casillas[fila][columna];
    }

    /**
     * Comprueba si una posición pertenece al tablero.
     *
     * @param fila posición de fila
     * @param columna posición de columna
     * @return {@code true} si la posición está dentro del tablero
     */
    public boolean estaDentro(int fila, int columna) 
    {
        return fila >= 0 && fila < filas &&
               columna >= 0 && columna < columnas;
    }

    /**
     * Obtiene la cantidad de filas del tablero.
     *
     * @return número de filas
     */
    public int getFilas() 
    {
        return filas;
    }

    /**
     * Obtiene la cantidad de columnas del tablero.
     *
     * @return número de columnas
     */
    public int getColumnas() 
    {
        return columnas;
    }

    /**
     * Obtiene la cantidad total de minas configuradas.
     *
     * @return número de minas del tablero
     */
    public int getTotalMinas() 
    {
        return totalMinas;
    }
}