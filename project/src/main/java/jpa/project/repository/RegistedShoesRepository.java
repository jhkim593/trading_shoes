package jpa.project.repository;

import jpa.project.entity.RegistedShoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RegistedShoesRepository extends JpaRepository<RegistedShoes,Long> {
    @Query("select r from RegistedShoes r where r.id=:id and r.shoesStatus=jpa.project.entity.ShoesStatus.BID")
    Optional<RegistedShoes> findByIdAndShoesStatus();
}
