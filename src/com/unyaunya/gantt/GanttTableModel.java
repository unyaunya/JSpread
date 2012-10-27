package com.unyaunya.gantt;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class GanttTableModel extends AbstractTableModel {
    private static final Logger LOG = Logger.getLogger(GanttTableModel.class.getName());
	private int headerRowCount;
	private GanttDocument document;
	
	public GanttTableModel(GanttDocument document) {
		assert(document != null);
		this.document = document;
		headerRowCount = 3;
	}
	
	public GanttDocument getDocument() {
		return document;
	}

	public int getHeaderRowCount() {
		return headerRowCount;
	}
	
	public int getDataColumnCount() {
		return getDocument().getColumnCount();
	}

	public Date getStartDate() {
		return (getDocument() == null)  ? null : getDocument().getStartDate();
	}

	public Date getEndDate() {
		return (getDocument() == null)  ? null : getDocument().getEndDate();
	}
	
	public int getColumnCount() {
		int count = getDataColumnCount();
		if(getEndDate() != null && getStartDate() != null) {
			count += CalendarUtil.getDiffInDate(getEndDate(),  getStartDate());
		}
		return count;
	}

	public Object getValueAt(int row, int col) {
		if(row < getHeaderRowCount()) {
			if(col < getDataColumnCount()) {
				if(row == 0) {
					return getDocument().getColumnName(col);
				}
				return null;
			}
			Calendar c = Calendar.getInstance();
			c.setTime((Date)getStartDate().clone());
			c.add(Calendar.DATE, col - getDataColumnCount());
			switch(row) {
			case 0:
				return Integer.toString(c.get(Calendar.YEAR));
			case 1:
				return Integer.toString(1+c.get(Calendar.MONTH));
			case 2:
			default:
				return Integer.toString(c.get(Calendar.DATE));
			}
		}
		else {
			List<Task> taskList = getDocument().getTasks();
			int index = row - getHeaderRowCount(); 
			if(index < taskList.size() && col < getDataColumnCount()) {
				Task task = getDocument().getTask(index);
				switch(col) {
				case 0:
				case 1:
					return null;
				case 2:
					return task.getName();
				case 3:
					return task.getStartDate();
				case 4:
					return task.getEndDate();
				}
			}
			return null;
		}
	}

	public void setValueAt(Object value, int row, int col) {
		if(!isCellEditable(row, col)) {
			return;
		}
		Task task = getDocument().getTask(row - getHeaderRowCount());
		if(task == null) {
			getDocument().setTaskCount(row - getHeaderRowCount() + 1);
			task = getDocument().getTask(row - getHeaderRowCount());
		}
		assert(task != null);
		if(task == GanttDocument.NULL) {
			task = new Task();
			getDocument().getTasks().set(row - getHeaderRowCount(), task);
		}
		Date date;
		switch(col) {
		case 0:
		case 1:
			break;
		case 2:
			task.setName(value == null ? "NULL" : value.toString());
			break;
		case 3:
			LOG.info(value.toString());
			LOG.info(value.getClass().toString());
			date = CalendarUtil.toDate(value);
			if(date != null) {
				task.setStartDate(date);
			}
			break;
		case 4:
			LOG.info(value.toString());
			LOG.info(value.getClass().toString());
			date = CalendarUtil.toDate(value);
			if(date != null) {
				task.setEndDate(date);
			}
			break;
		}
	}

	public boolean isCellEditable(int row, int col) {
		if(row < getHeaderRowCount()) {
			return false;
		}
		if(col >= getDataColumnCount()) {
			return false;
		}
		return true;
	}

	private int getMinimmiumRowCount() {
		return 30;
	}

	@Override
	public int getRowCount() {
		return Math.max(getMinimmiumRowCount(), getHeaderRowCount() + getDocument().getTasks().size());
	}

	public Task getTask(int row) {
		if(getDocument() == null) {
			return Task.NULL;
		}
		int index = row - getHeaderRowCount();
		if(index < 0 || index >= getDocument().getTasks().size()) {
			return Task.NULL;
		}
		Task task = getDocument().getTask(index);
		assert(task != null);
		return task;
	}

	public Date getStartDate(int row) {
		return getTask(row).getStartDate();
	}

	public Date getEndDate(int row) {
		return getTask(row).getEndDate();
	}

	public int getStartDateColumn(int row) {
		return getColumnByDate(getTask(row).getStartDate());
	}

	public int getEndDateColumn(int row) {
		return getColumnByDate(getTask(row).getEndDate());
	}

	public int getColumnByDate(Date date) {
		if(date == null) {
			return -1;
		}
		if(date.getTime() < getStartDate().getTime()) {
			return -1;
		}
		if(date.getTime() > getEndDate().getTime()) {
			return -1;
		}
		date = CalendarUtil.round(date).getTime();
		int delta = (int)CalendarUtil.getDiffInDate(date, getStartDate());
		return getDataColumnCount() + delta;
	}
}
