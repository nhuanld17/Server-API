package com.example.SERVER.service.company;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

	private final Cloudinary cloudinary;
	
	public Map upload(MultipartFile file) {
		try {
			Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
			return data;
		} catch (IOException io) {
			throw new RuntimeException("Image upload failed");
		}
	}
	
	public Map upload(MultipartFile file, String folder) {
		try {
			Map<String, Object> options = new HashMap<>();
			options.put("folder", folder);
			Map data = this.cloudinary.uploader().upload(file.getBytes(), options);
			return data;
		} catch (IOException io) {
			throw new RuntimeException("Image upload failed");
		}
	}
	
	public void delete(String url, String folder) {
		try {
			String fullPublicId = folder + "/" + extractPublicIdFromUrl(url);
			this.cloudinary.uploader().destroy(fullPublicId, Map.of());
		} catch (IOException io) {
			throw new RuntimeException("Image upload failed");
		}
	}
	
	private String extractPublicIdFromUrl(String url) {
		if (url == null || url.isEmpty()) {
			return null;
		}
		String[] parts = url.split("/");
		return parts[parts.length - 1].split("\\.")[0];
	}
}
