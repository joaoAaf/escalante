package apisemaperreio.escalante.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Absence {

    private Worker worker;
    private LocalDate startDate;
    private LocalDate endDate;
    private String absenceType;
    private String absenceDescription;

}
