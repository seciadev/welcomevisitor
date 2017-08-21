package de.novem.bergamo.welcomevisitor.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import de.novem.bergamo.welcomevisitor.Visitor;
import de.novem.bergamo.welcomevisitor.database.VisitorDbSchema.VisitorTable;

/**
 * Created by gfand on 17/08/2017.
 */

public class VisitorCursorWrapper extends CursorWrapper {
    public VisitorCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Visitor getVisitor() {
        String uuidString = getString(getColumnIndex(VisitorTable.Cols.UUID));
        String company = getString(getColumnIndex(VisitorTable.Cols.COMPANY));
        String purpose_visit = getString(getColumnIndex(VisitorTable.Cols.PURPOSE_VISIT));
        String employee_meet = getString(getColumnIndex(VisitorTable.Cols.EMPLOYEE_MEET));
        String last_name = getString(getColumnIndex(VisitorTable.Cols.LAST_NAME));
        String first_name = getString(getColumnIndex(VisitorTable.Cols.FIRST_NAME));
        long checkin_date = getLong(getColumnIndex(VisitorTable.Cols.CHECKIN_DATE));
        long checkout_date = getLong(getColumnIndex(VisitorTable.Cols.CHECKOUT_DATE));
        int completed = getInt(getColumnIndex(VisitorTable.Cols.COMPLETED));

        Visitor visitor = new Visitor(UUID.fromString(uuidString));
        visitor.setCompany(company);
        visitor.setPurpose(purpose_visit);
        visitor.setEmployeeVisit(employee_meet);
        visitor.setLast_name(last_name);
        visitor.setFirst_name(first_name);
        visitor.setArrivalDate(new Date(checkin_date));
        visitor.setDepartureDate(new Date(checkout_date));
        visitor.setCompleted(completed != 0);

        return visitor;
    }
}
