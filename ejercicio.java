package prop;

import java.util.ArrayList;
import java.util.List;



class Item {
    private String nombre;
    private int cantidad;
    private String tipo;
    private String descripcion;

    public Item(String nombre, int cantidad, String tipo, String descripcion) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
}

class InventarioModel {
    private List<Item> items = new ArrayList<>();
    private int capacidadMaxima;

    public InventarioModel(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public boolean agregarItem(Item itemNuevo) {
        // Buscar si ya existe el ítem para acumular cantidad
        for (Item item : items) {
            if (item.getNombre().equalsIgnoreCase(itemNuevo.getNombre())) {
                item.setCantidad(item.getCantidad() + itemNuevo.getCantidad());
                return true;
            }
        }
        // Verificar capacidad máxima antes de añadir un nuevo ítem
        if (items.size() < capacidadMaxima) {
            items.add(itemNuevo);
            return true;
        }
        return false; // Inventario lleno
    }

    public boolean eliminarItem(String nombre, int cantidad) {
        for (Item item : items) {
            if (item.getNombre().equalsIgnoreCase(nombre)) {
                if (item.getCantidad() > cantidad) {
                    item.setCantidad(item.getCantidad() - cantidad);
                    return true;
                } else if (item.getCantidad() == cantidad) {
                    items.remove(item);
                    return true;
                }
            }
        }
        return false;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Item> buscarPorTipo(String tipo) {
        List<Item> filtrados = new ArrayList<>();
        for (Item item : items) {
            if (item.getTipo().equalsIgnoreCase(tipo)) {
                filtrados.add(item);
            }
        }
        return filtrados;
    }

    public int getCapacidadMaxima() { return capacidadMaxima; }
}

// ==========================================
// 2. VISTA (View)
// ==========================================

class InventarioView {
    public void mostrarMensaje(String mensaje) {
        System.out.println("> " + mensaje);
    }

    public void mostrarInventario(List<Item> items, int capacidadMaxima) {
        System.out.println("\n--- INVENTARIO DEL JUGADOR (" + items.size() + "/" + capacidadMaxima + ") ---");
        if (items.isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        for (Item item : items) {
            System.out.println("- [" + item.getTipo() + "] " + item.getNombre() + 
                               " | Cantidad: " + item.getCantidad() + 
                               " | Desc: " + item.getDescripcion());
        }
    }
}

// ==========================================
// 3. CONTROLADOR Y CLASE PRINCIPAL
// ==========================================

public class ejercicio {
    private InventarioModel modelo;
    private InventarioView vista;

    public ejercicio(InventarioModel modelo, InventarioView vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void registrarItem(String nombre, int cantidad, String tipo, String descripcion) {
        Item nuevo = new Item(nombre, cantidad, tipo, descripcion);
        if (modelo.agregarItem(nuevo)) {
            vista.mostrarMensaje("Ítem '" + nombre + "' añadido/actualizado en el inventario.");
        } else {
            vista.mostrarMensaje("¡No se pudo añadir '" + nombre + "'! Inventario lleno.");
        }
    }

    public void removerItem(String nombre, int cantidad) {
        if (modelo.eliminarItem(nombre, cantidad)) {
            vista.mostrarMensaje("Se eliminó " + cantidad + " de '" + nombre + "'.");
        } else {
            vista.mostrarMensaje("No se pudo eliminar '" + nombre + "': ítem no encontrado o cantidad insuficiente.");
        }
    }

    public void verInventario() {
        vista.mostrarInventario(modelo.getItems(), modelo.getCapacidadMaxima());
    }

    public void verPorTipo(String tipo) {
        System.out.println("\nFiltro por tipo: " + tipo);
        vista.mostrarInventario(modelo.buscarPorTipo(tipo), modelo.getCapacidadMaxima());
    }

    public static void main(String[] args) {
        InventarioModel modelo = new InventarioModel(3); // Capacidad para 3 objetos distintos
        InventarioView vista = new InventarioView();
        ejercicio controller = new ejercicio(modelo, vista);

        // 1. Agregar ítems al inventario
        controller.registrarItem("Poción de Salud", 5, "Consumible", "Restaura 50 HP.");
        controller.registrarItem("Espada de Hierro", 1, "Arma", "Otorga +15 de ataque.");
        controller.registrarItem("Escudo de Madera", 1, "Armadura", "Otorga +5 de defensa.");

        // 2. Intentar agregar un ítem superando la capacidad
        controller.registrarItem("Casco de Hierro", 1, "Armadura", "Otorga +3 de defensa.");

        // 3. Ver inventario
        controller.verInventario();

        // 4. Usar / Eliminar ítems
        controller.removerItem("Poción de Salud", 2);
        controller.verInventario();

        // 5. Filtrar por tipo
        controller.verPorTipo("Arma");
    }
}