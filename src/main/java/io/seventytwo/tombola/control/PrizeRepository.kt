package io.seventytwo.tombola.control

import io.seventytwo.tombola.entity.Prize
import io.seventytwo.tombola.entity.Tombola
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PrizeRepository : JpaRepository<Prize?, Int?> {

    fun findAllByTombolaOrderByCreatedDateDesc(tombola: Tombola?): List<Prize?>?

    fun findAllByTombolaAndNumberOrderByCreatedDateDesc(tombola: Tombola?, number: Int?): List<Prize?>?

    fun findAllByTombolaAndNameContainsIgnoreCaseOrderByCreatedDateDesc(tombola: Tombola?, name: String?): List<Prize?>?

    fun findByTombolaAndNumber(tombola: Tombola?, number: Int?): Optional<Prize?>?

    fun countByTombola(tombola: Tombola?): Long
}
