package com.bezkoder.spring.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.datajpa.model.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {
	List<Content> findByPublished(boolean published);
	List<Content> findByTitleContaining(String title);
}
