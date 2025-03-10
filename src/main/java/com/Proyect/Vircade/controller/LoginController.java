package com.Proyect.Vircade.controller;

import com.Proyect.Vircade.modelo.*;
import com.Proyect.Vircade.service.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
public class LoginController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private Tipo_vehiculoService tipoVehiculoService;

    @Autowired
    private ConcesionarioService concesionario;

    @Autowired
    private CombustibleService combustible;

    @GetMapping("/admin/dashboard")
    public String showDashboardAdmin(@NotNull Model model, @NotNull Authentication authentication,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int pageSize) {
        // Obtener la información del usuario
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String correo = userDetails.getUsername();
        Usuario usuario = usuarioService.findByEmail(correo);

        Page<Vehiculo> paginaVehiculos = vehiculoService.listarVehiculos(PageRequest.of(page - 1, pageSize));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        for (Vehiculo vehiculo : paginaVehiculos) {
            vehiculo.setPrecioFormateado(currencyFormat.format(vehiculo.getPrecio()));
        }

        Vehiculo vehiculo = new Vehiculo();
        List<Tipo_Vehiculo> listatipovehi = tipoVehiculoService.listarTodosLosTiposVehiculos();
        List<Concesionario> licon = concesionario.ListarConce();
        List<Combustible> licom = combustible.ListarCom();
        model.addAttribute("lisTip", listatipovehi);
        model.addAttribute("lisConce", licon);
        model.addAttribute("liscombu", licom);
        model.addAttribute("Vehiculo", vehiculo);

        model.addAttribute("Vehiculos", paginaVehiculos.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginaVehiculos.getTotalPages());
        model.addAttribute("usuario", usuario);
        // Obtener la información del vehículo si se proporciona un idVehiculo
        return "admin/dashboard"; // Plantilla específica para administradores
    }

    @GetMapping("/cliente/dashboard")
    public String showDashboardCliente(@NotNull Model model, @NotNull Authentication authentication,
                                       @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String correo = userDetails.getUsername();
        Usuario usuario = usuarioService.findByEmail(correo);

        List<Vehiculo> paginaVehiculos = vehiculoService.listarTodosLosTiposVehiculos();

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        for (Vehiculo vehiculo : paginaVehiculos) {
            vehiculo.setPrecioFormateado(currencyFormat.format(vehiculo.getPrecio()));
        }

        Vehiculo vehiculo = new Vehiculo();
        List<Tipo_Vehiculo> listatipovehi = tipoVehiculoService.listarTodosLosTiposVehiculos();
        List<Concesionario> licon = concesionario.ListarConce();
        List<Combustible> licom = combustible.ListarCom();
        model.addAttribute("lisTip", listatipovehi);
        model.addAttribute("lisConce", licon);
        model.addAttribute("liscombu", licom);
        model.addAttribute("Vehiculo", vehiculo);

        model.addAttribute("Vehiculos", paginaVehiculos);
        model.addAttribute("currentPage", page);
        model.addAttribute("usuario", usuario);
        return "cliente/dashboard"; // Plantilla específica para clientes
    }

    @GetMapping("/asesor/dashboard")
    public String showDashboardAsesor(@NotNull Model model, @NotNull Authentication authentication,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int pageSize) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String correo = userDetails.getUsername();
        Usuario usuario = usuarioService.findByEmail(correo);
        Page<Vehiculo> paginaVehiculos = vehiculoService.listarVehiculos(PageRequest.of(page - 1, pageSize));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        for (Vehiculo vehiculo : paginaVehiculos) {
            vehiculo.setPrecioFormateado(currencyFormat.format(vehiculo.getPrecio()));
        }

        Vehiculo vehiculo = new Vehiculo();
        List<Tipo_Vehiculo> listatipovehi = tipoVehiculoService.listarTodosLosTiposVehiculos();
        List<Concesionario> licon = concesionario.ListarConce();
        List<Combustible> licom = combustible.ListarCom();
        model.addAttribute("lisTip", listatipovehi);
        model.addAttribute("lisConce", licon);
        model.addAttribute("liscombu", licom);
        model.addAttribute("Vehiculo", vehiculo);

        model.addAttribute("Vehiculos", paginaVehiculos.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginaVehiculos.getTotalPages());
        model.addAttribute("usuario", usuario);
        return "asesor/dashboard"; // Plantilla específica para asesores
    }

    @GetMapping("/home")
    public String redireccionPorRol(@NotNull Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
            return "redirect:/cliente/dashboard";
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ASESOR"))) {
            return "redirect:/asesor/dashboard";
        }
        return "redirect:/login";
    }
}
