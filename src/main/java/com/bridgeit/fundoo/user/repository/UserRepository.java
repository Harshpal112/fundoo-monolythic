package com.bridgeit.fundoo.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bridgeit.fundoo.user.model.User;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	 
	 Optional<User> findByEmailid(String emailid);
	 Optional<User> findByUserid(long id);
	
	
}
