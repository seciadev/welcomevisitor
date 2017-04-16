package de.novem.bergamo.welcomevisitor;

/**
 * Created by gfand on 13/04/2017.
 */

public class VisitorFromSameCompany {

    private String lastName;
    private String firstName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public VisitorFromSameCompany(String last, String first){
        lastName = last;
        firstName = first;
    }

    public VisitorFromSameCompany(){
        lastName = null;
        firstName = null;
    }
}
