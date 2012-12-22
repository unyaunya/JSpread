package com.unyaunya.gantt;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

import com.unyaunya.gantt.application.JAXBUtil;
import com.unyaunya.swing.application.IDocument;
import com.unyaunya.swing.application.IDocumentHandler;

public class GanttDocumentHandler implements IDocumentHandler {
	public FileNameExtensionFilter ssdFilter = new FileNameExtensionFilter(
	        "ガントチャート(.xml)", "xml");

	public GanttDocumentHandler() {}
	

	@Override
	public IDocument load(File file) throws IOException {
    	if(!ssdFilter.accept(file)) {
    		return null;
    	}
		try {
			GanttDocument doc = (GanttDocument)JAXBUtil.read(GanttDocument.class, file);
    		return doc;
		} catch (JAXBException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void save(IDocument document, File file) throws IOException {
		if(!file.getPath().endsWith(".xml")) {
			file = new File(file.getPath()+".xml");
		}
    	if(!ssdFilter.accept(file)) {
    		throw new RuntimeException("!ssdFilter.accept(file)");
    	}
		try {
			JAXBUtil.save(document, file);
		} catch (JAXBException e) {
			throw new IOException(e);
		}
	}

}
