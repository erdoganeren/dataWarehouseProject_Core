package pacApp.pacData;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pacApp.pacModel.Haus;
import pacApp.pacModel.Ort;

public interface HausRepository extends JpaRepository<Haus, Long> {
	Optional<Haus> findOneById(long id);
	List<Haus> findAllById(long id);
}
