package com.eventer.admin.data.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "image")
public class Image extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_seq")
    @SequenceGenerator(name = "image_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 255, unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(id, image.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Image() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
