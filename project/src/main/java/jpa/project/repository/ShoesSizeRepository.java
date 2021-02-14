package jpa.project.repository;

import jpa.project.entity.ShoesSize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoesSizeRepository extends JpaRepository<ShoesSize,Long> {
}
