package sk.upjs.ics.novotnyr.taskr;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;


public class TaskListActivity extends ListActivity {
	private static final int EDIT_OR_CREATE_TASK_REQUESTCODE = 100;
	private TaskDao taskDao;
	private SimpleCursorAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		taskDao = new TaskDao(this);
		resetListAdapter();
		registerForContextMenu(getListView());
	}
	
	@Override
	protected void onDestroy() {
		taskDao.close();
		super.onDestroy();
	}

	private void resetListAdapter() {
		String[] from = { Database.Tasks.DESCRIPTION, Database.Tasks.DEADLINE };
		int[] to = { android.R.id.text1, android.R.id.text2 };
		Cursor cursor = taskDao.list();	
		startManagingCursor(cursor);
		listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to);
		listAdapter.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if(columnIndex == cursor.getColumnIndex(Database.Tasks.DESCRIPTION)) { 
					int isDoneFlag = cursor.getInt(cursor.getColumnIndex(Database.Tasks.IS_DONE));
					TextView text1 = (TextView) view;
					if(isDoneFlag != 0) {
						text1.setPaintFlags(text1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
					} else {
						text1.setPaintFlags(text1.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
					}		
				}
				return false;
			}
		});
		setListAdapter(listAdapter);
	}


	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		onEditTask(id);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.task_list, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_new:
			createNewTask();
			return true;
		default: 		
			return super.onOptionsItemSelected(item);
		}
		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		if (view.getId() == this.getListView().getId()) {
			getMenuInflater().inflate(R.menu.task_item, menu);
		}
	}	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		
		switch (item.getItemId()) {
		case R.id.action_delete:
			onDeleteTask(menuInfo.id);
			return true;
		case R.id.action_edit:
			onEditTask(menuInfo.id);
			return true;
		case R.id.action_mark_completed:
			onMarkTaskCompleted(menuInfo.id);
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	private void onMarkTaskCompleted(long taskId) {
		taskDao.toggleDoneStatus(taskId, true);
		listAdapter.getCursor().requery();
	}

	private void onEditTask(long taskId) {
		Intent intent = new Intent(this, TaskDetailActivity.class);
		intent.putExtra("taskId", taskId);
		startActivityForResult(intent, EDIT_OR_CREATE_TASK_REQUESTCODE);
	}

	
	private void onDeleteTask(long taskId) {
		taskDao.delete(taskId);
	}

	private void createNewTask() {
		Intent intent = new Intent(this, TaskDetailActivity.class);
		startActivityForResult(intent, EDIT_OR_CREATE_TASK_REQUESTCODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == EDIT_OR_CREATE_TASK_REQUESTCODE) {
			resetListAdapter();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	

}

