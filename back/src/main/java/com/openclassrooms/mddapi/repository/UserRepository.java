package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'accès aux données de l'entité {@link User}.
 * <p>
 * Fournit les opérations CRUD standards ainsi que des méthodes de recherche
 * personnalisées (par email, username, avec abonnements).
 * </p>
 * <p>
 * Les requêtes avec JOIN FETCH évitent le problème N+1 lors du chargement
 * des abonnements (topics).
 * </p>
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    /**
     * Recherche un utilisateur par son adresse email.
     *
     * @param email l'adresse email recherchée
     * @return un Optional contenant l'utilisateur si trouvé, vide sinon
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur recherché
     * @return un Optional contenant l'utilisateur si trouvé, vide sinon
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Recherche un utilisateur par email ou username.
     * <p>
     * Utile pour l'authentification qui accepte les deux formats.
     * </p>
     *
     * @param email    l'email recherché
     * @param username le username recherché
     * @return un Optional contenant l'utilisateur si trouvé, vide sinon
     */
    Optional<User> findByEmailOrUsername(String email, String username);
    
    /**
     * Vérifie si un username existe déjà en base.
     *
     * @param username le username à vérifier
     * @return true si le username existe, false sinon
     */
    Boolean existsByUsername(String username);
    
    /**
     * Vérifie si un email existe déjà en base.
     *
     * @param email l'email à vérifier
     * @return true si l'email existe, false sinon
     */
    Boolean existsByEmail(String email);
    
    /**
     * Charge un utilisateur avec ses abonnements (topics) en une seule requête.
     * <p>
     * Utilise JOIN FETCH pour éviter le problème de requêtes multiples (N+1).
     * </p>
     *
     * @param id l'identifiant de l'utilisateur
     * @return un Optional contenant l'utilisateur avec ses abonnements si trouvé, vide sinon
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.subscriptions WHERE u.id = :id")
    Optional<User> findByIdWithSubscriptions(@Param("id") Long id);
    
    /**
     * Charge un utilisateur avec ses abonnements par email en une seule requête.
     * <p>
     * Utilise JOIN FETCH pour éviter le problème de requêtes multiples (N+1).
     * </p>
     *
     * @param email l'email de l'utilisateur
     * @return un Optional contenant l'utilisateur avec ses abonnements si trouvé, vide sinon
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.subscriptions WHERE u.email = :email")
    Optional<User> findByEmailWithSubscriptions(@Param("email") String email);
}
