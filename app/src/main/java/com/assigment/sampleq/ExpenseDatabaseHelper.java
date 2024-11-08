import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

public class ExpenseDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AMOUNT = "amount";

    private static final String TABLE_PARTICIPANTS = "participants";
    private static final String COLUMN_EXPENSE_ID = "expense_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CONTRIBUTION = "contribution";

    public ExpenseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create expenses table
        db.execSQL("CREATE TABLE " + TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_AMOUNT + " REAL)");

        // Create participants table
        db.execSQL("CREATE TABLE " + TABLE_PARTICIPANTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EXPENSE_ID + " INTEGER, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_CONTRIBUTION + " REAL, "
                + "FOREIGN KEY(" + COLUMN_EXPENSE_ID + ") REFERENCES " + TABLE_EXPENSES + "(" + COLUMN_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    public long addExpense(String title, double amount, Map<String, Double> participants) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert expense
        ContentValues expenseValues = new ContentValues();
        expenseValues.put(COLUMN_TITLE, title);
        expenseValues.put(COLUMN_AMOUNT, amount);
        long expenseId = db.insert(TABLE_EXPENSES, null, expenseValues);

        // Insert each participant
        for (Map.Entry<String, Double> entry : participants.entrySet()) {
            ContentValues participantValues = new ContentValues();
            participantValues.put(COLUMN_EXPENSE_ID, expenseId);
            participantValues.put(COLUMN_NAME, entry.getKey());
            participantValues.put(COLUMN_CONTRIBUTION, entry.getValue());
            db.insert(TABLE_PARTICIPANTS, null, participantValues);
        }

        return expenseId;
    }

    public Map<String, Double> getParticipants(long expenseId) {
        Map<String, Double> participants = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PARTICIPANTS, new String[]{COLUMN_NAME, COLUMN_CONTRIBUTION},
                COLUMN_EXPENSE_ID + "=?", new String[]{String.valueOf(expenseId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                double contribution = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CONTRIBUTION));
                participants.put(name, contribution);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return participants;
    }
}
