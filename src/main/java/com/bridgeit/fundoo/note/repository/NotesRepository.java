package com.bridgeit.fundoo.note.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgeit.fundoo.note.model.Notes;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Long>
{
	
	Optional<Notes> findByTitle(String title);
	Optional<Notes> findById(long id);
	public List<Notes> findByUserid(long userid);
	public Notes findByIdAndUserid(long id,long userid);
}
