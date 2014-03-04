package sk.upjs.ics.novotnyr.taskr;

import static sk.upjs.ics.android.util.CursorUtils.NO_GROUP_BY;
import static sk.upjs.ics.android.util.CursorUtils.NO_HAVING;
import static sk.upjs.ics.android.util.CursorUtils.NO_NULL_COLUMN_HACK;
import static sk.upjs.ics.android.util.CursorUtils.NO_PROJECTION;
import static sk.upjs.ics.android.util.CursorUtils.NO_SELECTION;
import static sk.upjs.ics.android.util.CursorUtils.NO_SELECTION_ARGS;
import static sk.upjs.ics.android.util.CursorUtils.NO_SORT_ORDER;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TaskDao {

	private SQLiteDatabase database;
	private TaskDatabaseOpenHelper taskDatabaseOpenHelper;

	public TaskDao(Context context) {
		taskDatabaseOpenHelper = TaskDatabaseOpenHelper.getInstance(context);
		database = taskDatabaseOpenHelper.getWritableDatabase();
	}

	public Cursor list() {
		return database.query(Database.Task.TABLE_NAME, 
				NO_PROJECTION, 
				NO_SELECTION, NO_SELECTION_ARGS, 
				NO_GROUP_BY, NO_HAVING, NO_SORT_ORDER);
	}
	
	public void close() {
		taskDatabaseOpenHelper.close();
	}

	public Cursor getTask(long taskId) {
		return database.query(Database.Task.TABLE_NAME, 
				NO_PROJECTION, 
				Database.Task._ID + "= ?", new String[] { String.valueOf(taskId) },
				NO_GROUP_BY, NO_HAVING, NO_SORT_ORDER);
	}

	public void saveOrUpdate(ContentValues taskContentValues) {
		String stringId = taskContentValues.getAsString(Database.Task._ID);
		if(stringId != null) {
			String[] whereArgs = { stringId };
			database.update(Database.Task.TABLE_NAME, taskContentValues, Database.Task._ID + "= ?", whereArgs);
		} else {
			database.insert(Database.Task.TABLE_NAME, NO_NULL_COLUMN_HACK, taskContentValues);
		}
	}

	public void delete(long taskId) {
		database.delete(Database.Task.TABLE_NAME, Database.Task._ID + "= ?", toWhereArgs(taskId));
	}
	
	public void toggleDoneStatus(long taskId, boolean doneStatus) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Database.Task.IS_DONE, doneStatus ? 1 : 0);
		database.update(Database.Task.TABLE_NAME, contentValues, Database.Task._ID + "= ?", toWhereArgs(taskId));
	}
	
	private String[] toWhereArgs(long id) {
		return new String[] { String.valueOf(id) };		
	}
}
