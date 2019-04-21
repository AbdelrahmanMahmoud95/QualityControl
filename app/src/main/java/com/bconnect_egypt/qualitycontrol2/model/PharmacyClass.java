package com.bconnect_egypt.qualitycontrol2.model;

public class PharmacyClass {

    public double code;
    public String code_string;

    public String ownerName;
    public String address;
    public String key;
    public String date_of_visit;
    public String phone;
    public String contactType;
    public String subscription;
    public String tele;
    public int totalPharmaciesInBranch;
    public double branchCode;
    public String pharmacyName;
    public String representativeName;
    public String branch;
    public String goal;
    public String motahedaCode;
    public String comment;

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PharmacyClass)) return false;
        PharmacyClass pharmacyClass = (PharmacyClass) other;
        return this.branch.equals(pharmacyClass.branch);
    }

    public String getMotahedaCode() {
        return motahedaCode;
    }

    public void setMotahedaCode(String motahedaCode) {
        this.motahedaCode = motahedaCode;
    }

    public double getCode() {
        return code;
    }

    public void setCode(double code) {
        this.code = code;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode_string() {
        return code_string;
    }

    public void setCode_string(String code_string) {
        this.code_string = code_string;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate_of_visit() {
        return date_of_visit;
    }

    public void setDate_of_visit(String date_of_visit) {
        this.date_of_visit = date_of_visit;
    }

    public String getPhone() {
        return phone;
    }

    public int getTotalPharmaciesInBranch() {
        return totalPharmaciesInBranch;
    }

    public void setTotalPharmaciesInBranch(int totalPharmaciesInBranch) {
        this.totalPharmaciesInBranch = totalPharmaciesInBranch;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public double getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(double branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}