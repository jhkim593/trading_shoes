package jpa.project.repository;

import jpa.project.entity.ShoesInSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoesInSizeRepository extends JpaRepository<ShoesInSize,Long> {



}
