package modelo;

import java.util.ArrayList;
import java.util.List;

import strategy.DificultadStrategy;

public class Partida {

    private Tablero tablero;
    private EstadoPartida estadoPartida;
    private List<Observador> observadores;

    private long tiempoInicial;
    private long tiempoFinal;

    private GeneradorMinas generadorMinas;

    /**
     * Metodo constructor de la clase.
     *
     * @param dificultad Recibe como parametro la dificultad que el usuario
     *                   haya seleccionado.
     * @throws Lanza una excepcion en caso de que la dificultad ingresada
     *               por el usuario sea null.
     */
    public Partida(DificultadStrategy dificultad) {

        if (dificultad == null) {
            throw new IllegalArgumentException(
                "La dificultad no puede ser null"
            );
        }

        tablero = new Tablero(
            dificultad.getFilas(),
            dificultad.getColumnas(),
            dificultad.getConteoMinas()
        );

        generadorMinas = new GeneradorMinas();
        generadorMinas.generar(tablero);

        estadoPartida = EstadoPartida.PREPARADA;
        observadores = new ArrayList<>();

        tiempoInicial = 0;
        tiempoFinal = 0;
    }

    /**
     * Metodo que permite agregar y actualizar los estados de la partida
     * del usuario mediante el patron Observer.
     *
     * @param observador Recibe como parametro un objeto observador.
     */
    public void agregarObservador(Observador observador) {

        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    /**
     * Metodo que permite eliminar un observador de la lista de observadores
     * de la partida.
     *
     * @param observador Recibe como parametro el observador que se desea
     *                   eliminar.
     */
    public void eliminarObservador(Observador observador) {
        observadores.remove(observador);
    }

    /**
     * Metodo que permite recorrer la lista de observadores y notificarles
     * cuando ocurre un cambio en la partida.
     */
    private void notificarObservadores() {

        for (Observador observador : new ArrayList<>(observadores)) {
            observador.actualizar(this);
        }
    }

    /**
     * Metodo que permite al usuario destapar una casilla y verificar
     * si contiene una mina.
     *
     * @param fila Recibe como parametro la fila de la casilla que el
     *             usuario quiera destapar.
     * @param columna Recibe como parametro la columna de la casilla que
     *                el usuario quiera destapar.
     */
    public void destaparCasilla(int fila, int columna) {

        // No se permiten acciones cuando la partida ya termino.
        if (estaTerminada()) {
            return;
        }

        validarPosicion(fila, columna);

        Casilla casilla = tablero.obtenerCasilla(fila, columna);

        // No se puede destapar una casilla que ya esta descubierta
        // o que tiene una bandera.
        if (casilla.estaDescubierta() || casilla.tieneBandera()) {
            return;
        }

        // El cronometro comienza con la primera casilla destapada.
        iniciarPartida();

        // Si la casilla seleccionada contiene una mina, el jugador pierde.
        if (casilla.tieneMina()) {

            casilla.descubrir();
            revelarMinas();
            terminarPartida(EstadoPartida.PERDIDA);

            return;
        }

        // Si la casilla esta vacia, se expanden las casillas vecinas.
        expandirCasillas(fila, columna);

        // Se comprueba si el usuario ya descubrio todas las casillas seguras.
        comprobarGanador();

        // Se notifica a la vista si la partida continua.
        if (!estaTerminada()) {
            notificarObservadores();
        }
    }

    /**
     * Metodo que permite al usuario colocar o quitar una bandera
     * de una casilla seleccionada.
     *
     * @param fila Recibe como parametro la fila de la casilla.
     * @param columna Recibe como parametro la columna de la casilla.
     */
    public void alternarBandera(int fila, int columna) {

        if (estaTerminada()) {
            return;
        }

        validarPosicion(fila, columna);

        Casilla casilla = tablero.obtenerCasilla(fila, columna);

        // No se pueden colocar banderas en casillas descubiertas.
        if (casilla.estaDescubierta()) {
            return;
        }

        casilla.alternarBandera();

        notificarObservadores();
    }

    /**
     * Metodo que permite destapar de forma automatica las casillas vecinas
     * de una casilla seleccionada por el usuario cuando esta no tiene
     * minas a su alrededor.
     *
     * @param fila Recibe como parametro la fila de la casilla seleccionada.
     * @param columna Recibe como parametro la columna de la casilla seleccionada.
     */
    private void expandirCasillas(int fila, int columna) {

        // Se verifica que la posicion se encuentre dentro del tablero.
        if (!tablero.estaDentro(fila, columna)) {
            return;
        }

        Casilla casilla = tablero.obtenerCasilla(fila, columna);

        // Se detiene la expansion si la casilla ya esta descubierta,
        // tiene una bandera o contiene una mina.
        if (casilla.estaDescubierta()
                || casilla.tieneBandera()
                || casilla.tieneMina()) {
            return;
        }

        casilla.descubrir();

        // Si la casilla tiene minas adyacentes, no se continua expandiendo
        // desde ella. Solo se descubre su contenido numerico.
        if (casilla.getMinasVecinas() > 0) {
            return;
        }

        // Se recorren las ocho casillas vecinas.
        for (int i = -1; i <= 1; i++) {

            for (int j = -1; j <= 1; j++) {

                // Se evita volver a procesar la casilla actual.
                if (i == 0 && j == 0) {
                    continue;
                }

                expandirCasillas(fila + i, columna + j);
            }
        }
    }

    /**
     * Metodo que permite verificar si el usuario ya gano la partida.
     * El jugador gana cuando todas las casillas que no contienen minas
     * han sido descubiertas.
     */
    private void comprobarGanador() {

        for (int i = 0; i < tablero.getFilas(); i++) {

            for (int j = 0; j < tablero.getColumnas(); j++) {

                Casilla casilla = tablero.obtenerCasilla(i, j);

                if (!casilla.tieneMina() && !casilla.estaDescubierta()) {
                    return;
                }
            }
        }

        terminarPartida(EstadoPartida.GANADA);
    }

    /**
     * Metodo que permite revelar las minas en caso de que el usuario
     * haya seleccionado una casilla que contiene una mina.
     */
    private void revelarMinas() {

        for (int i = 0; i < tablero.getFilas(); i++) {

            for (int j = 0; j < tablero.getColumnas(); j++) {

                Casilla casilla = tablero.obtenerCasilla(i, j);

                if (casilla.tieneMina()) {
                    casilla.descubrir();
                }
            }
        }
    }

    /**
     * Metodo que permite actualizar el estado final de la partida.
     *
     * @param resultadoPartida Recibe como parametro el resultado de la
     *                         partida dependiendo de las acciones del usuario.
     */
    private void terminarPartida(EstadoPartida resultadoPartida) {

        if (estaTerminada()) {
            return;
        }

        estadoPartida = resultadoPartida;
        tiempoFinal = System.currentTimeMillis();

        notificarObservadores();
    }

    /**
     * Metodo que permite inicializar el tiempo de la partida del usuario.
     * Solamente se ejecuta cuando la partida se encuentra preparada.
     */
    private void iniciarPartida() {

        if (estadoPartida == EstadoPartida.PREPARADA) {

            estadoPartida = EstadoPartida.EN_CURSO;
            tiempoInicial = System.currentTimeMillis();

            notificarObservadores();
        }
    }

    /**
     * Metodo que permite obtener el tiempo transcurrido de una partida.
     *
     * @return Regresa el tiempo en segundos que ha transcurrido desde
     *         que comenzo la partida. Si la partida termino, devuelve
     *         el tiempo final registrado.
     */
    public long getTiempoJuego() {

        if (estadoPartida == EstadoPartida.PREPARADA) {
            return 0;
        }

        long tiempoActual;

        if (estadoPartida == EstadoPartida.EN_CURSO) {
            tiempoActual = System.currentTimeMillis();
        } else {
            tiempoActual = tiempoFinal;
        }

        return (tiempoActual - tiempoInicial) / 1000;
    }

    /**
     * Metodo que permite verificar que una casilla seleccionada por
     * el usuario se encuentre dentro del tablero.
     *
     * @param fila Recibe como parametro la fila que el usuario quiere seleccionar.
     * @param columna Recibe como parametro la columna que el usuario quiere seleccionar.
     * @throws Lanza una excepcion en caso de que la posicion este fuera
     *               del rango del tablero.
     */
    private void validarPosicion(int fila, int columna) {

        if (!tablero.estaDentro(fila, columna)) {
            throw new IllegalArgumentException(
                "Posicion invalida. Esta fuera del tablero"
            );
        }
    }

    /**
     * Metodo que permite obtener el tablero del juego Buscaminas.
     *
     * @return Regresa el tablero de la partida.
     */
    public Tablero getTablero() {
        return tablero;
    }

    /**
     * Metodo que permite obtener el estado actual del juego Buscaminas.
     *
     * @return Regresa el estado de la partida dependiendo de las acciones
     *         que realice el usuario.
     */
    public EstadoPartida getEstadoPartida() {
        return estadoPartida;
    }

    /**
     * Metodo que permite verificar si una partida ya esta ganada o perdida.
     *
     * @return Regresa true en caso de que la partida haya terminado.
     */
    public boolean estaTerminada() {

        return estadoPartida == EstadoPartida.GANADA
            || estadoPartida == EstadoPartida.PERDIDA;
    }
}