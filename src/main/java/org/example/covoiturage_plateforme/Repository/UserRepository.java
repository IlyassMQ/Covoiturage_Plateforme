package org.example.covoiturage_plateforme.Repository;

import org.example.covoiturage_plateforme.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User,UUID> {
    Optional<User> findByEmailAndActiveTrue(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    List<User> findAllByLastNameAndActiveTrue(String lastName);

    List<User> findAllByActiveTrue();

   Optional<User> findByIdAndActiveTrue(UUID userId);
}
