package de.novem.bergamo.welcomevisitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.UUID;

import de.novem.bergamo.welcomevisitor.database.VisitorBaseHelper;
import de.novem.bergamo.welcomevisitor.database.VisitorCursorWrapper;
import de.novem.bergamo.welcomevisitor.database.VisitorDbSchema;
import de.novem.bergamo.welcomevisitor.database.VisitorDbSchema.VisitorTable;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

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

        VisitorCursorWrapper cursor = queryVisitors(null,null,null);

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
                new String[] { id.toString()},
                null
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

    public List<Visitor> getVisitorsSortName(boolean ascending) {
        List<Visitor> visitors = new ArrayList<>();
        VisitorCursorWrapper cursor;
        if(ascending) {
            cursor = queryVisitors(null, null, VisitorTable.Cols.LAST_NAME + " COLLATE NOCASE");
        }else{
            cursor = queryVisitors(null, null, VisitorTable.Cols.LAST_NAME + " COLLATE NOCASE DESC");
        }

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

    public List<Visitor> getVisitorSortPurpose(boolean ascending){
        List<Visitor> visitors = new ArrayList<>();
        VisitorCursorWrapper cursor;
        if(ascending) {
            cursor = queryVisitors(null, null, VisitorTable.Cols.PURPOSE_VISIT + " COLLATE NOCASE");
        }else{
            cursor = queryVisitors(null, null, VisitorTable.Cols.PURPOSE_VISIT + " COLLATE NOCASE DESC");
        }

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

    public List<Visitor> getVisitorSortCompany(boolean ascending){
        List<Visitor> visitors = new ArrayList<>();
        VisitorCursorWrapper cursor;
        if(ascending) {
            cursor = queryVisitors(null, null, VisitorTable.Cols.COMPANY + " COLLATE NOCASE");
        }else{
            cursor = queryVisitors(null, null, VisitorTable.Cols.COMPANY + " COLLATE NOCASE DESC");
        }

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


    public List<Visitor> getVisitorSortDate(boolean ascending) {
        List<Visitor> visitors = new ArrayList<>();
        VisitorCursorWrapper cursor;
        if(ascending) {
            cursor = queryVisitors(null, null, VisitorTable.Cols.CHECKIN_DATE);
        }else{
            cursor = queryVisitors(null, null, VisitorTable.Cols.CHECKIN_DATE + " DESC");
        }

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

    public List<String> getCompanies(){
        List<String> companies = new ArrayList<>();
        VisitorCursorWrapper cursor;
        cursor = queryVisitors(null, null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                companies.add(cursor.getVisitor().getCompany());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return companies;


    }

    /*public List<Visitor> getVisitorBetweenDates() {
        List<Visitor> visitors = new ArrayList<>();
        VisitorCursorWrapper cursor;
        //boooh
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        long timeNow = new Date(System.currentTimeMillis()).getTime();
        long time1dayAgo = new Date(System.currentTimeMillis() - DAY_IN_MS).getTime();
        cursor = queryVisitors(VisitorTable.Cols.CHECKIN_DATE + " >= ?", new String[] {"" + time1dayAgo},null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                visitors.add(cursor.getVisitor());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return visitors;
    }*/

    public List<Visitor> getVisitorQuery(String query) {
        List<Visitor> visitors = new ArrayList<>();
        VisitorCursorWrapper cursor;
        cursor = queryVisitors(VisitorTable.Cols.LAST_NAME + " LIKE ?" + " OR " + VisitorTable.Cols.COMPANY + " LIKE ?" + " OR " + VisitorTable.Cols.PURPOSE_VISIT + " LIKE ?", new String[] { "%"+query+"%","%"+query+"%","%"+query+"%" }, null);


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

    private VisitorCursorWrapper queryVisitors(String whereClause, String[] whereArgs, String orderBy){
        Cursor cursor = mDatabase.query(
                VisitorTable.NAME,
                null, //columns
                whereClause,
                whereArgs,
                null, //groupby
                null, //having
                orderBy //orderby
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


    public void exportToExcel(){
        List<Visitor> visitors = new ArrayList<>();

        VisitorCursorWrapper cursor = queryVisitors(null,null,null);
        File sd = Environment.getExternalStorageDirectory();
        String csvFile = "guestbook.xls";
        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {

            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);
            // column and row
            sheet.addCell(new Label(0, 0, "Company"));
            sheet.addCell(new Label(1, 0, "Last Name"));
            sheet.addCell(new Label(2, 0, "First Name"));
            sheet.addCell(new Label(3, 0, "Purpose of the visit"));
            sheet.addCell(new Label(4, 0, "Employee to meet"));
            sheet.addCell(new Label(5, 0, "Check-in date"));
            sheet.addCell(new Label(6, 0, "Check-out date"));
            sheet.addCell(new Label(7, 0, "Completed"));

            if (cursor.moveToFirst()) {
                do {


                    String company = cursor.getString(cursor.getColumnIndex(VisitorTable.Cols.COMPANY));
                    String last_name = cursor.getString(cursor.getColumnIndex(VisitorTable.Cols.LAST_NAME));
                    String first_name = cursor.getString(cursor.getColumnIndex(VisitorTable.Cols.FIRST_NAME));
                    String purpose_visit = cursor.getString(cursor.getColumnIndex(VisitorTable.Cols.PURPOSE_VISIT));
                    String employee_meet = cursor.getString(cursor.getColumnIndex(VisitorTable.Cols.EMPLOYEE_MEET));
                    Date checkin_date = new Date(cursor.getLong(cursor.getColumnIndex(VisitorTable.Cols.CHECKIN_DATE)));
                    Date checkout_date = new Date (cursor.getLong(cursor.getColumnIndex(VisitorTable.Cols.CHECKOUT_DATE)));
                    String completed = cursor.getString(cursor.getColumnIndex(VisitorTable.Cols.COMPLETED));

                    DateFormat customDateFormat = new DateFormat ("dd MMM yyyy hh:mm:ss");
                    WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);


                    int i = cursor.getPosition() + 1;
                    sheet.addCell(new Label(0, i, company));
                    sheet.addCell(new Label(1, i, last_name));
                    sheet.addCell(new Label(2, i, first_name));
                    sheet.addCell(new Label(3, i, purpose_visit));
                    sheet.addCell(new Label(4, i, employee_meet));
                    DateTime checkin_cell = new DateTime(5,i,checkin_date,dateFormat, DateTime.GMT);
                    sheet.addCell(checkin_cell);
                    DateTime checkout_cell = new DateTime(6,i,checkout_date, dateFormat, DateTime.GMT);
                    sheet.addCell(checkout_cell);
                    sheet.addCell(new Label(7, i, completed));

                } while (cursor.moveToNext());
            }

            cursor.close();
            workbook.write();
            workbook.close();

        } catch(Exception e){
            e.printStackTrace();
        }


    }

}
