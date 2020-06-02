package pacApp.pacKafka;

import java.io.Serializable;

public class MqRequest extends JavaSerializer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mqMessage;

	public String getMqMessage() {
		return mqMessage;
	}

	public void setMqMessage(String mqMessage) {
		this.mqMessage = mqMessage;
	}
}
