package pacApp.pacController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pacApp.pacData.OrtRepository;
import pacApp.pacModel.Ort;
import pacApp.pacModel.pacResponse.GenericResponse;

@RestController
public class OrtController {
	
	private final OrtRepository repository;
	
	public OrtController(OrtRepository repository) {
        this.repository = repository;
    }

	@CrossOrigin
    @GetMapping("/ortlist")
	public ResponseEntity<List<Ort>> getAllOrt() {
					
		List<Ort> orteList = this.repository.findAll();
		return new ResponseEntity<>(orteList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/ort", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> addOrt(@RequestBody Ort ort){
		
		this.repository.saveAndFlush(ort);
		GenericResponse response = new GenericResponse(HttpStatus.OK.value(), "Ort hinzgefügt!");
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
