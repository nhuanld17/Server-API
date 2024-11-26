package com.example.SERVER.controller.common;

import com.example.SERVER.service.company.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/cloudinary/upload")
@RequiredArgsConstructor
public class CloudinaryImageUploadController {
	private final CloudinaryService cloudinaryService;
	
	@PostMapping
	public ResponseEntity<Map> uploadImage(@RequestParam("image") MultipartFile image) {
		Map data = this.cloudinaryService.upload(image);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
}
