package com.example.examPlatform.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageController {
	@GetMapping("/getImg")
	@ResponseBody
	public HttpEntity<byte[]> getImg(@RequestParam String fileName){
		File fileImg = new File("/img/" + fileName + ".png");
		
		byte[] byteImg = null;
		HttpHeaders headers = null;
		try {
			byteImg = Files.readAllBytes(fileImg.toPath());
			headers = new HttpHeaders();
			
			headers.setContentType(MediaType.IMAGE_PNG);
			headers.setContentLength(byteImg.length);
		}catch(IOException e) {
			return null;
		}
		return new HttpEntity<byte[]>(byteImg,headers);
	}
}