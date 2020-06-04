package pacApp.pacData;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pacApp.pacModel.Ort;

public interface OrtRepository extends JpaRepository<Ort, Long> {
	Ort findById(long id);
    Optional<Ort> findOneByPlz(long plz);
}
