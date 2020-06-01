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

import pacApp.pacData.HausRepository;
import pacApp.pacModel.Haus;
import pacApp.pacModel.pacResponse.GenericResponse;

@RestController
public class HausController {
	
	private final HausRepository repository;
	
	public HausController(HausRepository repository) {
        this.repository = repository;
    }
	
	@CrossOrigin
    @GetMapping("/hauslist")
	public ResponseEntity<List<Haus>> getAllOrt() {
					
		List<Haus> hausList = this.repository.findAll();
		return new ResponseEntity<>(hausList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/haus", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> addOrt(@RequestBody Haus haus){
		
		this.repository.saveAndFlush(haus);
		GenericResponse response = new GenericResponse(HttpStatus.OK.value(), "Haus hinzgefügt!");
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
