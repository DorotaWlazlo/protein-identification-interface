package com.example.proteinidentificationinterface;

import org.springframework.web.multipart.MultipartFile;

public class ConfigFormObject {
    private String name;
    private String email;
    private String title;
    private String databaseName;
    private String enzyme;
    private int missedCleavages;
    private String[] ptmFix;
    private String[] ptmVar;
    private double pepTol;
    private String pepTolUnit;
    private double fragTol;
    private String fragTolUnit;
    private MultipartFile file;

    public String getEnzyme() {
        return enzyme;
    }

    public void setEnzyme(String enzyme) {
        this.enzyme = enzyme;
    }

    public int getMissedCleavages() {
        return missedCleavages;
    }

    public void setMissedCleavages(int missedCleavages) {
        this.missedCleavages = missedCleavages;
    }

    public String[] getPtmFix() {
        return ptmFix;
    }

    public void setPtmFix(String[] ptmFix) {
        this.ptmFix = ptmFix;
    }

    public String[] getPtmVar() {
        return ptmVar;
    }

    public void setPtmVar(String[] ptmVar) {
        this.ptmVar = ptmVar;
    }

    public double getPepTol() {
        return pepTol;
    }

    public void setPepTol(double pepTol) {
        this.pepTol = pepTol;
    }

    public String getPepTolUnit() {
        return pepTolUnit;
    }

    public void setPepTolUnit(String pepTolUnit) {
        this.pepTolUnit = pepTolUnit;
    }

    public double getFragTol() {
        return fragTol;
    }

    public void setFragTol(double fragTol) {
        this.fragTol = fragTol;
    }

    public String getFragTolUnit() {
        return fragTolUnit;
    }

    public void setFragTolUnit(String fragTolUnit) {
        this.fragTolUnit = fragTolUnit;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

}
