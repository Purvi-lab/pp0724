package com.rental.repository;

import com.rental.model.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolRepository extends JpaRepository<Tool, String> {
    Tool findByToolCode(String toolCode);
}
