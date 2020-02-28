package com.mundo.delivery.repository;

import com.mundo.delivery.models.PuntoDeVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface puntoDeVentaRepository extends JpaRepository<PuntoDeVenta, Integer> {
}
