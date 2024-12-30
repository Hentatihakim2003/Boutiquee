package com.ecom.repsitory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import com.ecom.model.Adresse;
import com.ecom.model.Users;
@Repository
public interface AdresseRepsitory extends JpaRepository<Adresse, Long> {
	Optional<Adresse> findById(Long id);
}

