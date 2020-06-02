package pacApp.pacController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import pacApp.pacKafka.MqRequest;

@RestController
@Api(value = "TestController", description = "Operations pertaining to Test")
public class TestController {

	@Autowired
	private KafkaTemplate<Object, Object> template;

	@GetMapping("/test")
	public String getTestMessage() {	
		MqRequest mqr =  new MqRequest();
		mqr.setMqMessage("halloTest");
		this.template.send("topic1", mqr);
		return "TestMessage";
	}

}
