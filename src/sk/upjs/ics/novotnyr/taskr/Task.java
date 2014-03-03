package sk.upjs.ics.novotnyr.taskr;
 
import java.util.Date;
 
public class Task {
	private Long id;
	
	private String name;
	
	private Date deadline;
	
	private boolean isDone;
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getDeadline() {
		return deadline;
	}
	
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	@Override
	public String toString() {
		return name + " " + (isDone ? " [hotová]" : "");
	}
}