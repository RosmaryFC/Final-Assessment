package nyc.c4q.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Created by c4q-rosmary on 9/27/15.
 */
public class LibraryDBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_LIBRARY_BOOKS_TABLE =
            "CREATE TABLE " + LibraryContract.BooksColumns.BOOKS_TABLE_NAME + " ("
            + LibraryContract.BooksColumns.BOOK_ID +                " INTEGER PRIMARY KEY, "
            + LibraryContract.BooksColumns.TITLE +                  " TEXT, "
            + LibraryContract.BooksColumns.AUTHOR +                 " TEXT, "
            + LibraryContract.BooksColumns.ISBN +                   " TEXT, "
            + LibraryContract.BooksColumns.ISBN13 +                 " TEXT, "
            + LibraryContract.BooksColumns.PUBLISHER +              " TEXT, "
            + LibraryContract.BooksColumns.PUBLISH_YEAR +           " INTEGER, "
            + LibraryContract.BooksColumns.CHECKED_OUT +            " INTEGER, "
            + LibraryContract.BooksColumns.CHECKED_OUT_BY +         " TEXT, "
            + LibraryContract.BooksColumns.CHECK_OUT_DATE_YEAR +    " INTEGER, "
            + LibraryContract.BooksColumns.CHECK_OUT_DATE_MONTH +   " INTEGER, "
            + LibraryContract.BooksColumns.CHECK_OUT_DATE_DAY +     " INTEGER, "
            + LibraryContract.BooksColumns.DUE_DATE_YEAR +          " INTEGER, "
            + LibraryContract.BooksColumns.DUE_DATE_MONTH +         " INTEGER, "
            + LibraryContract.BooksColumns.DUE_DATE_DAY +           " INTEGER " + ")";

    private static final String SQL_CREATE_LIBRARY_MEMBERS_TABLE =
            "CREATE TABLE " + LibraryContract.MembersColumns.MEMBERS_TABLE_NAME + " ("
            + LibraryContract.MembersColumns.MEMBER_ID +    " INTEGER PRIMARY KEY, "
            + LibraryContract.MembersColumns.NAME +         " TEXT, "
            + LibraryContract.MembersColumns.DOB_MONTH +    " INTEGER, "
            + LibraryContract.MembersColumns.DOB_DAY +      " INTEGER, "
            + LibraryContract.MembersColumns.DOB_YEAR +     " INTEGER, "
            + LibraryContract.MembersColumns.CITY +         " TEXT, "
            + LibraryContract.MembersColumns.STATE +        " TEXT " +")";

    private static final String SQL_DROP_TABLE_BOOKS =
            "DROP TABLE IF EXISTS " + LibraryContract.BooksColumns.BOOKS_TABLE_NAME;

    private static final String SQL_DROP_TABLE_MEMBERS =
            "DROP TABLE IF EXISTS " + LibraryContract.MembersColumns.MEMBERS_TABLE_NAME;

    private Resources resources;
    private String packageName;


    //todo: ASK SULTAN - why do we need resources and packageName
    public LibraryDBHelper(Context context, Resources resources, String packageName){
        super(context, LibraryContract.DB_NAME, null, LibraryContract.DB_VERSION);
        this.resources = resources;
        this.packageName = packageName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("LIBRARY DB HELPER ", "Query from Books table " + SQL_CREATE_LIBRARY_BOOKS_TABLE);
        Log.d("LIBRARY DB HELPER ", "Query from Members table " + SQL_CREATE_LIBRARY_MEMBERS_TABLE);

        db.execSQL(SQL_CREATE_LIBRARY_BOOKS_TABLE);
        db.execSQL(SQL_CREATE_LIBRARY_MEMBERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE_BOOKS);
        db.execSQL(SQL_DROP_TABLE_MEMBERS);
        onCreate(db);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public boolean hasData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM BOOKS", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        db.close();
        return count > 0;
    }

    public void populateData() {
        SQLiteDatabase db = getWritableDatabase();
        populateMembersData(db);
        populateBooksData(db);
        db.close();
    }

    //add new Book
    public void populateBooksData(SQLiteDatabase db){
        JSONArray books = getJsonArray("books");
        for (int i = 0, length = books.length(); i != length; ++i) {
            try{
                JSONObject book = books.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(LibraryContract.BooksColumns.BOOK_ID,      book.getInt("id"));
                values.put(LibraryContract.BooksColumns.TITLE,        book.getString("title"));
                values.put(LibraryContract.BooksColumns.AUTHOR,       book.getString("author"));
                values.put(LibraryContract.BooksColumns.ISBN,         book.getString("isbn"));
                values.put(LibraryContract.BooksColumns.ISBN13,       book.getString("isbn13"));
                values.put(LibraryContract.BooksColumns.PUBLISHER,    book.getString("publisher"));
                values.put(LibraryContract.BooksColumns.PUBLISH_YEAR, book.getInt("publishyear"));

                if (book.has("checkedout") && book.getBoolean("checkedout")) {
                    values.put(LibraryContract.BooksColumns.CHECKED_OUT,            book.getBoolean("checkedout") ? 1 : 0);
                    values.put(LibraryContract.BooksColumns.CHECKED_OUT_BY,         book.getInt("checkedoutby"));
                    values.put(LibraryContract.BooksColumns.CHECK_OUT_DATE_MONTH,   book.getInt("checkoutdatemonth"));
                    values.put(LibraryContract.BooksColumns.CHECK_OUT_DATE_DAY,     book.getInt("checkoutdateday"));
                    values.put(LibraryContract.BooksColumns.CHECK_OUT_DATE_YEAR,    book.getInt("checkoutdateyear"));
                    values.put(LibraryContract.BooksColumns.DUE_DATE_MONTH,         book.getInt("duedatemonth"));
                    values.put(LibraryContract.BooksColumns.DUE_DATE_DAY,           book.getInt("duedateday"));
                    values.put(LibraryContract.BooksColumns.DUE_DATE_YEAR,          book.getInt("duedateyear"));
                }

                db.insert(LibraryContract.BooksColumns.BOOKS_TABLE_NAME, null, values);

            }catch(JSONException e){
                Log.e("JSON EXCEPTION", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    private void populateMembersData(SQLiteDatabase db) {
        JSONArray members = getJsonArray("members");
        for (int i = 0, length = members.length(); i != length; ++i) {
            try {
                JSONObject member = members.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(LibraryContract.MembersColumns.MEMBER_ID,    member.getInt("id"));
                values.put(LibraryContract.MembersColumns.NAME,         member.getString("name"));
                values.put(LibraryContract.MembersColumns.DOB_MONTH,    member.getInt("dob_month"));
                values.put(LibraryContract.MembersColumns.DOB_DAY,      member.getInt("dob_day"));
                values.put(LibraryContract.MembersColumns.DOB_YEAR,     member.getInt("dob_year"));
                values.put(LibraryContract.MembersColumns.CITY,         member.getString("city"));
                values.put(LibraryContract.MembersColumns.STATE,        member.getString("state"));

                db.insert(LibraryContract.MembersColumns.MEMBERS_TABLE_NAME, null, values);
            }
            catch (JSONException e) {
                Log.e("JSON EXCEPTION", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private JSONArray getJsonArray(String file){
        int resId = resources.getIdentifier(file, "raw", packageName);
        InputStream is = resources.openRawResource(resId);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String line;
        StringBuilder sb = new StringBuilder();
        try{
            while((line = br.readLine()) != null){
                sb.append(line);
            }
        }catch (IOException e){
            Log.e("Database Exception", e.getMessage());
            e.printStackTrace();
        }

        String json = sb.toString();
        JSONArray jsonArray = null;
        try{

            jsonArray = new JSONArray(json);
        }catch(JSONException e){
            Log.e("JSON Exception", e.getMessage());
            e.printStackTrace();
        }

        return jsonArray;
    }

    public String getMember(String name) {
        SQLiteDatabase db = getReadableDatabase();

        final String[] projection = {
                LibraryContract.MembersColumns.MEMBER_ID,
                LibraryContract.MembersColumns.NAME,
                LibraryContract.MembersColumns.DOB_MONTH,
                LibraryContract.MembersColumns.DOB_DAY,
                LibraryContract.MembersColumns.DOB_YEAR,
                LibraryContract.MembersColumns.CITY,
                LibraryContract.MembersColumns.STATE
        };

        final String selection = LibraryContract.MembersColumns.NAME + "=?";

        final String[] selectionArgs = { name };

        Cursor cursor = db.query(LibraryContract.MembersColumns.MEMBERS_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.getCount() < 1) { return null; }

        cursor.moveToFirst();
        String result = "id: " + cursor.getInt(0) + "\n"
                        + "name: " + cursor.getString(1) + "\n"
                        + "dob: " + cursor.getInt(2) + "/" + cursor.getInt(3) + "/" + cursor.getInt(4) + "\n"
                        + "location: " + cursor.getString(5) + ", " + cursor.getString(6);
        db.close();
        return result;
    }

    public String getBook(String isbn) {
        SQLiteDatabase db = getReadableDatabase();

        final String[] projection = {
                LibraryContract.BooksColumns.BOOK_ID,
                LibraryContract.BooksColumns.TITLE,
                LibraryContract.BooksColumns.AUTHOR,
                LibraryContract.BooksColumns.ISBN,
                LibraryContract.BooksColumns.ISBN13,
                LibraryContract.BooksColumns.PUBLISHER,
                LibraryContract.BooksColumns.PUBLISH_YEAR
        };

        final String selection = LibraryContract.BooksColumns.ISBN + "=?";

        final String[] selectionArgs = { isbn };

        Cursor cursor = db.query(LibraryContract.BooksColumns.BOOKS_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.getCount() < 1) { return null; }

        cursor.moveToFirst();
        String result = "id: " + cursor.getInt(0) + "\n"
                        + "title: " + cursor.getString(1) + "\n"
                        + "author: " + cursor.getString(2) + "\n"
                        + "isbn: " + cursor.getString(3) + "\n"
                        + "isbn13: " + cursor.getString(4) + "\n"
                        + "publisher: " + cursor.getString(5) + "\n"
                        + "publication year: " + cursor.getInt(6);

        db.close();
        return result;
    }

    public String getCheckedOut(String name) {
        SQLiteDatabase db = getReadableDatabase();

        final String query =
                "SELECT b." + LibraryContract.BooksColumns.TITLE +
                        ", b." + LibraryContract.BooksColumns.AUTHOR +
                        ", b." + LibraryContract.BooksColumns.CHECK_OUT_DATE_MONTH +
                        ", b." + LibraryContract.BooksColumns.CHECK_OUT_DATE_DAY +
                        ", b." + LibraryContract.BooksColumns.CHECK_OUT_DATE_YEAR +
                        ", b." + LibraryContract.BooksColumns.DUE_DATE_MONTH +
                        ", b." + LibraryContract.BooksColumns.DUE_DATE_DAY +
                        ", b." + LibraryContract.BooksColumns.DUE_DATE_YEAR +
                        " FROM " + LibraryContract.MembersColumns.MEMBERS_TABLE_NAME + " m" +
                        " JOIN " + LibraryContract.BooksColumns.BOOKS_TABLE_NAME + " b" +
                        " ON m." + LibraryContract.MembersColumns.MEMBER_ID + "=b." + LibraryContract.BooksColumns.CHECKED_OUT_BY +
                        " WHERE m." + LibraryContract.MembersColumns.NAME + "=?" +
                        " ORDER BY b." + LibraryContract.BooksColumns.DUE_DATE_YEAR + " ASC" +
                        ", b." + LibraryContract.BooksColumns.DUE_DATE_MONTH + " ASC" +
                        ", b." + LibraryContract.BooksColumns.DUE_DATE_DAY + " ASC";

        final String[] selectionArgs = { name };

        Cursor cursor = db.rawQuery(query, selectionArgs);

        StringBuilder sb = new StringBuilder("name: " + name);

        while (cursor.moveToNext()) {
            sb.append(
                    "\n-----\n"
                            + "title: " + cursor.getString(0) + "\n"
                            + "author: " + cursor.getString(1) + "\n"
                            + "checkout date: " + cursor.getInt(2) + "/" + cursor.getInt(3) + "/" + cursor.getInt(4) + "\n"
                            + "due date: " + cursor.getInt(5) + "/" + cursor.getInt(6) + "/" + cursor.getInt(7)
            );
        }

        db.close();
        return sb.toString();
    }

    public void checkOut(int memberId, int bookId) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LibraryContract.BooksColumns.CHECKED_OUT, 1);
        values.put(LibraryContract.BooksColumns.CHECKED_OUT_BY, memberId);

        Calendar calendar = Calendar.getInstance();
        values.put(LibraryContract.BooksColumns.CHECK_OUT_DATE_YEAR,  calendar.get(Calendar.YEAR));
        values.put(LibraryContract.BooksColumns.CHECK_OUT_DATE_MONTH, calendar.get(Calendar.MONTH) + 1);
        values.put(LibraryContract.BooksColumns.CHECK_OUT_DATE_DAY,   calendar.get(Calendar.DATE));

        calendar.add(Calendar.DATE, 14);
        values.put(LibraryContract.BooksColumns.DUE_DATE_YEAR,  calendar.get(Calendar.YEAR));
        values.put(LibraryContract.BooksColumns.DUE_DATE_MONTH, calendar.get(Calendar.MONTH) + 1);
        values.put(LibraryContract.BooksColumns.DUE_DATE_DAY,   calendar.get(Calendar.DATE));

        final String where = LibraryContract.BooksColumns.BOOK_ID + "=?";
        final String[] whereArgs = { String.valueOf(bookId) };

        db.update(LibraryContract.BooksColumns.BOOKS_TABLE_NAME, values, where, whereArgs);
        db.close();
    }

    public boolean checkIn(int memberId, int bookId) {
        boolean isLate = checkLate(memberId, bookId);

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LibraryContract.BooksColumns.CHECKED_OUT, 0);
        values.putNull(LibraryContract.BooksColumns.CHECKED_OUT_BY);
        values.putNull(LibraryContract.BooksColumns.CHECK_OUT_DATE_YEAR);
        values.putNull(LibraryContract.BooksColumns.CHECK_OUT_DATE_MONTH);
        values.putNull(LibraryContract.BooksColumns.CHECK_OUT_DATE_DAY);
        values.putNull(LibraryContract.BooksColumns.DUE_DATE_YEAR);
        values.putNull(LibraryContract.BooksColumns.DUE_DATE_MONTH);
        values.putNull(LibraryContract.BooksColumns.DUE_DATE_DAY);

        final String where = LibraryContract.BooksColumns.BOOK_ID + "=?";
        final String[] whereArgs = { String.valueOf(bookId) };

        db.update(LibraryContract.BooksColumns.BOOKS_TABLE_NAME, values, where, whereArgs);
        db.close();

        return isLate;
    }

    private boolean checkLate(int memberId, int bookId) {
        SQLiteDatabase db = getReadableDatabase();

        final String[] projection = {
                LibraryContract.BooksColumns.DUE_DATE_YEAR,
                LibraryContract.BooksColumns.DUE_DATE_MONTH,
                LibraryContract.BooksColumns.DUE_DATE_DAY
        };

        final String selection = LibraryContract.BooksColumns._ID + "=? AND " +
                LibraryContract.BooksColumns.CHECKED_OUT_BY + "=?";

        final String[] selectionArgs = {
                String.valueOf(bookId),
                String.valueOf(memberId)
        };

        Cursor cursor = db.query(LibraryContract.BooksColumns.BOOKS_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.getCount() < 1) { return false; }

        cursor.moveToFirst();
        Calendar dueDate = Calendar.getInstance();
        dueDate.set(cursor.getInt(0), cursor.getInt(1) - 1, cursor.getInt(2));

        db.close();
        return dueDate.after(Calendar.getInstance());
    }


}
