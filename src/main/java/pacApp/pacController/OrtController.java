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

import pacApp.pacData.OrtRepository;
import pacApp.pacKafka.MqRequest;
import pacApp.pacKafka.SqlActionEnum;
import pacApp.pacModel.Ort;
import pacApp.pacModel.Person;
import pacApp.pacModel.User;
import pacApp.pacModel.pacResponse.GenericResponse;

@RestController
public class OrtController {
	
	private final OrtRepository repository;
	private final String ORT_CLASS_NAME = Ort.class.getName();
	@Autowired
	private KafkaTemplate<String, MqRequest> kafkaTemplete;
	
	public OrtController(OrtRepository repository) {
        this.repository = repository;
    }

	@CrossOrigin
    @GetMapping("/ortlist")
	public ResponseEntity<List<Ort>> getAllOrt() {					
		List<Ort> orteList = this.repository.findAll();
		return new ResponseEntity<>(orteList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/ort", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> addOrt(@RequestBody Ort ort){
		this.repository.saveAndFlush(ort);		
		this.kafkaTemplete.send("topic1", new MqRequest(SqlActionEnum.SQL_ACTION_ADD, ORT_CLASS_NAME, ort));
		GenericResponse response = new GenericResponse(HttpStatus.OK.value(), "Ort hinzgefügt!");
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/ortupdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> updateOrt(@RequestBody Ort ort){		
		if (ort == null){
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Ort nicht gefunde");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
		Optional<Ort> optOrt = this.repository.findOneByPlz(ort.getPlz());
		
		if (!optOrt.isPresent()){
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Ort nicht gefunden");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
		Ort ortTmp = optOrt.get();
		ortTmp.setPlz(ort.getPlz());
		ortTmp.setOrtsname(ort.getOrtsname());
		this.kafkaTemplete.send("topic1", new MqRequest(SqlActionEnum.SQL_ACTION_UPDATE, ORT_CLASS_NAME, ortTmp));
		this.repository.saveAndFlush(ortTmp);
		GenericResponse response = new GenericResponse(HttpStatus.OK.value(), "Ort bearbeitet!");
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value={"/ortdelete/{id}"}, method=RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> deleteOrt(@PathVariable(value="id") String id){		
		Ort optOrt = this.repository.findById(Long.parseLong(id));
		if (optOrt == null){
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Ort nicht gefunden");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
		Ort ortTmp = optOrt;
		this.repository.delete(ortTmp);
		this.kafkaTemplete.send("topic1", new MqRequest(SqlActionEnum.SQL_ACTION_DELETE, ORT_CLASS_NAME, ortTmp));
		GenericResponse response = new GenericResponse(HttpStatus.OK.value(), "Ort wurde gelöscht!");
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
