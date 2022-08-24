package io.seventytwo.tombola.control

import io.seventytwo.tombola.entity.Tombola
import org.springframework.data.jpa.repository.JpaRepository

interface TombolaRepository : JpaRepository<Tombola?, Int?>
