package ch.martinelli.oss.tombola.control;

import ch.martinelli.oss.tombola.entity.Prize;
import ch.martinelli.oss.tombola.entity.Tombola;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrizeRepository extends JpaRepository<Prize, Integer> {

    List<Prize> findAllByTombolaOrderByCreatedDateDesc(Tombola tombola);

    List<Prize> findAllByTombolaAndNumberOrderByCreatedDateDesc(Tombola tombola, Integer number);

    List<Prize> findAllByTombolaAndNameContainsIgnoreCaseOrderByCreatedDateDesc(Tombola tombola, String name);

    Optional<Prize> findByTombolaAndNumber(Tombola tombola, Integer number);

    long countByTombola(Tombola tombola);
}
