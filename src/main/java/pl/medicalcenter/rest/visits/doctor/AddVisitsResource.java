package pl.medicalcenter.rest.visits.doctor;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddVisitsResource {
    private Integer duration;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer startHour;
    private Integer startMinutes;
    private Integer numberOfVisits;
    private BigDecimal price;
}
