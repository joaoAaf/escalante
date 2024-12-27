package apisemaperreio.escalante.model;

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
    private Short priority;
    @ManyToOne
    @JoinColumn(name = "id_worker_position")
    private WorkerPosition position;
    @ManyToOne
    @JoinColumn(name = "id_worker_role")
    private WorkerRole role;

}
