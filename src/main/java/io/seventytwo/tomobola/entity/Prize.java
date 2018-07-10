package io.seventytwo.tomobola.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UX_PRIZE", columnNames = {"number", "tombola_id"})})
public class Prize {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer number;
    private String name;

    @ManyToOne
    private Tombola tombola;

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
