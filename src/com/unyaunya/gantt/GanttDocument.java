package com.unyaunya.gantt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	private static String[] columnName = {"ID", "�K�w", "�^�X�N��", "�J�n��", "�I����", "�i����", "sss"};
	public static Task NULL = new NullTask();
	
	@XmlAttribute(name = "start-date")
	private Date startDate;
	@XmlAttribute(name = "end-date")
	private Date endDate;

	@XmlElementWrapper(name = "Tasks")
    @XmlElement(name = "Task")
	protected List<Task> tasks;

	/**
	 * �f�t�H���g�R���X�g���N�^
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
