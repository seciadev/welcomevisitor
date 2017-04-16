package de.novem.bergamo.welcomevisitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.novem.bergamo.welcomevisitor.database.VisitorBaseHelper;

/**
 * Created by gfand on 26/12/2016.
 */

public class VisitorLab {
    private static VisitorLab sVisitorLab;

    private List<Visitor> mVisitors;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static VisitorLab get(Context context){
        if (sVisitorLab == null){
            sVisitorLab = new VisitorLab(context);
        }
        return sVisitorLab;
    }

    private VisitorLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new VisitorBaseHelper(mContext).getWritableDatabase();
        mVisitors = new ArrayList<>();
    }

    public void addVisitor(Visitor v){
        mVisitors.add(v);
    }

    public List<Visitor> getVisitors(){
        return mVisitors;
    }

    public Visitor getVisitor(UUID id){
        for (Visitor visitor: mVisitors){
            if(visitor.getId().equals(id)){
                return visitor;
            }
        }
        return null;
    }

    public List<Visitor> getUncompletedVisitors(){
        List<Visitor> uncompletedVisitors = new ArrayList<>();
        for (Visitor visitor: mVisitors){
            if(!visitor.isCompleted()){
                uncompletedVisitors.add(visitor);
            }
        }
        return uncompletedVisitors;
    }
}
