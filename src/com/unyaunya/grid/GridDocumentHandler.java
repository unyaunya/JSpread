package com.unyaunya.grid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.unyaunya.grid.sample.CsvTable;
import com.unyaunya.swing.application.IDocument;
import com.unyaunya.swing.application.IDocumentHandler;

public class GridDocumentHandler implements IDocumentHandler {
	public FileNameExtensionFilter csvFilter = new FileNameExtensionFilter(
	        "CSV & TXT", "csv", "txt");
	public FileNameExtensionFilter ssdFilter = new FileNameExtensionFilter(
	        "スプレッドシート", "ssd");
	
	public IDocument _load(File file) throws IOException {
    	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
    	try {
	    	return (GridDocument)ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			throw new IOException(e);
		} finally {
    		ois.close();
    	}
	}

	@Override
	public IDocument load(File file) throws IOException {
    	if(ssdFilter.accept(file)) {
    		return _load(file);
    	}
    	else if(csvFilter.accept(file)) {
	    	try {
	    		CSVReader reader = new CSVReader(new FileReader(file));
	    	    List<String[]> myEntries = reader.readAll();
	    	    reader.close();
	    	    GridDocument tmp = new GridDocument(new CsvTable(myEntries));
	    	    return tmp;
	    	} catch (IOException e) {
				e.printStackTrace();
			}
    	}
		return null;
	}
	
	public void _save(IDocument document, File file) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
    	oos.writeObject(document);
    	oos.close();
	}
	@Override
	public void save(IDocument document, File file) throws IOException {
		GridDocument doc = (GridDocument)document;
    	if(ssdFilter.accept(file)) {
			_save((IDocument)document, file);
    	}
    	else if(csvFilter.accept(file)) {
	    	CSVWriter writer;
			List<String[]> data = new ArrayList<String[]>();
			for(int i = 0; i < doc.getRowCount(); i++) {
				String[] row = new String[doc.getColumnCount()-1];
				for(int j = 0; j < doc.getColumnCount(); j++) {
					Object value = doc.getValueAt(i, j);
					if(value != null) {
						row[j-1] = value.toString();
					}
					else {
						row[j-1] = null;
					}
				}
				data.add(row);
			}
			try {
				writer = new CSVWriter(new FileWriter(file));
		        writer.writeAll(data);
		        writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    	}
	}
}
