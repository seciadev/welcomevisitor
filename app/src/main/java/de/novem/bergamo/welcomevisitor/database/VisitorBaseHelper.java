package de.novem.bergamo.welcomevisitor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import de.novem.bergamo.welcomevisitor.database.VisitorDbSchema.VisitorTable;

/**
 * Created by gfand on 21/01/2017.
 */

public class VisitorBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "visitorBase.db";

    public VisitorBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + VisitorTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            VisitorTable.Cols.UUID + ", " +
            VisitorTable.Cols.COMPANY + ", " +
            VisitorTable.Cols.LAST_NAME + ", " +
            VisitorTable.Cols.FIRST_NAME + ", " +
            VisitorTable.Cols.EMPLOYEE_MEET + ", " +
            VisitorTable.Cols.PURPOSE_VISIT + ", " +
            VisitorTable.Cols.CHECKIN_DATE + ", " +
            VisitorTable.Cols.CHECKOUT_DATE + ", " +
            VisitorTable.Cols.COMPLETED +
            ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}
