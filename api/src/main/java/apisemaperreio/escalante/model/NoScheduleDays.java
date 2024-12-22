package apisemaperreio.escalante.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class NoScheduleDays {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

}
