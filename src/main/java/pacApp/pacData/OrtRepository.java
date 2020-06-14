package pacApp.pacData;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pacApp.pacModel.Haus;
import pacApp.pacModel.Ort;

public interface OrtRepository extends JpaRepository<Ort, Long> {
    Optional<Ort> findById(long id);
    Optional<Ort> findOneByPlz(long plz);
    List<Ort> findAllByPlz(long plz);
}
