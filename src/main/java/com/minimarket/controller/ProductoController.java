package com.minimarket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.minimarket.entidades.Categoria;
import com.minimarket.entidades.Producto;
import com.minimarket.entidades.User;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.ProductoRepository;

import java.util.List;

@Controller
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository; // Añadido: repositorio para categorías


    @GetMapping("/login")
    public String login() {
        return "login"; // nombre de la vista de inicio de sesión
    }

    @PostMapping("/login")
    public String loginPost() {
        System.out.println("Login exitoso, redirigiendo a /home"); // Para depurar
        return "redirect:/home"; // Redirigir a la vista de inicio
    }


    @GetMapping("/home")
    public String home(Model model) {
        List<Producto> products = productoRepository.findAll(); // Obtener todos los productos de la base de datos
        model.addAttribute("products", products);
        return "home"; // nombre de la vista de inicio
    }
    
    
    @GetMapping("/registro")
    public String registroForm() {
        return "registro"; // Devuelve la vista llamada "registro" (thymeleaf buscará un archivo registro.html)
    }

    // Manejar solicitud POST para procesar el formulario de registro
    @PostMapping("/registro")
    public String registroPost() {
        System.out.println("registro exitoso, redirigiendo a /login");
        return "redirect:/login"; // Redirigir a la vista de inicio de sesión
    }


    @GetMapping("/agregar-producto")
    public String agregarProducto(Model model) {
        List<Categoria> categories = categoriaRepository.findAll(); // Obtener todas las categorías
        model.addAttribute("categories", categories);
        return "agregar-producto"; // nombre de la vista para agregar producto
    }

    @PostMapping("/agregar-producto")
    public String guardarProducto(@RequestParam String name,
                                  @RequestParam long categoryId,
                                  @RequestParam int quantity,
                                  @RequestParam double price) {
        Categoria category = categoriaRepository.findById(categoryId).orElse(null); // Obtener la categoría por ID
        if (category != null) {
            Producto product = new Producto(name, category, quantity, price); // Crear un nuevo producto
            productoRepository.save(product); // Guardar el producto en la base de datos
        }
        return "redirect:/home"; // Redirigir a la lista de productos
    }

    @GetMapping("/ver-lista-productos")
    public String verListaProductos(@RequestParam(required = false) Long categoriaId, Model model) {
        List<Producto> products;
        
        // Filtrar productos por categoría o mostrar todos si no hay selección
        if (categoriaId != null) {
            products = productoRepository.findByCategoriaId(categoriaId); // Filtrar productos por categoría
        } else {
            products = productoRepository.findAll(); // Mostrar todos los productos si no se ha seleccionado una categoría
        }

        List<Categoria> categories = categoriaRepository.findAll(); // Obtener todas las categorías para el combobox
        model.addAttribute("categorias", categories); // Asegúrate de que el nombre sea "categorias" para coincidir con el HTML
        model.addAttribute("products", products);
        model.addAttribute("selectedCategoryId", categoriaId); // Mantener la categoría seleccionada para el combo box
        return "ver-lista-productos"; // Asegúrate de que el nombre de la vista sea correcto
    }



    @GetMapping("/ver-detalles/{id}")
    public String verDetalles(@PathVariable int id, Model model) {
        Producto product = productoRepository.findById(id).orElse(null); // Obtener el producto por ID
        model.addAttribute("product", product);
        return "ver-detalles"; // nombre de la vista para ver detalles del producto
    }

    @GetMapping("/modificar-producto/{id}")
    public String modificarProducto(@PathVariable int id, Model model) {
        Producto product = productoRepository.findById(id).orElse(null); // Obtener el producto por ID
        List<Categoria> categories = categoriaRepository.findAll(); // Obtener todas las categorías
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "modificar-producto"; // nombre de la vista para modificar producto
    }

    @PostMapping("/modificar-producto/{id}")
    public String actualizarProducto(@PathVariable int id,
                                      @RequestParam String name,
                                      @RequestParam long categoryId,
                                      @RequestParam int quantity,
                                      @RequestParam double price) {
        Producto product = productoRepository.findById(id).orElse(null);
        if (product != null) {
            Categoria category = categoriaRepository.findById(categoryId).orElse(null);
            if (category != null) {
                product.setNombre(name);
                product.setCategoria(category);
                product.setCantidad(quantity);
                product.setPrecioUnitario(price);
                productoRepository.save(product); // Guardar los cambios en el producto
            }
        }
        return "redirect:/ver-lista-productos"; // Redirigir a la lista de productos
    }

    @GetMapping("/eliminar-producto/{id}")
    public String eliminarProducto(@PathVariable int id) {
        productoRepository.deleteById(id); // Eliminar el producto por ID
        return "redirect:/ver-lista-productos"; // Redirigir a la lista de productos
    }
}