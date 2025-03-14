package com.Proyect.Vircade.controller;

import com.Proyect.Vircade.modelo.*;
import com.Proyect.Vircade.service.CombustibleService;
import com.Proyect.Vircade.service.ConcesionarioService;
import com.Proyect.Vircade.service.Tipo_vehiculoService;
import com.Proyect.Vircade.service.VehiculoService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    public String guardarVehiculos(Vehiculo vehiculo,
                                   @NotNull Model modelo,
                                   @RequestParam("file") @NotNull MultipartFile imagen) {

        List<Tipo_Vehiculo> liistatipovehi = tipoVehiculoService.listarTodosLosTiposVehiculos();
        List<Concesionario> licon = concesionario.ListarConce();
        List<Combustible> licom = combustible.ListarCom();
        modelo.addAttribute("lisTip", liistatipovehi);
        modelo.addAttribute("lisConce", licon);
        modelo.addAttribute("liscombu",licom);
        modelo.addAttribute("Vehiculo",vehiculo);
        modelo.addAttribute("i", "Vehiculos");

        if (!imagen.isEmpty()) {
            try {
                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "dqa4rvuqd",
                        "api_key", "842462633174474",
                        "api_secret", "I1x_VsMmWRBlW53n6PyHJY764FU"
                ));

                Map uploadResult = cloudinary.uploader().upload(imagen.getBytes(), ObjectUtils.emptyMap());
                String urlImagen = (String) uploadResult.get("secure_url"); // Obtén la URL de la imagen

                vehiculo.setImagen(urlImagen); // Guarda la URL de la imagen en tu entidad Vehiculo
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image to Cloudinary", e);
            }
        }

        vehiculoService.guardarVe(vehiculo);
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
        try {
            vehiculoService.eleminarVe(id);
            System.out.println("Vehiculo Eliminada con exito!");
        } catch (Exception e) {
            // Manejo de errores, por ejemplo, mostrar un mensaje de error
            System.out.println("Error al eliminar el vehículo: " + e.getMessage());
        }
        return "redirect:/Vehiculos";
    }
}