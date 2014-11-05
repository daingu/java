package com.mhdanh.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PdfController {

	private static final int BUFSIZE = 4096;

	@RequestMapping("/win/getpdf")
	public void getPath(@RequestParam("link") String link,HttpServletRequest request, HttpServletResponse response)
			throws IOException, InterruptedException {
		//create file name and filePath for export pdf
		Date d = new Date();
		String fileName = String.valueOf(d.getTime())+".pdf";
		String outPutPathFileNamePdf = "D:/" + fileName;
		
		exportPDF(link, outPutPathFileNamePdf);
		
		//downloadPDFFile(response, outPutPathFileNamePdf, fileName);
		viewPDFFile(response, outPutPathFileNamePdf, fileName);
	}
	@RequestMapping("/linux/getpdf")
	public void getPdfFromLinux(@RequestParam("link") String link,HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException{
		//create file name and filePath for export pdf
		Date d = new Date();
		String fileName = String.valueOf(d.getTime())+".pdf";
		String outPutPathFileNamePdf = "/opt/" + fileName;
		
		exportPDFOnLinux(link, outPutPathFileNamePdf);
		
		//downloadPDFFile(response, outPutPathFileNamePdf, fileName);
		viewPDFFile(response, outPutPathFileNamePdf, fileName);
	}
	
	@SuppressWarnings("unused")
	private void downloadFromCommonIo(HttpServletResponse response,
			String pathFileDownload, String fileNameDownload)
			throws IOException {
		FileInputStream fis = new FileInputStream(new File(pathFileDownload));
		IOUtils.copy(fis, response.getOutputStream());
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileNameDownload);
		response.flushBuffer();
	}
	private void viewPDFFile(HttpServletResponse response,String outputPath,String filename) throws IOException{
		File file = new File(outputPath);
		if(file.exists()){
			FileInputStream inputStream= new FileInputStream(file);
			ServletOutputStream outStream = response.getOutputStream();
	        byte[] buffer = new byte[4096];
	        int bytesRead = -1;
			response.setHeader("Content-Disposition","inline;filename="+filename);
			response.setContentLength((int) file.length());
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	        inputStream.close();
	        outStream.close();
		}
	}
	private void downloadPDFFile(HttpServletResponse response,String pathFileDownload,String fileNameDownload) throws IOException{
		response.setHeader("Content-Disposition", "attachment; filename ="+ fileNameDownload);
		OutputStream outp = null;
		FileInputStream in = null;
		try {
			outp = response.getOutputStream();
			in = new FileInputStream(new File(pathFileDownload));

			byte[] b = new byte[1024];
			int i = 0;

			while ((i = in.read(b)) > 0) {
				outp.write(b, 0, i);
			}
			outp.flush();
		} catch (Exception e) {
			System.out.println("Error!");
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
				in = null;
			}
			if (outp != null) {
				outp.close();
				outp = null;
			}
		}
	}

	private void exportPDF(String inputFilePath, String outFilePath)
			throws IOException, InterruptedException {
		String path = this.getClass().getClassLoader().getResource("")
				.getPath();
		String fullPath = URLDecoder.decode(path, "UTF-8");
		String pathArr[] = fullPath.split("/WEB-INF/classes/");
		String fullWebInfo = pathArr[0]
				+ "/WEB-INF/lib/execute/wkhtmltopdf.exe";
		String cmdLine = fullWebInfo + " " + inputFilePath + " " + outFilePath;
		Process process = Runtime.getRuntime().exec(cmdLine);
		process.waitFor();
	}
	
	private void exportPDFOnLinux(String inputFilePath, String outFilePath)
			throws IOException, InterruptedException {
		String path = this.getClass().getClassLoader().getResource("")
				.getPath();
		String fullPath = URLDecoder.decode(path, "UTF-8");
		String pathArr[] = fullPath.split("/WEB-INF/classes/");
		String fullWebInfo = "wkhtmltopdf";
		String cmdLine = fullWebInfo + " " + inputFilePath + " " + outFilePath;
		Process process = Runtime.getRuntime().exec(cmdLine);
		process.waitFor();
	}
}
