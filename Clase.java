package propue;

import java.util.ArrayList;
import java.util.List;

// ==========================================
// 1. MODELO (Model)
// ==========================================

class Producto {
    private String id;
    private String nombre;
    private double precio;

    public Producto(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
}

class ElementoCarrito {
    private Producto producto;
    private int cantidad;

    public ElementoCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getSubtotal() { return producto.getPrecio() * cantidad; }
}

class CarritoModel {
    private List<Producto> catalogo = new ArrayList<>();
    private List<ElementoCarrito> carrito = new ArrayList<>();
    private List<String> historialCompras = new ArrayList<>();

    public void agregarProductoCatalogo(Producto p) {
        catalogo.add(p);
    }

    public List<Producto> getCatalogo() {
        return catalogo;
    }

    public void agregarAlCarrito(String idProducto, int cantidad) {
        for (ElementoCarrito item : carrito) {
            if (item.getProducto().getId().equalsIgnoreCase(idProducto)) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        for (Producto p : catalogo) {
            if (p.getId().equalsIgnoreCase(idProducto)) {
                carrito.add(new ElementoCarrito(p, cantidad));
                return;
            }
        }
    }

    public boolean eliminarDelCarrito(String idProducto) {
        return carrito.removeIf(item -> item.getProducto().getId().equalsIgnoreCase(idProducto));
    }

    public List<ElementoCarrito> getCarrito() {
        return carrito;
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (ElementoCarrito item : carrito) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }

    public double calcularTotal(double porcentajeDescuento, double costoEnvio) {
        double subtotal = calcularSubtotal();
        double descuento = subtotal * (porcentajeDescuento / 100.0);
        return (subtotal - descuento) + costoEnvio;
    }

    public void realizarCompra(double descuento, double costoEnvio) {
        if (carrito.isEmpty()) return;
        double total = calcularTotal(descuento, costoEnvio);
        historialCompras.add("Compra realizada. Ítems: " + carrito.size() + " | Total pagado: S/ " + total);
        carrito.clear();
    }

    public List<String> getHistorialCompras() {
        return historialCompras;
    }
}

// ==========================================
// 2. VISTA (View)
// ==========================================

class CarritoView {
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarCatalogo(List<Producto> productos) {
        System.out.println("\n--- CATÁLOGO DE PRODUCTOS ---");
        for (Producto p : productos) {
            System.out.println("ID: " + p.getId() + " | " + p.getNombre() + " - S/ " + p.getPrecio());
        }
    }

    public void mostrarCarrito(List<ElementoCarrito> carrito, double subtotal) {
        System.out.println("\n--- CARRITO DE COMPRAS ---");
        if (carrito.isEmpty()) {
            System.out.println("El carrito está vacío.");
            return;
        }
        for (ElementoCarrito item : carrito) {
            System.out.println(item.getProducto().getNombre() + " x" + item.getCantidad() + " = S/ " + item.getSubtotal());
        }
        System.out.println("Subtotal: S/ " + subtotal);
    }

    public void mostrarHistorial(List<String> historial) {
        System.out.println("\n--- HISTORIAL DE COMPRAS ---");
        if (historial.isEmpty()) {
            System.out.println("No hay compras en el historial.");
        } else {
            for (String h : historial) {
                System.out.println("- " + h);
            }
        }
    }
}

// ==========================================
// 3. CONTROLADOR Y MAIN (Controller & Execution)
// ==========================================

public class Clase {
    private CarritoModel modelo;
    private CarritoView vista;

    public Clase(CarritoModel modelo, CarritoView vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void registrarProducto(String id, String nombre, double precio) {
        modelo.agregarProductoCatalogo(new Producto(id, nombre, precio));
    }

    public void verCatalogo() {
        vista.mostrarCatalogo(modelo.getCatalogo());
    }

    public void agregarProductoAlCarrito(String id, int cantidad) {
        modelo.agregarAlCarrito(id, cantidad);
        vista.mostrarMensaje("Producto agregado al carrito.");
    }

    public void eliminarProductoDelCarrito(String id) {
        if (modelo.eliminarDelCarrito(id)) {
            vista.mostrarMensaje("Producto eliminado del carrito.");
        } else {
            vista.mostrarMensaje("El producto no se encuentra en el carrito.");
        }
    }

    public void verCarrito() {
        vista.mostrarCarrito(modelo.getCarrito(), modelo.calcularSubtotal());
    }

    public void procesarCompra(double porcentajeDescuento, double costoEnvio) {
        if (modelo.getCarrito().isEmpty()) {
            vista.mostrarMensaje("No se puede realizar la compra: El carrito está vacío.");
            return;
        }
        double total = modelo.calcularTotal(porcentajeDescuento, costoEnvio);
        modelo.realizarCompra(porcentajeDescuento, costoEnvio);
        vista.mostrarMensaje("¡Compra procesada con éxito! Total pagado: S/ " + total);
    }

    public void verHistorial() {
        vista.mostrarHistorial(modelo.getHistorialCompras());
    }

    public static void main(String[] args) {
        CarritoModel modelo = new CarritoModel();
        CarritoView vista = new CarritoView();
        Clase controller = new Clase(modelo, vista);

        // 1. Agregar productos al catálogo
        controller.registrarProducto("P1", "Laptop Gamer", 3500.0);
        controller.registrarProducto("P2", "Mouse Inalámbrico", 80.0);

        // 2. Ver catálogo
        controller.verCatalogo();

        // 3. Agregar productos al carrito
        controller.agregarProductoAlCarrito("P1", 1);
        controller.agregarProductoAlCarrito("P2", 2);

        // 4. Ver carrito
        controller.verCarrito();

        // 5. Procesar compra (10% descuento y S/ 15 costo de envío)
        controller.procesarCompra(10, 15.0);

        // 6. Ver el historial de compras
        controller.verHistorial();
    }
}