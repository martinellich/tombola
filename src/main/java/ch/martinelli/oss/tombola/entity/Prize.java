package ch.martinelli.oss.tombola.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = { @UniqueConstraint(name = "UX_PRIZE", columnNames = { "number", "tombola_id" }) })
public class Prize {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	@NotNull
	private Integer number;

	@NotEmpty
	private String name;

	@ManyToOne(optional = false)
	private Tombola tombola;

	@SuppressWarnings("unused")
	@CreatedDate
	private LocalDateTime createdDate;

	@SuppressWarnings("unused")
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Tombola getTombola() {
		return tombola;
	}

	public void setTombola(Tombola tombola) {
		this.tombola = tombola;
	}

}
