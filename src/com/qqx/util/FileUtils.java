package com.qqx.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * �ļ���д������
 * @author qqx
 *
 */
public class FileUtils {
	public static int READ_FILE_LENGTH = 1024;
	
	/**
	 * ���ж�ȡ�ļ���������һ���ַ����ļ���
	 * @param file Ҫ��ȡ���ļ�ʵ��
	 * @return
	 * @throws Exception 
	 */
	public static List<String> readFileByLine(File file){
		BufferedReader reader = null;
		List<String> result = new ArrayList<String>();
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempStr = null;
			while((tempStr = reader.readLine()) != null)
			{
				result.add(tempStr);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * ������д���ļ�
	 */
	public static void writeFileByBytes(List<String> contents,File file)
	{ 
		BufferedOutputStream bOut = null;
		try {
			bOut = new BufferedOutputStream(new FileOutputStream(file));
			for(String s : contents){
				s +="\r\n";
				bOut.write(s.getBytes());
			}
			bOut.flush();
			bOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bOut != null)
			{
				try {
					bOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * ������д���ļ�
	 */
	public static void writeFileByBytes(String contents,File file){
		writeFileByBytes(contents,file,false);
	}
	
	/**
	 * ������д���ļ�
	 */
	public static void writeFileByBytes(String contents,File file,boolean append)
	{ 
		BufferedOutputStream bOut = null;
		try {
			bOut = new BufferedOutputStream(new FileOutputStream(file,append));
			bOut.write(contents.getBytes());
			bOut.flush();
			bOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bOut != null)
			{
				try {
					bOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	/**
	 * ��ȡ�ļ�������
	 * @param fileName
	 */
	public static String readFileByChars(File file)
	{
		StringBuffer content = new StringBuffer("");
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file),"GBK");
			char[] buffer = new char[READ_FILE_LENGTH];
			int tempchar;
			while((tempchar=reader.read(buffer)) != -1)
			{
				content.append(new String(buffer,0,tempchar));
			}
			reader.close();
			return content.toString();
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			if(reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
 
}
