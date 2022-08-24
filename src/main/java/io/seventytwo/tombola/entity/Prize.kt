package io.seventytwo.tombola.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(uniqueConstraints = [UniqueConstraint(name = "UX_PRIZE", columnNames = ["number", "tombola_id"])])
class Prize(

    var number: Int? = null,

    var name: String? = null,

    @ManyToOne(optional = false)
    var tombola: Tombola? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @CreatedDate
    var createdDate: LocalDateTime? = null,

    @LastModifiedDate
    var lastModifiedDate: LocalDateTime? = null
)
