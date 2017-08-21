package de.novem.bergamo.welcomevisitor;

import java.util.Date;
import java.util.UUID;

/**
 * Created by gfand on 24/12/2016.
 */

public class Visitor {

    private UUID mId;

    private String mCompany;
    private String mLast_name;
    private String mFirst_name;
    private String mInternalRef;
    private String mAim;
    private Date mArrivalDate;
    private Date mDepartureDate;
    private boolean mCompleted;
    private String mPurpose;
    private String mEmployeeVisit;

    public String getEmployeeVisit() {
        return mEmployeeVisit;
    }

    public void setEmployeeVisit(String employeeVisit) {
        mEmployeeVisit = employeeVisit;
    }

    public String getPurpose() {
        return mPurpose;
    }

    public void setPurpose(String purpose) {
        mPurpose = purpose;
    }

    public UUID getId() {
        return mId;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String company) {
        mCompany = company;
    }

    public String getLast_name() {
        return mLast_name;
    }

    public void setLast_name(String last_name) {
        mLast_name = last_name;
    }

    public String getFirst_name() {
        return mFirst_name;
    }

    public void setFirst_name(String first_name) {
        mFirst_name = first_name;
    }

    public Date getArrivalDate() {
        return mArrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        mArrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return mDepartureDate;
    }

    public void setDepartureDate(Date departureDate) {
        mDepartureDate = departureDate;
    }

    public String getInternalRef() {
        return mInternalRef;
    }

    public void setInternalRef(String internalRef) {
        mInternalRef = internalRef;
    }

    public String getAim() {
        return mAim;
    }

    public void setAim(String aim) {
        mAim = aim;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public Visitor(){
        this(UUID.randomUUID());
    }

    public Visitor(UUID id){
        mId = id;
        mArrivalDate = new Date();
        mDepartureDate = new Date();
        //mCompleted = false;
    }
}
