package pacApp.pacController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(value = "TestController", description = "Operations pertaining to Test")
public class TestController {

	@GetMapping("/test")
	public String getTestMessage() {
		return "TestMessage";
	}

}
