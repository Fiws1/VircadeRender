package com.Proyect.Vircade.repository;

import com.Proyect.Vircade.modelo.*;
import com.Proyect.Vircade.modelo.Disponibilidad.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VehiculoRepositoryTest {

    @Autowired
    VehiculoRepository vehiculoRepository;

    @Autowired
    ConcesionarioRepository concesionarioRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    CombustibleRepository combustibleRepository;

    @BeforeEach
    void setUp() {
        Disponibilidad disponibilidad = new Disponibilidad(1, DisponibilidadEstado.SI);
        Concesionario concesionario = new Concesionario(20, "Central", "Calle 2 A sur #98 A 32",disponibilidad);
        Combustible combustible = new Combustible(20, "Gasolina");
        Tipo_Vehiculo tipoVehiculo = new Tipo_Vehiculo(20, "Moto");

        Vehiculo vehiculo = new Vehiculo(20,"Moto CRR500", "C", "200cc", "#00000", 2000000, "imagen.jpg", "2023", "Toyota",combustible, tipoVehiculo, concesionario,"$2.000.000.00");

        testEntityManager.persist(vehiculo);
    }

    @Test
    void findByMarcaVehiculoContainingIgnoreCaseOrModeloVehiculoContainingIgnoreCase() {
        Page<Vehiculo> vehiculos = vehiculoRepository.findByMarcaVehiculoContainingIgnoreCaseOrModeloVehiculoContainingIgnoreCase(
                "Toyota", "2023", PageRequest.of(0, 10)
        );

        assertFalse(vehiculos.isEmpty());
        assertEquals(20, vehiculos.getTotalElements());
    }
}