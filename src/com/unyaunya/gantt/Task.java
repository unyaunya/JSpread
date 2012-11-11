package com.unyaunya.gantt;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * WBS上のタスクを表現するクラス
 * 
 * @author wata
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class Task {
	public static Task NULL = new Task("", null, null);
	
	@XmlAttribute(name = "level")
	protected int level;
	@XmlAttribute(name = "start-date")
	protected Date startDate;
	@XmlAttribute(name = "end-date")
	protected Date endDate;
	@XmlAttribute(name = "name")
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
	@SuppressWarnings("deprecation")
	public Task() {
		this("", new Date(), new Date(112,9,23));
	}

	public Task(String name, Date start, Date end) {
		this.name = name;
		setStartDate(start);
		setEndDate(end);
		setLevel(0);
		//description = "";
		//remark = "";
	}

	/**
	 * @param date
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * タスクの階層レベルを設定する。
	 * @param date
	 */
	public void setLevel(int level) {
		if(level < 0) {
			level = 0;
		}
		this.level = level;
	}
	
	/**
	 * タスクの開始日を設定する。
	 * @param date
	 */
	public void setStartDate(Date date) {
		this.startDate = date == null ? null : new Date(date.getTime());
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
		this.endDate = date == null ? null : new Date(date.getTime());
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
