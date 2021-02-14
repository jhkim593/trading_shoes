package jpa.project.repository.shoes;

import jpa.project.entity.Shoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface ShoesRepository extends JpaRepository<Shoes,Long> ,ShoesRepositoryCustom{

    Optional<Shoes> findByName(@Param("name")String name);
}
