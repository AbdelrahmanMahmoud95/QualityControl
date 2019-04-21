package com.bconnect_egypt.qualitycontrol2.model;


public class AdminClass {
    public long total;
    public long totalNotVisit;
    public long totalVisit;
    public String emp_name;
    public String emp_code;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalNotVisit() {
        return totalNotVisit;
    }

    public void setTotalNotVisit(long totalNotVisit) {
        this.totalNotVisit = totalNotVisit;
    }

    public long getTotalVisit() {
        return totalVisit;
    }

    public void setTotalVisit(long totalVisit) {
        this.totalVisit = totalVisit;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }
}
