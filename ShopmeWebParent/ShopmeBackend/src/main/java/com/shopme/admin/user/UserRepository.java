package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer>, CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("email") String email);

    @Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :id")
    @Modifying
    public void updateUserEnabledStatus(@Param("id")Integer id, @Param("enabled")boolean enabled);

    // first parameter "?1"
    // special syntax to match any field with wildcards
    @Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName)" +
            " LIKE %:key_word%")
    public Page<User> findAllByKeyWord(@Param("key_word")String keyWord, Pageable pageable);

}
