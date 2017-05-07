package de.novem.bergamo.welcomevisitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public static VisitorLab get(Context context) {
        if (sVisitorLab == null) {
            sVisitorLab = new VisitorLab(context);
        }
        return sVisitorLab;
    }

    private VisitorLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new VisitorBaseHelper(mContext).getWritableDatabase();
        mVisitors = new ArrayList<>();
    }

    public void addVisitor(Visitor v) {
        mVisitors.add(v);
    }

    public List<Visitor> getVisitors() {
        return mVisitors;
    }

    public Visitor getVisitor(UUID id) {
        for (Visitor visitor : mVisitors) {
            if (visitor.getId().equals(id)) {
                return visitor;
            }
        }
        return null;
    }

    public List<Visitor> getUncompletedVisitors() {
        List<Visitor> uncompletedVisitors = new ArrayList<>();
        for (Visitor visitor : mVisitors) {
            if (!visitor.isCompleted() && visitor != null) {
                uncompletedVisitors.add(visitor);
            }
        }
        return uncompletedVisitors;
    }

    public List<String> getUncompletedCompanies() {
        List<Visitor> uncompletedVisitors;
        uncompletedVisitors = getUncompletedVisitors();
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
