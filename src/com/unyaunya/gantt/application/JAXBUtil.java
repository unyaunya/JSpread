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
	 * obj���w�肵��file�ɕۑ�����B
	 * 
	 * @param obj
	 * @param file
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	public static void save(Object obj, File file) throws JAXBException, FileNotFoundException {
		@SuppressWarnings("rawtypes")
		Class klass = obj.getClass();
		JAXBContext jc = JAXBContext.newInstance(klass);
		OutputStream os = new FileOutputStream(file);
		Marshaller mu = jc.createMarshaller();  
		mu.marshal(obj, os); 
	}

	/**
	 * file��ǂݍ���ŁAklass�N���X�̃I�u�W�F�N�g�𐶐�����B
	 * @param klass
	 * @param file
	 * @return klass�N���X�̃I�u�W�F�N�gs
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static Object read(@SuppressWarnings("rawtypes") Class klass, File file) throws JAXBException, IOException {
		InputStream xmlInputStream = new FileInputStream(file);  
		JAXBContext jc = JAXBContext.newInstance(klass);  
		Unmarshaller u = jc.createUnmarshaller();  
		Object obj = u.unmarshal(xmlInputStream);  
		xmlInputStream.close();  		
		return obj;
	}
}
