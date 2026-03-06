package lk.ijse.medihelpbackend.repo;


import lk.ijse.medihelpbackend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,String> {

    User findByEmail(String userName);

    boolean existsByEmail(String userName);

}