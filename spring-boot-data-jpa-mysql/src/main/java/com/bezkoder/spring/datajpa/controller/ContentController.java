package com.bezkoder.spring.datajpa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.datajpa.model.Content;
import com.bezkoder.spring.datajpa.repository.ContentRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class ContentController {

	@Autowired
	ContentRepository contentRepository;

	@GetMapping("/contents")
	public ResponseEntity<List<Content>> getAllContents(@RequestParam(required = false) String title) {
		try {
			List<Content> contents = new ArrayList<Content>();

			if (title == null)
				contents = contentRepository.findAll();
			else
				contents = contentRepository.findByTitleContaining(title);

			if (contents.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(contents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/contents/{id}")
	public ResponseEntity<Content> getContentById(@PathVariable("id") long id) {
		Optional<Content> contentData = contentRepository.findById(id);

		if (contentData.isPresent()) {
			return new ResponseEntity<>(contentData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/contents")
	public ResponseEntity<Content> createContent(@RequestBody Content content) {
		try {
			Content _content = contentRepository.save(new Content(content.getTitle(), content.getDescription(), false));
			return new ResponseEntity<>(_content, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/contents/{id}")
	public ResponseEntity<Content> updateContent(@PathVariable("id") long id, @RequestBody Content content) {
		Optional<Content> contentData = contentRepository.findById(id);

		if (contentData.isPresent()) {
			Content _content = contentData.get();
			_content.setTitle(content.getTitle());
			_content.setDescription(content.getDescription());
			_content.setPublished(content.isPublished());
			return new ResponseEntity<>(contentRepository.save(_content), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/contents/{id}")
	public ResponseEntity<HttpStatus> deleteContent(@PathVariable("id") long id) {
		try {
			contentRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/contents")
	public ResponseEntity<HttpStatus> deleteAllContents() {
		try {
			contentRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/contents/published")
	public ResponseEntity<List<Content>> findByPublished() {
		try {
			List<Content> contents = contentRepository.findByPublished(true);

			if (contents.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(contents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
