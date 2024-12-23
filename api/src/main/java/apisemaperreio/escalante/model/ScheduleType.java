package apisemaperreio.escalante.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class ScheduleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false)
    private Integer daysWorked;
    @Column(nullable = false)
    private Integer daysOff;
    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer multiplier;
    @Column(length = 255)
    private String description;
    @OneToMany(mappedBy = "scheduleType")
    private List<WorkerPosition> positions = new ArrayList<>();

}
