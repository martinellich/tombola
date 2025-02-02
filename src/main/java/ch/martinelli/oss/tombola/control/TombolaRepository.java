package ch.martinelli.oss.tombola.control;

import ch.martinelli.oss.tombola.entity.Tombola;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TombolaRepository extends JpaRepository<Tombola, Integer> {
}
