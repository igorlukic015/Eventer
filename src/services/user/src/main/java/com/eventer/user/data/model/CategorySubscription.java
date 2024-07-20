package com.eventer.user.data.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "category_subscription")
public class CategorySubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_subscription_seq")
    @SequenceGenerator(name = "category_subscription_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "categoryId", nullable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategorySubscription that = (CategorySubscription) o;
        return Objects.equals(id, that.id)
                || (Objects.equals(categoryId, that.categoryId) && Objects.equals(user, that.user));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryId, user.getId());
    }

    public User getUser() {
        return this.user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
