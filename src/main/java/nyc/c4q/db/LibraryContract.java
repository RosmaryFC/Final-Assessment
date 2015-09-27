package nyc.c4q.db;

import android.provider.BaseColumns;

/**
 * Created by c4q-rosmary on 9/27/15.
 */
public class LibraryContract {

    public static final String DB_NAME = "final_assessment_library_db";
    public static final int DB_VERSION = 1;

    public LibraryContract(){

    }

    public static abstract class BooksColumns implements BaseColumns{
        public static final String BOOKS_TABLE_NAME         = "books";

        public static final String BOOK_ID                  = BaseColumns._ID;
        public static final String TITLE                    = "title";
        public static final String AUTHOR                   = "author";
        public static final String ISBN                     = "isbn";
        public static final String ISBN13                   = "isbn13";
        public static final String PUBLISHER                = "publisher";
        public static final String PUBLISH_YEAR             = "publishyear";
        public static final String CHECKED_OUT              = "checkedout";
        public static final String CHECKED_OUT_BY           = "checkedoutby";
        public static final String CHECK_OUT_DATE_YEAR      = "checkoutdateyear";
        public static final String CHECK_OUT_DATE_MONTH     = "checkoutdatemonth";
        public static final String CHECK_OUT_DATE_DAY       = "checkoutdateday";
        public static final String DUE_DATE_YEAR            = "duedateyear";
        public static final String DUE_DATE_MONTH           = "duedatemonth";
        public static final String DUE_DATE_DAY             = "duedateday";

    }

    public static abstract class MembersColumns implements BaseColumns{
        public static final String MEMBERS_TABLE_NAME   = "members";

        public static final String MEMBER_ID            = BaseColumns._ID;
        public static final String NAME                 = "name";
        public static final String DOB_MONTH            = "dob_month";
        public static final String DOB_DAY              = "dob_day";
        public static final String DOB_YEAR             = "dob_year";
        public static final String CITY                 = "city";
        public static final String STATE                = "state";

    }

}
