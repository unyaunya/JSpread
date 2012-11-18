package com.unyaunya.gantt.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


public class JAXBUtil {
	/**
	 * objを指定したfileに保存する。
	 * 
	 * @param obj
	 * @param file
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	public static void save(Object obj, File file) throws JAXBException, FileNotFoundException {
		Class klass = obj.getClass();
		JAXBContext jc = JAXBContext.newInstance(klass);
		OutputStream os = new FileOutputStream(file);
		Marshaller mu = jc.createMarshaller();  
		mu.marshal(obj, os); 
	}

	/**
	 * fileを読み込んで、klassクラスのオブジェクトを生成する。
	 * @param klass
	 * @param file
	 * @return klassクラスのオブジェクトs
	 * @throws JAXBException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static Object read(Class klass, File file) throws JAXBException, IOException {
		InputStream xmlInputStream = new FileInputStream(file);  
		JAXBContext jc = JAXBContext.newInstance(klass);  
		Unmarshaller u = jc.createUnmarshaller();  
		Object obj = u.unmarshal(xmlInputStream);  
		xmlInputStream.close();  		
		return obj;
	}
}
