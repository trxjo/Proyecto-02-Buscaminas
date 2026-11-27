package vista;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controlador.Controlador;
import modelo.Observador;
import modelo.Partida;
import modelo.Tablero;

/**
 * Panel que dibuja el tablero del buscaminas como una cuadrícula de
 * {@link CellButton}. Funciona como observador de la {@link Partida}: cada
 * vez que el modelo cambia, el panel vuelve a pintar todas sus casillas.
 * Las acciones del usuario se envían al {@link Controlador}:
 * clic izquierdo para destapar y clic derecho para colocar o quitar una bandera.
 *
 * @author Ozkar Mauricio Trejo Garcia
 */
public class BoardPanel extends JPanel implements Observador 
{
    private final Controlador controlador;
    private final CellButton[][] botones;

    /**
     * Metodo constructor de la clase.
     *
     * @param controlador Controlador que recibirá las acciones del usuario.
     *                    La partida que controla es la que se dibujará.
     * @throws IllegalArgumentException si el controlador es null.
     */
    public BoardPanel(Controlador controlador) 
    {
        if (controlador == null) 
        {
            throw new IllegalArgumentException(
                "El controlador no puede ser null"
            );
        }

        this.controlador = controlador;

        Tablero tablero = controlador.getPartida().getTablero();

        setLayout(new GridLayout(tablero.getFilas(), tablero.getColumnas()));

        botones = new CellButton[tablero.getFilas()][tablero.getColumnas()];

        crearBotones(tablero);

        controlador.getPartida().agregarObservador(this);

        refrescar(controlador.getPartida());
    }

    /**
     * Crea un botón por cada casilla del tablero, le asigna el manejador
     * del ratón y lo agrega al panel.
     *
     * @param tablero Tablero del que se toman las dimensiones.
     */
    private void crearBotones(Tablero tablero) 
    {
        MouseAdapter manejador = new MouseAdapter() 
        {
            @Override
            public void mouseReleased(MouseEvent e) 
            {
                manejarClic(e);
            }
        };

        for (int i = 0; i < tablero.getFilas(); i++) 
        {
            for (int j = 0; j < tablero.getColumnas(); j++) 
            {
                CellButton boton = new CellButton(i, j);

                boton.addMouseListener(manejador);

                botones[i][j] = boton;

                add(boton);
            }
        }
    }

    /**
     * Metodo que interpreta el clic del usuario y lo envía al controlador.
     * El clic solo se procesa si el ratón se soltó dentro del botón.
     *
     * @param e Evento del ratón generado por el botón.
     */
    private void manejarClic(MouseEvent e) 
    {
        CellButton boton = (CellButton) e.getComponent();

        if (!boton.contains(e.getPoint())) 
        {
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) 
        {
            controlador.destaparCasilla(boton.getFila(), boton.getColumna());
        } 
        else if (SwingUtilities.isRightMouseButton(e)) 
        {
            controlador.alternarBandera(boton.getFila(), boton.getColumna());
        }
    }

    /**
     * Metodo del patrón Observador. Se ejecuta cuando la partida cambia
     * y vuelve a pintar el tablero completo.
     *
     * @param partida Partida que notificó el cambio.
     */
    @Override
    public void actualizar(Partida partida) 
    {
        refrescar(partida);
    }

    /**
     * Actualiza la apariencia de todos los botones con el estado
     * actual de las casillas de la partida.
     *
     * @param partida Partida de la que se leen las casillas y su estado.
     */
    private void refrescar(Partida partida) 
    {
        Tablero tablero = partida.getTablero();

        for (int i = 0; i < tablero.getFilas(); i++) 
        {
            for (int j = 0; j < tablero.getColumnas(); j++) 
            {
                botones[i][j].actualizar(
                    tablero.obtenerCasilla(i, j),
                    partida.getEstadoPartida()
                );
            }
        }

        repaint();
    }

    /**
     * Metodo que desconecta este panel de la partida. Debe llamarse antes de
     * descartar el panel para que deje de recibir notificaciones.
     */
    public void desconectar() 
    {
        controlador.getPartida().eliminarObservador(this);
    }
}
