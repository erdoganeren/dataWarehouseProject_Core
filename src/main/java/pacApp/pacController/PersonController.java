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

import pacApp.pacData.PersonRepository;
import pacApp.pacModel.Person;
import pacApp.pacModel.pacResponse.GenericResponse;

@RestController
public class PersonController {
	private final PersonRepository repository;
	
	public PersonController(PersonRepository repository) {
        this.repository = repository;
    }
	@CrossOrigin
    @GetMapping("/personlist")
	public ResponseEntity<List<Person>> getAllOrt() {
					
		List<Person> personList = this.repository.findAll();
		return new ResponseEntity<>(personList, HttpStatus.OK);
	}
	@RequestMapping(value = "/person", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> addOrt(@RequestBody Person person){
		
		this.repository.saveAndFlush(person);
		GenericResponse response = new GenericResponse(HttpStatus.OK.value(), "Person hinzgefügt!");
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
