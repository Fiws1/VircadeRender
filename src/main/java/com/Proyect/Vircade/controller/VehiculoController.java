package com.Proyect.Vircade.controller;

import com.Proyect.Vircade.modelo.*;
import com.Proyect.Vircade.service.CombustibleService;
import com.Proyect.Vircade.service.ConcesionarioService;
import com.Proyect.Vircade.service.Tipo_vehiculoService;
import com.Proyect.Vircade.service.VehiculoService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Slf4j
@Controller
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private Tipo_vehiculoService tipoVehiculoService;

    @Autowired
    private ConcesionarioService concesionario;

    @Autowired
    private CombustibleService combustible;

    @GetMapping("/Vehiculos")
    public String listarVehiculos(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int pageSize,
                                  @NotNull Model modelo) {
        List<Vehiculo> paginaVehiculos = vehiculoService.listarTodosLosTiposVehiculos();

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        for (Vehiculo vehiculo : paginaVehiculos) {
            vehiculo.setPrecioFormateado(currencyFormat.format(vehiculo.getPrecio()));
        }

        Vehiculo vehiculo = new Vehiculo();
        List<Tipo_Vehiculo> listatipovehi = tipoVehiculoService.listarTodosLosTiposVehiculos();
        List<Concesionario> licon = concesionario.ListarConce();
        List<Combustible> licom = combustible.ListarCom();
        modelo.addAttribute("lisTip", listatipovehi);
        modelo.addAttribute("lisConce", licon);
        modelo.addAttribute("liscombu", licom);
        modelo.addAttribute("Vehiculo", vehiculo);
        modelo.addAttribute("i", "Vehiculos");

        modelo.addAttribute("Vehiculos", paginaVehiculos);
        modelo.addAttribute("totalPages", paginaVehiculos);

        return "view/vehiculo/vehiculo"; // nos retorna al archivo estudiantes
    }

    @PostMapping("/Vehiculosave")
    public String guardarVehiculos(Vehiculo Vehiculo,
                                   @NotNull Model modelo,
                                   @RequestParam("file") @NotNull MultipartFile imagen) {

        List<Tipo_Vehiculo> liistatipovehi = tipoVehiculoService.listarTodosLosTiposVehiculos();
        List<Concesionario> licon = concesionario.ListarConce();
        List<Combustible> licom = combustible.ListarCom();
        modelo.addAttribute("lisTip", liistatipovehi);
        modelo.addAttribute("lisConce", licon);
        modelo.addAttribute("liscombu",licom);
        modelo.addAttribute("Vehiculo",Vehiculo);
        modelo.addAttribute("i", "Vehiculos");

        if (!imagen.isEmpty()) {
            String nombreImagen = imagen.getOriginalFilename();
            Path directorioImagenes = Paths.get("src/main/resources/static/Images");
            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();

            try {
                byte[] bytesImg = imagen.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreImagen);
                Files.write(rutaCompleta, bytesImg);

                Vehiculo.setImagen(nombreImagen);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        vehiculoService.guardarVe(Vehiculo);
        System.out.println("Vehiculo guardado con exito!");
        return "redirect:/Vehiculos";
    }

    @GetMapping("/Vehiculosedit/{id}")
    public String editarVehiculos(@PathVariable("id") int id, @NotNull Model modelo) {
        Vehiculo vehiculo = vehiculoService.findByVehiculo(id);
        List<Tipo_Vehiculo> listatipovehi = tipoVehiculoService.listarTodosLosTiposVehiculos();
        List<Concesionario> licon = concesionario.ListarConce();
        List<Combustible> licom = combustible.ListarCom();

        modelo.addAttribute("lisTip", listatipovehi);
        modelo.addAttribute("lisConce", licon);
        modelo.addAttribute("liscombu", licom);
        modelo.addAttribute("Vehiculo", vehiculo);
        modelo.addAttribute("i", "Vehiculos"); // Mantén "i" para identificar la vista

        return "view/vehiculo/modificar"; // Crea una nueva vista para la edición
    }

    @DeleteMapping("/Vehiculosde/{id}")
    public String eliminarVehiculos(@PathVariable("id") int id) {
        vehiculoService.eleminarVe(id);
        System.out.println("Vehiculo Eliminada con exito!");
        return "redirect:/Vehiculos";
    }
}