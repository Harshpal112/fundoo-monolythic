package com.bridgeit.fundoo.label.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgeit.fundoo.label.model.Labels;


public interface LabelRepo extends JpaRepository<Labels, Long> {
	Optional<Labels> findByLabelname(String labelname);
	public List<Labels> findByUserid(long userid);
	public Labels findByLabelidAndUserid(long labelid,long userid);
	
}
