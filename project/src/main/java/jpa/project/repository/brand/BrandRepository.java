package jpa.project.repository.brand;

import jpa.project.entity.Brand;
import jpa.project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand,Long> {
    Optional<Brand> findByName(@Param("username") String name);
}
