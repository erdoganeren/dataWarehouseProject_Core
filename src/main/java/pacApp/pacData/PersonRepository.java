package pacApp.pacData;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pacApp.pacModel.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	Person findById(long id);
	Optional<Person> findOneByNachname(String nachname);
	List<Person> findAllById(long id);
}
