package pacApp.pacKafka;

public class MqRequest{

	private static final long serialVersionUID = 1L;
	private String mqMessage;

	public MqRequest(String mqMessage) {
		this.mqMessage = mqMessage;
	}
	public String getMqMessage() {
		return mqMessage;
	}

	public void setMqMessage(String mqMessage) {
		this.mqMessage = mqMessage;
	}
}
