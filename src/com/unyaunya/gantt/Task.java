package com.unyaunya.gantt;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * WBS上のタスクを表現するクラス
 * 
 * @author wata
 *
 */
public class Task {
	public static Task NULL = new Task("", new Date(0), new Date(0));
	
	@XmlAttribute
	protected Date startDate;
	@XmlAttribute
	protected Date endDate;
	@XmlAttribute
	protected String name;
	/*
	@XmlAttribute
	protected String description;
	@XmlAttribute
	protected String remark;
	@XmlAttribute
	protected float plannedValue;
	@XmlAttribute
	protected float earnedValue;
	@XmlAttribute
	protected float actualCost;
	*/
	
	/**
	 * デフォルトコンストラクタ
	 */
	public Task() {
		this("", null, null);
	}

	public Task(String name, Date start, Date end) {
		this.name = name;
		setStartDate(start);
		setEndDate(end);
		//description = "";
		//remark = "";
	}

	/**
	 * タスクの開始日を設定する。
	 * @param date
	 */
	public void setStartDate(Date date) {
		if(date == null) {
			this.startDate = new Date();
		}
		else {
			this.startDate = new Date(date.getTime());
		}
	}

	/**
	 * タスクの開始日を取得する。
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * タスクの終了日を設定する。
	 * @param date
	 */
	public void setEndDate(Date date) {
		if(date == null) {
			this.endDate = new Date(getStartDate().getTime());
		}
		else {
			this.endDate = new Date(date.getTime());
		}
	}
	
	/**
	 * タスクの終了日を取得する。
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * タスクの名称を設定する
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * タスクの名称を取得する
	 * @return
	 */
	public String getName() {
		return name;
	}
}
