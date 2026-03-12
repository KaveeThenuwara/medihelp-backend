package lk.ijse.medihelpbackend.Entity;

import jakarta.persistence.*;
import lk.ijse.medihelpbackend.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID doctorId;

    private String specialization;

    private String hospital;

    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}