package vista;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import modelo.EstadoPartida;
import modelo.Observador;
import modelo.Partida;

/**
 * Panel que muestra el tiempo transcurrido de la partida, en segundos.
 * El cronómetro se activa cuando la partida pasa a {@link EstadoPartida#EN_CURSO}
 * y se detiene cuando la partida termina. El valor mostrado se toma de
 * {@link Partida#getTiempoJuego()}, por lo que el modelo es quien lleva
 * realmente la cuenta del tiempo.
 *
 * @author Ozkar Mauricio Trejo Garcia
 */
public class TimerPanel extends JPanel implements Observador 
{
    /** Cada cuántos milisegundos se refresca la etiqueta mientras corre el tiempo. */
    private static final int INTERVALO_MS = 250;

    /** Tiempo máximo que se muestra (tres dígitos). */
    private static final int TIEMPO_MAXIMO = 999;

    private final JLabel etiqueta;
    private final Timer reloj;

    private Partida partida;

    /**
     * Metodo constructor de la clase. Crea el panel sin partida asociada;
     * se debe llamar a {@link #asociar(Partida)} para comenzar a mostrar
     * el tiempo.
     */
    public TimerPanel() 
    {
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        setBackground(Color.BLACK);

        etiqueta = new JLabel(formatear(0));
        etiqueta.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        etiqueta.setForeground(Color.RED);

        add(etiqueta);

        reloj = new Timer(INTERVALO_MS, e -> refrescar());
    }

    /**
     * Metodo constructor de la clase.
     *
     * @param partida Partida cuyo tiempo se mostrará.
     */
    public TimerPanel(Partida partida) 
    {
        this();

        asociar(partida);
    }

    /**
     * Metodo que asocia el panel a una partida nueva. Detiene el reloj,
     * deja de observar la partida anterior (si había una) y pone el
     * contador al valor actual de la nueva partida.
     *
     * @param nuevaPartida Partida cuyo tiempo se mostrará.
     */
    public void asociar(Partida nuevaPartida) 
    {
        reloj.stop();

        if (partida != null) 
        {
            partida.eliminarObservador(this);
        }

        partida = nuevaPartida;

        if (partida != null) 
        {
            partida.agregarObservador(this);
        }

        refrescar();
    }

    /**
     * Metodo del patrón Observador. Inicia el reloj cuando la partida
     * comienza y lo detiene cuando termina.
     *
     * @param p Partida que notificó el cambio.
     */
    @Override
    public void actualizar(Partida p) 
    {
        if (p.getEstadoPartida() == EstadoPartida.EN_CURSO) 
        {
            if (!reloj.isRunning()) 
            {
                reloj.start();
            }
        } 
        else 
        {
            reloj.stop();
        }

        refrescar();
    }

    /**
     * Metodo que detiene el reloj y deja de observar la partida.
     * Debe llamarse al cerrar la ventana.
     */
    public void detener() 
    {
        reloj.stop();

        if (partida != null) 
        {
            partida.eliminarObservador(this);
        }
    }

    /**
     * Actualiza el texto de la etiqueta con el tiempo actual de la partida.
     */
    private void refrescar() 
    {
        long segundos = (partida == null) ? 0 : partida.getTiempoJuego();

        etiqueta.setText(formatear(segundos));
    }

    /**
     * Da formato al tiempo con tres dígitos (por ejemplo, 007).
     *
     * @param segundos Tiempo en segundos.
     * @return Regresa el tiempo como cadena de tres dígitos, con un máximo de 999.
     */
    private String formatear(long segundos) 
    {
        return String.format("%03d", Math.min(segundos, TIEMPO_MAXIMO));
    }
}
