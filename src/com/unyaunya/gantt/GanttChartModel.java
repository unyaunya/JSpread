package com.unyaunya.gantt;

import java.awt.Color;
import java.text.SimpleDateFormat;

import javax.swing.SwingConstants;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.GridModel;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.format.CellFormatModel;
import com.unyaunya.grid.format.CellSpanModel;
import com.unyaunya.grid.format.RangedColor;
import com.unyaunya.grid.format.RangedFormat;
import com.unyaunya.grid.format.RangedInteger;

public class GanttChartModel extends GridModel {
	public GanttChartModel() {
		super(new GanttTableModel(new GanttDocument()));
		initHeader();
	}

	public GanttTableModel getGanttTableModel() {
		return (GanttTableModel)getTableModel();
	}
	
	@Override
	public int getLevel(int row) {
		GanttTableModel tm = getGanttTableModel();
		Task task = tm.getTask(row);
		if(task != null) {
			return task.getLevel();
		}
		return 0;
	}

	/**
	 * ヘッダとして扱う行の数を返す。
	 * 派生クラスでオーバライドする。
	 * @return
	 */
	public int getHeaderRowCount() {
		return getGanttTableModel().getHeaderRowCount();
	}

	private GanttDocument getGanttDocument() {
		return getGanttTableModel().getDocument();
	}
	
	/**
	 * /ヘッダ部で、所要のフォーマット設定を行う
	 */
	private void initHeader() {
		CellSpanModel sm = getCellSpanModel();
		GanttTableModel tm = getGanttTableModel();
		CellFormatModel fm = getCellFormatModel();

		//
		//セル結合
		//

		//チャート左側のデータ領域
		for(int i = 0; i < getGanttDocument().getColumnCount(); i++) {
			sm.add(new CellRange(0, i, tm.getHeaderRowCount()-1, i));
		}
		
		//チャート右側のカレンダ領域
		int cmin = getGanttDocument().getColumnCount();
		int cmax = tm.getColumnCount()-1;
		for(int i = 0; i < tm.getHeaderRowCount(); i++) {
			int cs = 0;
			Object v= null;
			Object pv = null;
			for(int j = cmin; j <= cmax; j++) {
				v = tm.getValueAt(i, j);
				if(j == cmin) {
					cs = cmin;
					pv = v;
					assert(v != null);
				}
				else {
					if(!v.equals(pv)) {
						if(j+1 > cs) {
							sm.add(new CellRange(i, cs, i, j-1));
						}
						cs = j;
					}
				}
				pv = v;
			}
			sm.add(new CellRange(i, cs, i, cmax));
		}
		
		//
		//背景色、縦横アライメント
		//
		IRange headerRange = new CellRange(0, 0, tm.getHeaderRowCount()-1, cmax);
		fm.addHorizontalAlignment(new RangedInteger(SwingConstants.CENTER, headerRange));
		fm.addVerticalAlignment(new RangedInteger(SwingConstants.CENTER, headerRange));
		fm.addBackgroundColor(new RangedColor(Color.PINK, headerRange));

		//日付フォーマットの設定
		fm.addFormat(new RangedFormat(new SimpleDateFormat("yyyy/MM/dd"), new CellRange(tm.getHeaderRowCount(), 3, tm.getRowCount()-1, 4)));
	}
	
	public GanttChartModel readDocument(GanttDocument document) { 
		getGanttTableModel().setDocument(document);
		return this;
	}

	@Override
	public void insertRow(int row) {
		getGanttTableModel().insertTask(row);
	}

}
