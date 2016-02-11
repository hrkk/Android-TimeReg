package timereg.roninit.dk.timereg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by kasper on 06/02/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    // Books table name
    private static final String TABLE_BOOKS = "books";

    private static final String TABLE_TIME_REG = "timereg";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";

    private static final String KEY_TASK_ID = "taskid";
    private static final String KEY_TASK_COMPANY = "company";
    private static final String KEY_TASK_NUMBER = "tasknumber";
    private static final String KEY_TASK_NAME = "taskname";
    private static final String KEY_TASK_HOURS = "hours";
    private static final String KEY_TASK_INFORMATION = "infomation";
    private static final String KEY_TASK_DATE = "date";
    private static final String KEY_TASK_SUBMIT_DATE = "submitdate";


    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_AUTHOR};
    private static final String[] COLUMNS_TIME_REG = {KEY_ID, KEY_TASK_ID, KEY_TASK_COMPANY,KEY_TASK_NUMBER,
            KEY_TASK_NAME,KEY_TASK_HOURS,KEY_TASK_INFORMATION, KEY_TASK_DATE,KEY_TASK_SUBMIT_DATE};

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TimeRegDB";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE books ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                "author TEXT )";


        String CREATE_TIME_REG_TABLE = "CREATE TABLE timereg ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "taskid TEXT, "+
                "company TEXT, "+
                "tasknumber TEXT, "+
                "taskname TEXT, "+
                "hours TEXT, "+
                "infomation TEXT, "+
                "date TEXT, "+
                "submitdate TEXT )";
        // create books table
       // db.execSQL(CREATE_BOOK_TABLE);

        db.execSQL(CREATE_TIME_REG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        //db.execSQL("DROP TABLE IF EXISTS books");
        db.execSQL("DROP TABLE IF EXISTS timereg");

        // create fresh books table
        this.onCreate(db);
    }

    /*
    public void addBook(Book book){
        //for logging
        Log.d("addBook", book.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, book.getTitle()); // get title
        values.put(KEY_AUTHOR, book.getAuthor()); // get author

        // 3. insert
        db.insert(TABLE_BOOKS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }
    */

    public void addTimeReg(TimeRegTask timeReg){
        //for logging
        Log.d("addTimeReg", timeReg.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_ID, timeReg.getTaskId()); // get title
        values.put(KEY_TASK_COMPANY, timeReg.getCompany()); // get title
        values.put(KEY_TASK_NUMBER, timeReg.getTaskNumber()); // get author
        values.put(KEY_TASK_NAME, timeReg.getTaskName()); // get author
        values.put(KEY_TASK_HOURS, timeReg.getHours()); // get author
        values.put(KEY_TASK_INFORMATION, timeReg.getAdditionInfomation()); // get author
        values.put(KEY_TASK_DATE, timeReg.getDate()); // get author
        values.put(KEY_TASK_SUBMIT_DATE, timeReg.getSubmitDate()); // get author

        // 3. insert
        db.insert(TABLE_TIME_REG, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }


    /*

    public Book getBook(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_BOOKS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Book book = new Book();
        book.setId(Integer.parseInt(cursor.getString(0)));
        book.setTitle(cursor.getString(1));
        book.setAuthor(cursor.getString(2));

        //log
        Log.d("getBook(" + id + ")", book.toString());

        // 5. return book
        return book;
    }
*/

    public TimeRegTask getTimeReg(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TIME_REG, // a. table
                        COLUMNS_TIME_REG, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        TimeRegTask timeReg = new TimeRegTask();
        timeReg.setId(Integer.parseInt(cursor.getString(0)));
        timeReg.setTaskId(cursor.getString(1));
        timeReg.setCompany(cursor.getString(2));
        timeReg.setTaskNumber(cursor.getString(3));
        timeReg.setTaskName(cursor.getString(4));
        timeReg.setHours(cursor.getString(5));
        timeReg.setAdditionInfomation(cursor.getString(6));
        timeReg.setDate(cursor.getString(7));
        timeReg.setSubmitDate(cursor.getString(8));

        //log
        Log.d("getTimeReg(" + id + ")", timeReg.toString() +" date "+timeReg.getDate());

        db.close();
        // 5. return book
        return timeReg;
    }


/*


    public List<Book> getAllBooks() {
        List<Book> books = new LinkedList<Book>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_BOOKS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Book book = null;
        if (cursor.moveToFirst()) {
            do {
                book = new Book();
                book.setId(Integer.parseInt(cursor.getString(0)));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));

                // Add book to books
                books.add(book);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", books.toString());

        // return books
        return books;
    }
*/
    public List<TimeRegTask> getAllTimeReg() {
        List<TimeRegTask> books = new LinkedList<TimeRegTask>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_TIME_REG;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        TimeRegTask timeReg = null;
        if (cursor.moveToFirst()) {
            do {
                timeReg = new TimeRegTask();
                timeReg.setId(Integer.parseInt(cursor.getString(0)));
                timeReg.setTaskId(cursor.getString(1));
                timeReg.setCompany(cursor.getString(2));
                timeReg.setTaskNumber(cursor.getString(3));
                timeReg.setTaskName(cursor.getString(4));
                timeReg.setHours(cursor.getString(5));
                timeReg.setAdditionInfomation(cursor.getString(6));
                timeReg.setDate(cursor.getString(7));
                timeReg.setSubmitDate(cursor.getString(8));

                // Add book to books
                books.add(timeReg);
            } while (cursor.moveToNext());
        }

        if(!books.isEmpty())
            Log.d("getAllTimeReg()", books.toString()+" date "+timeReg.getDate());

        db.close();
        // return books
        return books;
    }

    public List<TimeRegTask> getAllTimeRegByDate(String date) {
        List<TimeRegTask> books = new LinkedList<TimeRegTask>();

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TIME_REG, // a. table
                        COLUMNS_TIME_REG, // b. column names
                        " date = ?", // c. selections
                        new String[]{String.valueOf(date)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. go over each row, build book and add it to list
        TimeRegTask timeReg = null;
        if (cursor.moveToFirst()) {
            do {
                timeReg = new TimeRegTask();
                timeReg.setId(Integer.parseInt(cursor.getString(0)));
                timeReg.setTaskId(cursor.getString(1));
                timeReg.setCompany(cursor.getString(2));
                timeReg.setTaskNumber(cursor.getString(3));
                timeReg.setTaskName(cursor.getString(4));
                timeReg.setHours(cursor.getString(5));
                timeReg.setAdditionInfomation(cursor.getString(6));
                timeReg.setDate(cursor.getString(7));
                timeReg.setSubmitDate(cursor.getString(8));

                // Add book to books
                books.add(timeReg);
            } while (cursor.moveToNext());
        }

        Log.d("getAllTimeRegByDate("+ date +")", books.toString());

        db.close();
        // return books
        return books;
    }

    /*
    public int updateBook(Book book) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", book.getTitle()); // get title
        values.put("author", book.getAuthor()); // get author

        // 3. updating row
        int i = db.update(TABLE_BOOKS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(book.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }
    */


    public int updateTimeReg(TimeRegTask timeReg) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_ID, timeReg.getTaskId()); // get title
        values.put(KEY_TASK_COMPANY, timeReg.getCompany()); // get title
        values.put(KEY_TASK_NUMBER, timeReg.getTaskNumber()); // get author
        values.put(KEY_TASK_NAME, timeReg.getTaskName()); // get author
        values.put(KEY_TASK_HOURS, timeReg.getHours()); // get author
        values.put(KEY_TASK_INFORMATION, timeReg.getAdditionInfomation()); // get author
        values.put(KEY_TASK_DATE, timeReg.getDate()); // get author
        values.put(KEY_TASK_SUBMIT_DATE, timeReg.getSubmitDate()); // get author

        // 3. updating row
        int i = db.update(TABLE_TIME_REG, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(timeReg.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    /*
    public void deleteBook(Book book) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_BOOKS, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(book.getId())}); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteBook", book.toString());

    }
    */

    public void deleteTimeReg(TimeRegTask timeReg) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_TIME_REG, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(timeReg.getId()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteBook", timeReg.toString());
    }

}