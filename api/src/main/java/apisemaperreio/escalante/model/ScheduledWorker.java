package apisemaperreio.escalante.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class ScheduledWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "id_worker")
    @JsonIgnore
    private Worker worker;
    @ManyToOne
    @JoinColumn(name = "id_worker_role")
    private WorkerRole role;

}
