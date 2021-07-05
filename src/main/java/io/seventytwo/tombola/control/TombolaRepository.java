package io.seventytwo.tombola.control;

import io.seventytwo.tombola.entity.Tombola;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TombolaRepository extends JpaRepository<Tombola, Integer> {
}
