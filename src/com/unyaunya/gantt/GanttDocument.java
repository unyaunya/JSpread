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
	private String[] columnName = {"ID", "�K�w", "�^�X�N��", "�J�n��", "�I����", "�i����", "sss"};
	public static Task NULL = new NullTask();
	
	@XmlElement
	private Calendar startDate;
	@XmlElement
	private Calendar endDate;
	
	@XmlElement
	protected List<Task> tasks;

	/**
	 * �f�t�H���g�R���X�g���N�^
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
	 * �^�X�N���X�g���擾����B
	 * @return
	 */
	public List<Task> getTasks() {
		return tasks;
	}
	/**
	 * index�Ŏw�肵���^�X�N���擾����B
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
	 * �^�X�N��ǉ�����B
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
