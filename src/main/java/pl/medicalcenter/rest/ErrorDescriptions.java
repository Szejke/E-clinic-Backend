package pl.medicalcenter.rest;

public enum ErrorDescriptions {
    PATIENT_NOT_FOUND("Pacjent nie został znaleziony"),
    DOCTOR_NOT_FOUND("Doktor nie został znaleziony"),
    VISIT_NOT_FOUND("Wizyta nie zostala znaleziona"),
    RESERVATION("BLAD REZERWACJI"),
    VISIT_ALREADY_EXIST_IN_TIME("W podanym zakresie czasu juz istnieją wizyty"),
    VISIT_ABORT("BLAD ANULOWANIA REZERWACJI");

    private String value;

    private ErrorDescriptions(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
