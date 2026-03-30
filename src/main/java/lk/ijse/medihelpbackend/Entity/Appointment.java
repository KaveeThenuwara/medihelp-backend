package lk.ijse.medihelpbackend.Entity;
import jakarta.persistence.*;
import lk.ijse.medihelpbackend.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;
@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID appointmentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User patient;



    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private Date appointmentDate;

    private String status;

}