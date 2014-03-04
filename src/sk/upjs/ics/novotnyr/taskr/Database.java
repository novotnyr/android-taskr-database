package sk.upjs.ics.novotnyr.taskr;

import android.provider.BaseColumns;

public interface Database {
    public static final String NAME = "tasks";
	public static final int VERSION = 1;

	public interface Task extends BaseColumns {
		public static final String TABLE_NAME = "task";

		public static final String DESCRIPTION = "description";
		public static final String IS_DONE = "is_done";
		public static final String DEADLINE = "deadline";
	}
}
