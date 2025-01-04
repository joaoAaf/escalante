package apisemaperreio.escalante.model;

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
public class WorkerPriority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Short priority;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_worker_position", nullable = false)
    private WorkerPosition position;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_worker_role", nullable = false)
    private WorkerRole role;

}
