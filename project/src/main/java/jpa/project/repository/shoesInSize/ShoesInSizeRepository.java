package jpa.project.repository.shoesInSize;

import jpa.project.entity.ShoesInSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShoesInSizeRepository extends JpaRepository<ShoesInSize,Long> {



}
