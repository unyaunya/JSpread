package com.unyaunya.gantt;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GanttDocument {
	@XmlElement
	protected List<Task> tasks;

	/**
	 * �f�t�H���g�R���X�g���N�^
	 */
	public GanttDocument() {
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
		return tasks.get(index);
	}
	/**
	 * �^�X�N��ǉ�����B
	 * @param task
	 */
	public void addTask(Task task) {
		tasks.add(task);
	}
}
