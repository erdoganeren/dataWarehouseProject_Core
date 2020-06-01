package pacApp.pacData;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pacApp.pacModel.Haus;

public interface HausRepository extends JpaRepository<Haus, Long> {
	Haus findById(long id);
}
