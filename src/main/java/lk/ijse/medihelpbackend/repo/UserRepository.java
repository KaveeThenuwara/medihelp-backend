package lk.ijse.medihelpbackend.repo;

import lk.ijse.medihelpbackend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(String role);
    long countByRole(String role);
}