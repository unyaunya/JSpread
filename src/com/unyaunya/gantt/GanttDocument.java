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
	 * デフォルトコンストラクタ
	 */
	public GanttDocument() {
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
		return tasks.get(index);
	}
	/**
	 * タスクを追加する。
	 * @param task
	 */
	public void addTask(Task task) {
		tasks.add(task);
	}
}
