package com.eventer.user.data.repository;

import com.eventer.user.data.model.CategorySubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CategorySubscriptionRepository extends JpaRepository<CategorySubscription, Long> {

    @Query("SELECT s FROM CategorySubscription s WHERE s.categoryId IN :categoryIds")
    Set<CategorySubscription> findAllByCategoryId(@Param("categoryIds") Set<Long> categoryIds);

    Set<CategorySubscription> findAllByUserId(Long userId);

    Optional<CategorySubscription> findByCategoryIdAndUserId(Long categoryId, Long userId);
}
