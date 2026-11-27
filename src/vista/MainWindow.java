package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import controlador.Controlador;
import modelo.Casilla;
import modelo.EstadoPartida;
import modelo.Observador;
import modelo.Partida;
import modelo.Tablero;
import strategy.DificilStrategy;
import strategy.DificultadStrategy;
import strategy.FacilStrategy;
import strategy.IntermedioStrategy;

/**
 * Ventana principal del buscaminas. Reúne el contador de minas, el botón de
 * nueva partida, el cronómetro ({@link TimerPanel}) y el tablero
 * ({@link BoardPanel}). Incluye un menú para elegir la dificultad
 * (patrón Strategy) y, al ser observadora de la {@link Partida}, avisa
 * al usuario cuando gana o pierde.
 *
 * @author Ozkar Mauricio Trejo Garcia
 */
public class MainWindow extends JFrame implements Observador 
{
    private final TimerPanel timerPanel;
    private final JLabel etiquetaMinas;
    private final JLabel etiquetaEstado;

    private BoardPanel boardPanel;
    private Partida partidaActual;
    private Controlador controlador;
    private DificultadStrategy dificultadActual;

    private boolean mensajeFinMostrado;

    /**
     * Metodo constructor de la clase. Construye la ventana e inicia una
     * partida con la dificultad "Fácil".
     */
    public MainWindow() 
    {
        super("Buscaminas - Ozkar Mauricio Trejo Garcia");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        timerPanel = new TimerPanel();
        etiquetaMinas = crearEtiquetaContador();
        etiquetaEstado = new JLabel(" ", SwingConstants.CENTER);

        add(crearCabecera(), BorderLayout.NORTH);
        add(etiquetaEstado, BorderLayout.SOUTH);

        setJMenuBar(crearMenu());

        iniciarPartida(new FacilStrategy());

        setResizable(false);
        setLocationRelativeTo(null);
    }

    /**
     * Crea la etiqueta con aspecto de display que muestra las minas restantes.
     *
     * @return Regresa la etiqueta ya configurada.
     */
    private JLabel crearEtiquetaContador() 
    {
        JLabel etiqueta = new JLabel("000", SwingConstants.CENTER);

        etiqueta.setOpaque(true);
        etiqueta.setBackground(Color.BLACK);
        etiqueta.setForeground(Color.RED);
        etiqueta.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        etiqueta.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        return etiqueta;
    }

    /**
     * Crea la parte superior de la ventana: contador de minas, botón de
     * nueva partida y cronómetro.
     *
     * @return Regresa el panel de cabecera.
     */
    private JPanel crearCabecera() 
    {
        JPanel cabecera = new JPanel(new BorderLayout(10, 0));

        cabecera.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        JButton botonNueva = new JButton("Nueva partida");

        botonNueva.setFocusable(false);
        botonNueva.addActionListener(e -> iniciarPartida(dificultadActual));

        cabecera.add(etiquetaMinas, BorderLayout.WEST);
        cabecera.add(botonNueva, BorderLayout.CENTER);
        cabecera.add(timerPanel, BorderLayout.EAST);

        return cabecera;
    }

    /**
     * Crea el menú "Juego" con las opciones de nueva partida, dificultad y salir.
     *
     * @return Regresa la barra de menú.
     */
    private JMenuBar crearMenu() 
    {
        JMenuBar barra = new JMenuBar();
        JMenu menuJuego = new JMenu("Juego");

        JMenuItem itemNueva = new JMenuItem("Nueva partida");

        itemNueva.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        itemNueva.addActionListener(e -> iniciarPartida(dificultadActual));

        menuJuego.add(itemNueva);
        menuJuego.addSeparator();

        ButtonGroup grupo = new ButtonGroup();

        DificultadStrategy[] dificultades = 
        {
            new FacilStrategy(),
            new IntermedioStrategy(),
            new DificilStrategy()
        };

        for (DificultadStrategy dificultad : dificultades) 
        {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(
                dificultad.getNombre() + " ("
                + dificultad.getFilas() + "x" + dificultad.getColumnas()
                + ", " + dificultad.getConteoMinas() + " minas)"
            );

            item.setSelected(dificultad instanceof FacilStrategy);
            item.addActionListener(e -> iniciarPartida(dificultad));

            grupo.add(item);
            menuJuego.add(item);
        }

        menuJuego.addSeparator();

        JMenuItem itemSalir = new JMenuItem("Salir");

        itemSalir.addActionListener(e -> System.exit(0));

        menuJuego.add(itemSalir);
        barra.add(menuJuego);

        return barra;
    }

    /**
     * Metodo que crea una partida nueva con la dificultad indicada y
     * reconstruye el tablero. Desconecta los componentes de la partida
     * anterior para que no sigan recibiendo notificaciones.
     *
     * @param dificultad Estrategia de dificultad de la nueva partida.
     */
    private void iniciarPartida(DificultadStrategy dificultad) 
    {
        if (boardPanel != null) 
        {
            boardPanel.desconectar();
            remove(boardPanel);
        }

        if (partidaActual != null) 
        {
            partidaActual.eliminarObservador(this);
        }

        dificultadActual = dificultad;
        mensajeFinMostrado = false;

        partidaActual = new Partida(dificultad);
        controlador = new Controlador(partidaActual);

        boardPanel = new BoardPanel(controlador);

        partidaActual.agregarObservador(this);
        timerPanel.asociar(partidaActual);

        add(boardPanel, BorderLayout.CENTER);

        actualizar(partidaActual);

        pack();
        revalidate();
        repaint();
    }

    /**
     * Metodo del patrón Observador. Actualiza el contador de minas y el
     * mensaje de estado, y avisa al usuario cuando la partida termina.
     *
     * @param partida Partida que notificó el cambio.
     */
    @Override
    public void actualizar(Partida partida) 
    {
        int minasRestantes = partida.getTablero().getTotalMinas() 
                             - contarBanderas(partida.getTablero());

        etiquetaMinas.setText(String.format("%03d", Math.max(minasRestantes, 0)));

        EstadoPartida estado = partida.getEstadoPartida();

        switch (estado) 
        {
            case PREPARADA:
                etiquetaEstado.setText(dificultadActual.getNombre()
                    + " - Clic izquierdo: destapar, clic derecho: bandera");
                break;
            case EN_CURSO:
                etiquetaEstado.setText("Partida en curso");
                break;
            case GANADA:
                etiquetaEstado.setText("¡Ganaste!");
                break;
            case PERDIDA:
                etiquetaEstado.setText("Perdiste");
                break;
        }

        if (partida.estaTerminada() && !mensajeFinMostrado) 
        {
            mensajeFinMostrado = true;

            // Se muestra después de que el tablero termine de repintarse.
            SwingUtilities.invokeLater(() -> mostrarFinDePartida(partida));
        }
    }

    /**
     * Cuenta cuántas banderas ha colocado el usuario en el tablero.
     *
     * @param tablero Tablero de la partida.
     * @return Regresa el número de casillas con bandera.
     */
    private int contarBanderas(Tablero tablero) 
    {
        int banderas = 0;

        for (int i = 0; i < tablero.getFilas(); i++) 
        {
            for (int j = 0; j < tablero.getColumnas(); j++) 
            {
                Casilla casilla = tablero.obtenerCasilla(i, j);

                if (casilla.tieneBandera()) 
                {
                    banderas++;
                }
            }
        }

        return banderas;
    }

    /**
     * Muestra un cuadro de diálogo con el resultado y pregunta al usuario
     * si desea jugar otra vez con la misma dificultad.
     *
     * @param partida Partida que acaba de terminar.
     */
    private void mostrarFinDePartida(Partida partida) 
    {
        // Si el usuario ya inició otra partida, el aviso ya no aplica.
        if (partida != partidaActual) 
        {
            return;
        }

        String resultado = (partida.getEstadoPartida() == EstadoPartida.GANADA)
            ? "¡Felicidades, ganaste!"
            : "Pisaste una mina. ¡Perdiste!";

        int opcion = JOptionPane.showConfirmDialog(
            this,
            resultado + "\nTiempo: " + partida.getTiempoJuego() 
                + " s\n¿Quieres jugar otra vez?",
            "Fin de la partida",
            JOptionPane.YES_NO_OPTION,
            partida.getEstadoPartida() == EstadoPartida.GANADA
                ? JOptionPane.INFORMATION_MESSAGE
                : JOptionPane.WARNING_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) 
        {
            iniciarPartida(dificultadActual);
        }
    }
}
