package io.seventytwo.tomobola.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrizeRepository extends JpaRepository<Prize, Integer> {

    List<Prize> findAllByTombolaOrderByCreatedDateDesc(Tombola tombola);

    List<Prize> findAllByTombolaAndNumberOrderByCreatedDateDesc(Tombola tombola, Integer number);

    List<Prize> findAllByTombolaAndNameLikeOrderByCreatedDateDesc(Tombola tombola, String name);
}
