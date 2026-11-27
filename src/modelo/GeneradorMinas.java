package modelo;

import java.util.Random;

/**
 * Encargado de colocar minas aleatoriamente en un tablero y de calcular
 * el número de minas vecinas de cada casilla.
 */
public class GeneradorMinas 
{
    private Random random = new Random();

    /**
     * Coloca en el tablero la cantidad de minas configurada.
     * Una casilla no puede recibir más de una mina. Al terminar,
     * calcula el número de minas vecinas de todas las casillas.
     *
     * @param tablero tablero donde se distribuirán las minas
     * @throws IllegalArgumentException si el tablero no permite generar las minas
     * configuradas
     */
    public void generar(Tablero tablero) 
    {
        int minasRestantes = tablero.getTotalMinas();

        while (minasRestantes > 0) 
        {
            int fila = random.nextInt(tablero.getFilas());
            int columna = random.nextInt(tablero.getColumnas());

            Casilla casilla = tablero.obtenerCasilla(
                fila, columna
            );

            if (!casilla.tieneMina()) 
            {
                casilla.colocarMina();

                minasRestantes--;
            }
        }

        calcularMinasVecinas(tablero);
    }

    /**
     * Calcula, para cada casilla del tablero, cuántas de sus ocho
     * casillas vecinas contienen una mina y guarda el resultado en la casilla.
     *
     * @param tablero tablero cuyas casillas serán actualizadas
     */
    private void calcularMinasVecinas(Tablero tablero) 
    {
        for (int i = 0; i < tablero.getFilas(); i++) 
        {
            for (int j = 0; j < tablero.getColumnas(); j++) 
            {
                int contador = 0;

                for (int di = -1; di <= 1; di++) 
                {
                    for (int dj = -1; dj <= 1; dj++) 
                    {
                        if (di == 0 && dj == 0) 
                        {
                            continue;
                        }

                        int vf = i + di;
                        int vc = j + dj;

                        if (tablero.estaDentro(vf, vc) 
                            && tablero.obtenerCasilla(vf, vc).tieneMina()) 
                        {
                            contador++;
                        }
                    }
                }

                tablero.obtenerCasilla(i, j).setMinasVecinas(contador);
            }
        }
    }
}
