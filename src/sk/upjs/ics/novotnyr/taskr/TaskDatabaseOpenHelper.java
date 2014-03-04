package sk.upjs.ics.novotnyr.taskr;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import sk.upjs.ics.android.util.CursorUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDatabaseOpenHelper extends SQLiteOpenHelper {
	
	private static TaskDatabaseOpenHelper INSTANCE; 
	
	public TaskDatabaseOpenHelper(Context context) {
		super(context, Database.NAME, CursorUtils.DEFAULT_CURSOR_FACTORY, Database.VERSION);
	}
	
	public static TaskDatabaseOpenHelper getInstance(Context context) {
		if(INSTANCE == null) {
			INSTANCE = new TaskDatabaseOpenHelper(context.getApplicationContext());
		}
		return INSTANCE;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sql = new StringBuilder("CREATE TABLE ")
			.append(Database.Task.TABLE_NAME)
			.append("(")
			.append(Database.Task._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
			.append(Database.Task.DESCRIPTION).append(" TEXT,")
			.append(Database.Task.IS_DONE).append(" BOOLEAN,")
			.append(Database.Task.DEADLINE).append(" DATE")
			.append(")");
		db.execSQL(sql.toString());
		
		ContentValues values = new ContentValues();
		values.put(Database.Task.DESCRIPTION, "Lunch");
		values.put(Database.Task.IS_DONE, 0);
		values.put(Database.Task.DEADLINE, new Date().toString());
		db.insert(Database.Task.TABLE_NAME, CursorUtils.NO_NULL_COLUMN_HACK, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// do nothing
	}

}

