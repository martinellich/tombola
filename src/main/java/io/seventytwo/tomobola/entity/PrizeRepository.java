package io.seventytwo.tomobola.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrizeRepository extends JpaRepository<Prize, Integer> {

    List<Prize> findAllByTombolaOrderByCreatedDateDesc(Tombola tombola);
}
