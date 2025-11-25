package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'accès aux données des utilisateurs
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    // Méthode qui recherche un utilisateur à partir de son email
    Optional<User> findByEmail(String email);
    
    // Méthode qui recherche un utilisateur à partir de son username
    Optional<User> findByUsername(String username);
    
    // Méthode qui recherche un utilisateur par email ou username
    Optional<User> findByEmailOrUsername(String email, String username);
    
    // Vérifie si un username existe déjà
    Boolean existsByUsername(String username);
    
    // Vérifie si un email existe déjà
    Boolean existsByEmail(String email);
    
    // Charge un utilisateur avec ses abonnements (JOIN FETCH pour éviter le problème N+1)
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.subscriptions WHERE u.id = :id")
    Optional<User> findByIdWithSubscriptions(@Param("id") Long id);
    
    // Charge un utilisateur avec ses abonnements par email
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.subscriptions WHERE u.email = :email")
    Optional<User> findByEmailWithSubscriptions(@Param("email") String email);
}