package sk.upjs.ics.novotnyr.taskr;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.CheckBox;
import android.widget.EditText;

public class TaskDetailActivity extends Activity {
	private static final Long UNKNOWN_TASK_ID = null;
	private TaskDao taskDao;
	private EditText txtName;
	private CheckBox chkDone;
	private Long taskId;
	private EditText txtDeadline;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail);
		
		taskDao = new TaskDao(this);
		
		txtName = (EditText) findViewById(R.id.txtName);
		txtDeadline = (EditText) findViewById(R.id.txtDeadline);
		chkDone = (CheckBox) findViewById(R.id.chkDone);

		
		taskId = retrieveTaskId();
		if(taskId != UNKNOWN_TASK_ID) {
			Cursor taskCursor = taskDao.getTask(taskId);
			if(taskCursor.moveToNext()) {
				txtName.setText(taskCursor.getString(taskCursor.getColumnIndex(Database.Tasks.DESCRIPTION)));
				txtDeadline.setText(taskCursor.getString(taskCursor.getColumnIndex(Database.Tasks.DEADLINE)));
				chkDone.setChecked(taskCursor.getInt(taskCursor.getColumnIndex(Database.Tasks.IS_DONE)) != 0);
			} else {
				throw new IllegalStateException("No task with ID [" + + taskId + "] found in database.");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.task_detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_done:
			saveTaskAndFinish();
			return true;
		default:
			return super.onOptionsItemSelected(item);			
		}
	}


	private void saveTaskAndFinish() {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Database.Tasks._ID, taskId);
		contentValues.put(Database.Tasks.DESCRIPTION, txtName.getText().toString());
		contentValues.put(Database.Tasks.DEADLINE, txtDeadline.getText().toString());
		contentValues.put(Database.Tasks.IS_DONE, chkDone.isChecked() ? 1 : 0);
		
		taskDao.saveOrUpdate(contentValues);
		
		finish();
	}

	private Long retrieveTaskId() {
		Intent intent = getIntent();
		if(intent.hasExtra("taskId")) {
			return intent.getLongExtra("taskId", 0);
		} else {
			return null;
		}
	}
	

}