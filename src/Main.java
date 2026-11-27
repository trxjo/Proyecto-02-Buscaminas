import javax.swing.SwingUtilities;

import vista.MainWindow;

/**
 * Clase principal del juego Buscaminas. Inicia la interfaz gráfica
 * creando la ventana principal en el hilo de eventos de Swing.
 *
 * @author Ozkar Mauricio Trejo Garcia
 */
public class Main 
{
    /**
     * Punto de entrada de la aplicación.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
