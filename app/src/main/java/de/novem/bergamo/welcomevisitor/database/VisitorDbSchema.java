package de.novem.bergamo.welcomevisitor.database;

/**
 * Created by gfand on 21/01/2017.
 */

public class VisitorDbSchema {
    public static final class VisitorTable{
        public static final String NAME = "visitors";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String COMPANY = "company";
            public static final String LAST_NAME = "last_name";
            public static final String FIRST_NAME = "first_name";
            public static final String REF_PERSON = "ref_person";
            public static final String AIM = "aim";
            public static final String ARRIVAL_DATE = "arrival_date";
            public static final String DEPARTURE_DATE = "departure_date";
            public static final String COMPLETED = "completed";

        }
    }
}
