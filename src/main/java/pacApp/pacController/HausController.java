package pacApp.pacController;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pacApp.pacData.HausRepository;
import pacApp.pacKafka.MqRequest;
import pacApp.pacKafka.SqlActionEnum;
import pacApp.pacModel.Haus;
import pacApp.pacModel.pacResponse.GenericResponse;

@RestController
public class HausController {
	
	private final HausRepository repository;
	private final String HAUS_CLASS_NAME = Haus.class.getName();
	
	@Autowired
	private KafkaTemplate<String, MqRequest> kafkaTemplete;
	
	public HausController(HausRepository repository) {
        this.repository = repository;
    }
	
	@CrossOrigin
    @GetMapping("/hauslist")
	public ResponseEntity<List<Haus>> getAllOrt() {
					
		List<Haus> hausList = this.repository.findAll();
		return new ResponseEntity<>(hausList, HttpStatus.OK);
	}	
	@RequestMapping(value = "/haus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> addOrt(@RequestBody Haus haus){		
		List<Haus> hausList = this.repository.findAllById(haus.getId()); // get first object
		if (!hausList.isEmpty()){
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Haus vorhanden");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
		this.repository.saveAndFlush(haus);
		this.kafkaTemplete.send("topic1", new MqRequest(SqlActionEnum.SQL_ACTION_ADD, HAUS_CLASS_NAME, haus));
		GenericResponse response = new GenericResponse(HttpStatus.OK.value(), "Haus hinzgefügt!");
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value={"/hausdelete/{id}"}, method=RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> deleteHaus(@PathVariable(value="id") String id){
		Optional<Haus> optHaus = this.repository.findById(Long.parseLong(id));
		if (optHaus.isEmpty()){
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Ort nicht gefunden");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
		this.repository.delete(optHaus.get());
		this.kafkaTemplete.send("topic1", new MqRequest(SqlActionEnum.SQL_ACTION_DELETE, HAUS_CLASS_NAME, optHaus.get()));
		GenericResponse response = new GenericResponse(HttpStatus.OK.value(), "Haus wurde gelöscht!");
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
