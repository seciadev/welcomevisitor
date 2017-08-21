package de.novem.bergamo.welcomevisitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import de.novem.bergamo.welcomevisitor.database.VisitorBaseHelper;
import de.novem.bergamo.welcomevisitor.database.VisitorCursorWrapper;
import de.novem.bergamo.welcomevisitor.database.VisitorDbSchema;
import de.novem.bergamo.welcomevisitor.database.VisitorDbSchema.VisitorTable;

/**
 * Created by gfand on 26/12/2016.
 */

public class VisitorLab {
    private static VisitorLab sVisitorLab;

    //private List<Visitor> mVisitors;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static VisitorLab get(Context context) {
        if (sVisitorLab == null) {
            sVisitorLab = new VisitorLab(context);
        }
        return sVisitorLab;
    }

    private VisitorLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new VisitorBaseHelper(mContext).getWritableDatabase();
        //mVisitors = new ArrayList<>();
    }

    public void addVisitor(Visitor v) {
        ContentValues values = getContentValues(v);
        mDatabase.insert(VisitorTable.NAME, null, values);
    }

    public List<Visitor> getVisitors() {
        List<Visitor> visitors = new ArrayList<>();

        VisitorCursorWrapper cursor = queryVisitors(null,null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                visitors.add(cursor.getVisitor());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return visitors;
    }

    public Visitor getVisitor(UUID id) {
        VisitorCursorWrapper cursor = queryVisitors(
                VisitorTable.Cols.UUID + " = ?",
                new String[] { id.toString()}
        );

        try{
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getVisitor();
        } finally {
            cursor.close();
        }
    }

    public void updateVisitor(Visitor visitor){
        String uuidString = visitor.getId().toString();
        ContentValues values = getContentValues(visitor);
        mDatabase.update(VisitorTable.NAME, values, VisitorTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    private static ContentValues getContentValues(Visitor visitor){
        ContentValues values = new ContentValues();
        values.put(VisitorTable.Cols.UUID, visitor.getId().toString());
        values.put(VisitorTable.Cols.COMPANY, visitor.getCompany());
        values.put(VisitorTable.Cols.PURPOSE_VISIT, visitor.getPurpose());
        values.put(VisitorTable.Cols.EMPLOYEE_MEET, visitor.getEmployeeVisit());
        values.put(VisitorTable.Cols.LAST_NAME, visitor.getLast_name());
        values.put(VisitorTable.Cols.FIRST_NAME, visitor.getFirst_name());
        values.put(VisitorTable.Cols.CHECKIN_DATE, visitor.getArrivalDate().getTime());
        values.put(VisitorTable.Cols.CHECKOUT_DATE, visitor.getDepartureDate().getTime());
        values.put(VisitorTable.Cols.COMPLETED, visitor.isCompleted() ? 1 : 0);

        return values;

    }

    private VisitorCursorWrapper queryVisitors(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                VisitorTable.NAME,
                null, //columns
                whereClause,
                whereArgs,
                null, //groupby
                null, //having
                null //orderby
        );

        return new VisitorCursorWrapper(cursor);
    }

    public List<Visitor> getUncompletedVisitors() {
        List<Visitor> uncompletedVisitors = new ArrayList<>();
        List<Visitor> currentVisitors = getVisitors();
        for (Visitor visitor : currentVisitors) {
            if (!visitor.isCompleted()) {
                uncompletedVisitors.add(visitor);
            }
        }
        return uncompletedVisitors;
    }

    /*public List<Visitor> getUncompletedVisitors() {
        List<Visitor> visitors = new ArrayList<>();

        VisitorCursorWrapper cursor = queryVisitors(VisitorTable.Cols.COMPLETED + " == 0",null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                visitors.add(cursor.getVisitor());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return visitors;
    }*/

    public List<String> getUncompletedCompanies() {
        List<Visitor> uncompletedVisitors = getUncompletedVisitors();
        List<String> temp = new ArrayList<>();
        for (Visitor visitor : uncompletedVisitors) {
            /*if(!uncompletedCompanies.contains(visitor.getCompany())&&visitor.getCompany()!=null){
                uncompletedCompanies.add(visitor.getCompany());
            }*/
            temp.add(visitor.getCompany());
        }
        Set<String> uncompleted = new HashSet<>(temp);
        List<String> uncompletedCompanies = new ArrayList<>(uncompleted);

        return uncompletedCompanies;
    }

    public List<Visitor> getUncompletedVisitorsFromSameCompany(String company) {
        List<Visitor> uncompletedVisitorsFromSameCompany = new ArrayList<>();
        List<Visitor> uncompletedVisitors;
        uncompletedVisitors = getUncompletedVisitors();
        for (Visitor visitor : uncompletedVisitors) {
            if (visitor.getCompany().equals(company)) {
                uncompletedVisitorsFromSameCompany.add(visitor);
            }
        }
        return uncompletedVisitorsFromSameCompany;
    }

    public int getUncompletedVisitorNumberFromSameCompany(String company) {
        List<Visitor> uncompletedVisitorsFromSameCompany = getUncompletedVisitorsFromSameCompany(company);
        int numVis = uncompletedVisitorsFromSameCompany.size();
        return numVis;
    }

}
