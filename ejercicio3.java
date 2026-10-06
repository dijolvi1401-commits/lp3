package pro;

import java.util.Random;

// ==========================================
// 1. MODELO (Model)
// ==========================================

class Jugador {
    private String nombre;
    private int salud;
    private int saludMaxima;
    private int nivel;
    private int ataqueBase;

    public Jugador(String nombre, int salud, int nivel, int ataqueBase) {
        this.nombre = nombre;
        this.salud = salud;
        this.saludMaxima = salud;
        this.nivel = nivel;
        this.ataqueBase = ataqueBase;
    }

    public String getNombre() { return nombre; }
    public int getSalud() { return salud; }
    public int getSaludMaxima() { return saludMaxima; }
    public int getNivel() { return nivel; }

    public int atacar() {
        // Cálculo de daño con una pequeña variación aleatoria
        int variacion = (int) (Math.random() * 5);
        return ataqueBase + variacion;
    }

    public void recibirDano(int cantidad) {
        this.salud -= cantidad;
        if (this.salud < 0) this.salud = 0;
    }

    public boolean estaVivo() {
        return this.salud > 0;
    }
}

class Enemigo {
    private String nombre;
    private int salud;
    private int saludMaxima;
    private int nivel;
    private String tipo;
    private int ataqueBase;

    public Enemigo(String nombre, int salud, int nivel, String tipo, int ataqueBase) {
        this.nombre = nombre;
        this.salud = salud;
        this.saludMaxima = salud;
        this.nivel = nivel;
        this.tipo = tipo;
        this.ataqueBase = ataqueBase;
    }

    public String getNombre() { return nombre; }
    public int getSalud() { return salud; }
    public int getSaludMaxima() { return saludMaxima; }
    public int getNivel() { return nivel; }
    public String getTipo() { return tipo; }

    public int atacar() {
        int variacion = (int) (Math.random() * 6);
        return ataqueBase + variacion;
    }

    public void recibirDano(int cantidad) {
        this.salud -= cantidad;
        if (this.salud < 0) this.salud = 0;
    }

    public boolean estaVivo() {
        return this.salud > 0;
    }
}

// ==========================================
// 2. VISTA (View)
// ==========================================

class CombateView {
    public void mostrarMensaje(String mensaje) {
        System.out.println("> " + mensaje);
    }

    public void mostrarEstadoCombate(Jugador jugador, Enemigo enemigo, int turno) {
        System.out.println("\n================ TURNO " + turno + " ================");
        System.out.println("Jugador: " + jugador.getNombre() + " [HP: " + jugador.getSalud() + "/" + jugador.getSaludMaxima() + "]");
        System.out.println("Enemigo: " + enemigo.getNombre() + " (" + enemigo.getTipo() + ") [HP: " + enemigo.getSalud() + "/" + enemigo.getSaludMaxima() + "]");
        System.out.println("----------------------------------------");
    }

    public void mostrarResultado(String ganador) {
        System.out.println("\n========================================");
        System.out.println("¡FIN DEL COMBATE!");
        System.out.println("Ganador: " + ganador);
        System.out.println("========================================\n");
    }
}

// ==========================================
// 3. CONTROLADOR Y CLASE PRINCIPAL
// ==========================================

class CombateController {
    private Jugador jugador;
    private Enemigo enemigo;
    private CombateView vista;

    public CombateController(Jugador jugador, Enemigo enemigo, CombateView vista) {
        this.jugador = jugador;
        this.enemigo = enemigo;
        this.vista = vista;
    }

    public void ejecutarCombate() {
        vista.mostrarMensaje("¡Iniciando batalla entre " + jugador.getNombre() + " y " + enemigo.getNombre() + "!");
        int turno = 1;

        while (jugador.estaVivo() && enemigo.estaVivo()) {
            vista.mostrarEstadoCombate(jugador, enemigo, turno);

            // Turno del Jugador
            int danoJugador = jugador.atacar();
            enemigo.recibirDano(danoJugador);
            vista.mostrarMensaje(jugador.getNombre() + " ataca a " + enemigo.getNombre() + " causando " + danoJugador + " de daño.");

            // Verificar si el enemigo fue derrotado
            if (!enemigo.estaVivo()) {
                vista.mostrarMensaje("¡" + enemigo.getNombre() + " ha sido derrotado!");
                break;
            }

            // Turno del Enemigo (Ataque aleatorio / Acción)
            Random rand = new Random();
            boolean ataca = rand.nextBoolean();

            if (ataca) {
                int danoEnemigo = enemigo.atacar();
                jugador.recibirDano(danoEnemigo);
                vista.mostrarMensaje(enemigo.getNombre() + " contraataca causando " + danoEnemigo + " de daño.");
            } else {
                vista.mostrarMensaje(enemigo.getNombre() + " dudó y no realizó ningún ataque en este turno.");
            }

            turno++;
        }

        // Determinar ganador
        if (jugador.estaVivo()) {
            vista.mostrarResultado(jugador.getNombre());
        } else {
            vista.mostrarResultado(enemigo.getNombre());
        }
    }
}

public class ejercicio3 {
    public static void main(String[] args) {
        // 1. Creación de Entidades (Jugador y Enemigo)
        Jugador jugador = new Jugador("Guerrero Arturo", 100, 1, 18);
        Enemigo enemigo = new Enemigo("Goblin Feroz", 70, 1, "Bestia", 12);

        // 2. Creación de la Vista
        CombateView vista = new CombateView();

        // 3. Creación del Controlador e inicio del combate
        CombateController controlador = new CombateController(jugador, enemigo, vista);
        controlador.ejecutarCombate();
    }
}