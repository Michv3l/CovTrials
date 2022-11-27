package com.example.sars_cov_3;

public class Volunteer {

    String vaccine;
    String dose;


    public Volunteer(String vaccine, String dose) {
        this.vaccine = vaccine;
        this.dose = dose;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }


}
