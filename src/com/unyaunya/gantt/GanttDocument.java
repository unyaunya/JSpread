package com.unyaunya.gantt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

class NullTask extends Task {
	
}

@XmlRootElement
public class GanttDocument {
	private String[] columnName = {"ID", "階層", "タスク名", "開始日", "終了日", "進捗率", "sss"};
	public static Task NULL = new NullTask();
	
	@XmlElement
	private Calendar startDate;
	@XmlElement
	private Calendar endDate;
	
	@XmlElement
	protected List<Task> tasks;

	/**
	 * デフォルトコンストラクタ
	 */
	public GanttDocument() {
		Calendar end = CalendarUtil.today();
		Calendar start = (Calendar)end.clone();
		end.add(Calendar.DATE, 180);
		setStartDate(start);
		setEndDate(end);
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

	public Calendar getStartDate() {
		return this.startDate;
	}

	public Calendar getEndDate() {
		return this.endDate;
	}
	
	public void setStartDate(Calendar date) {
		this.startDate = date;
	}

	public void setEndDate(Calendar date) {
		this.endDate = date;
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
