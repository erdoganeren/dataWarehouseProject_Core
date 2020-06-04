package pacApp.pacController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import pacApp.pacKafka.MqRequest;
import pacApp.pacModel.Person;

@RestController
@Api(value = "TestController", description = "Operations pertaining to Test")
public class TestController {

	@Autowired
	private KafkaTemplate<String, MqRequest> kafkaTemplete;

	@GetMapping("/test")
	public String getTestMessage() {	
		this.kafkaTemplete.send("topic1", new MqRequest("testt",Person.class.getName() ,new Person()));
		return "TestMessage";
	}

}
