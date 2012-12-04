package com.unyaunya.gantt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

class NullTask extends Task {
	
}

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "GanttDocument")
public class GanttDocument {
    private static final Logger LOG = Logger.getLogger(GanttDocument.class.getName());

	private static String[] columnName = {"ID", "階層", "タスク名", "開始日", "終了日", "進捗率", "sss"};
	public static Task NULL = new NullTask();
	
	@XmlAttribute(name = "start-date")
	private Date startDate;
	@XmlAttribute(name = "end-date")
	private Date endDate;

	@XmlElementWrapper(name = "Tasks")
    @XmlElement(name = "Task")
	protected ArrayList<Task> tasks;

	/**
	 * デフォルトコンストラクタ
	 */
	public GanttDocument() {
		Calendar end = CalendarUtil.today();
		Calendar start = (Calendar)end.clone();
		end.add(Calendar.DATE, 180);
		setStartDate(start.getTime());
		setEndDate(end.getTime());
		tasks = new ArrayList<Task>();
	}
	/**
	 * タスクリストを取得する。
	 * @return
	 */
	public List<Task> getTasks() {
		return tasks;
	}
	/**
	 * indexで指定したタスクを取得する。
	 * @param index
	 * @return
	 */
	public Task getTask(int index) {
		if(index < 0 || index >= tasks.size()) {
			return null;
		}
		return tasks.get(index);
	}
	/**
	 * タスクを追加する。
	 * @param task
	 */
	public void addTask(Task task) {
		tasks.add(task);
	}

	public void insertTask(int index, Task task) {
		if(index > tasks.size()) {
			for(int i = tasks.size(); i < index; i++) {
				LOG.info("i=" + i);
				tasks.add(new Task());
			}
		}
		tasks.add(index, task);
	}
	
	public Date getStartDate() {
		return this.startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}
	
	public void setStartDate(Date date) {
		this.startDate = new Date(date.getTime());
	}

	public void setEndDate(Date date) {
		this.endDate = new Date(date.getTime());
	}

	public int getColumnCount() {
		return columnName.length;
	}

	public String getColumnName(int col) {
		return columnName[col];
	}

	public void setTaskCount(int count) {
		assert(count >= 0);
		while(tasks.size() < count) {
			tasks.add(NULL);
		}
		while(tasks.size() > count) {
			tasks.remove(tasks.size()-1);
		}
	}
}
