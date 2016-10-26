package com.ned;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

	
	
	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public @ResponseBody String provideUploadInfo(){
		return "you can upload file on this url";
	}
	
	
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public @ResponseBody String handleFileUpload (@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file){
		
	if(!file.isEmpty()){
		;
		try {

			byte[] bytes=file.getBytes();
			BufferedOutputStream stream=
					new BufferedOutputStream(new FileOutputStream(new File(name)));
			stream.write(bytes);
			stream.close();
			System.out.println(bytes);
		} catch (IOException e) {
			return "You failed to upload " + name + " => " + e.getMessage();
		}
		return "You successfully uploaded " + name + " !";
				
	}else {
		 return "You failed to upload " + name + " because the file was empty.";
	}
		
		
	}	// post /upload ends here
	
	
	
}
