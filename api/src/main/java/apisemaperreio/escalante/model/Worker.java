package apisemaperreio.escalante.model;

import java.time.LocalDate;
import java.util.Set;

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
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String registration;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 1)
    private Character sex;

    @Column(nullable = false, unique = true)
    private Short seniority;

    @Column(nullable = false, unique = true, length = 50)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean driver;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean scheduleable;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_worker_position", nullable = false)
    private WorkerPosition position;

    @ManyToOne
    @JoinColumn(name = "id_schedule_type")
    private ScheduleType scheduleType;

    @OneToMany(mappedBy = "worker", orphanRemoval = true)
    private Set<ScheduledWorker> scheduledWorkers;

    @OneToMany(mappedBy = "worker", orphanRemoval = true)
    private Set<WorkerAbsence> workerAbsences;
    
}
