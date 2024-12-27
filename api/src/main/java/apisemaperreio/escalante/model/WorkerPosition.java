package apisemaperreio.escalante.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class WorkerPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    @ManyToOne
    @JoinColumn(name = "id_schedule_type")
    private ScheduleType scheduleType;
    @OneToMany(mappedBy = "position")
    private List<WorkerPriority> priorities = new ArrayList<>();
    @OneToMany(mappedBy = "position")
    private List<Worker> workers = new ArrayList<>();

}
