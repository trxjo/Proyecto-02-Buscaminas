package vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

import modelo.Casilla;
import modelo.EstadoPartida;

/**
 * Botón gráfico que representa una casilla del tablero del buscaminas.
 * Esta clase solo se encarga de dibujar la casilla (cubierta, con bandera,
 * con número, vacía o con mina); no contiene lógica del juego. La interacción
 * con el usuario es gestionada por {@link BoardPanel}.
 *
 * @author Ozkar Mauricio Trejo Garcia
 */
public class CellButton extends JButton 
{
    /** Tamaño (en píxeles) del lado de cada botón. */
    public static final int TAMANIO = 32;

    /** Colores clásicos del buscaminas para los números del 1 al 8. */
    private static final Color[] COLORES_NUMEROS = 
    {
        Color.BLACK,
        new Color(0, 0, 255),
        new Color(0, 128, 0),
        new Color(255, 0, 0),
        new Color(0, 0, 128),
        new Color(128, 0, 0),
        new Color(0, 128, 128),
        Color.BLACK,
        Color.DARK_GRAY
    };

    private static final Color COLOR_CUBIERTA = new Color(190, 195, 205);
    private static final Color COLOR_DESCUBIERTA = new Color(228, 228, 228);
    private static final Color COLOR_MINA = new Color(235, 90, 90);
    private static final Color COLOR_BANDERA_CORRECTA = new Color(120, 200, 120);

    private static final Font FUENTE_NUMERO = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FUENTE_MINA = new Font("SansSerif", Font.BOLD, 24);

    private final int fila;
    private final int columna;

    /**
     * Metodo constructor de la clase.
     *
     * @param fila Fila del tablero a la que pertenece el botón.
     * @param columna Columna del tablero a la que pertenece el botón.
     */
    public CellButton(int fila, int columna) 
    {
        this.fila = fila;
        this.columna = columna;

        setPreferredSize(new Dimension(TAMANIO, TAMANIO));
        setMargin(new java.awt.Insets(0, 0, 0, 0));
        setFocusPainted(false);
        setFocusable(false);
        setOpaque(true);

        mostrarCubierta();
    }

    /**
     * Metodo que permite obtener la fila de la casilla que representa
     * este botón.
     *
     * @return Regresa la fila del botón dentro del tablero.
     */
    public int getFila() 
    {
        return fila;
    }

    /**
     * Metodo que permite obtener la columna de la casilla que representa
     * este botón.
     *
     * @return Regresa la columna del botón dentro del tablero.
     */
    public int getColumna() 
    {
        return columna;
    }

    /**
     * Metodo que actualiza la apariencia del botón según el estado actual
     * de la casilla del modelo.
     *
     * @param casilla Casilla del modelo cuyo estado se quiere reflejar.
     * @param estado Estado actual de la partida; permite mostrar, por
     *               ejemplo, las banderas incorrectas cuando el usuario pierde.
     */
    public void actualizar(Casilla casilla, EstadoPartida estado) 
    {
        if (casilla.estaDescubierta()) 
        {
            if (casilla.tieneMina()) 
            {
                mostrarMina(casilla.tieneBandera());
            } 
            else 
            {
                mostrarDescubierta(casilla.getMinasVecinas());
            }

            return;
        }

        // Al ganar, las minas que quedaron sin marcar se señalan con bandera.
        if (estado == EstadoPartida.GANADA && casilla.tieneMina()) 
        {
            mostrarBandera();
            return;
        }

        if (casilla.tieneBandera()) 
        {
            if (estado == EstadoPartida.PERDIDA) 
            {
                mostrarBanderaIncorrecta();
            } 
            else 
            {
                mostrarBandera();
            }

            return;
        }

        mostrarCubierta();
    }

    /**
     * Dibuja la casilla cubierta (aún no descubierta y sin bandera).
     */
    private void mostrarCubierta() 
    {
        setText("");
        setBackground(COLOR_CUBIERTA);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    }

    /**
     * Dibuja una casilla cubierta con una bandera.
     */
    private void mostrarBandera() 
    {
        mostrarCubierta();
        setFont(FUENTE_NUMERO);
        setForeground(Color.RED);
        setText("F");
    }

    /**
     * Dibuja una bandera colocada en una casilla que no tenía mina.
     * Solo se utiliza cuando el usuario pierde la partida.
     */
    private void mostrarBanderaIncorrecta() 
    {
        mostrarCubierta();
        setFont(FUENTE_NUMERO);
        setForeground(Color.RED);
        setText("X");
    }

    /**
     * Dibuja una casilla descubierta sin mina.
     *
     * @param minasVecinas Número de minas alrededor; si es 0 la casilla
     *                     se muestra vacía.
     */
    private void mostrarDescubierta(int minasVecinas) 
    {
        setBackground(COLOR_DESCUBIERTA);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setFont(FUENTE_NUMERO);

        if (minasVecinas > 0) 
        {
            setForeground(COLORES_NUMEROS[minasVecinas]);
            setText(String.valueOf(minasVecinas));
        } 
        else 
        {
            setText("");
        }
    }

    /**
     * Dibuja una mina revelada.
     *
     * @param marcadaConBandera true si el usuario la había marcado
     *                          correctamente con una bandera.
     */
    private void mostrarMina(boolean marcadaConBandera) 
    {
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setFont(FUENTE_MINA);
        setForeground(Color.BLACK);

        if (marcadaConBandera) 
        {
            setBackground(COLOR_BANDERA_CORRECTA);
        } 
        else 
        {
            setBackground(COLOR_MINA);
        }

        setText("*");
    }
}
