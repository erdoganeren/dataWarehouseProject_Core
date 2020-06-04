package pacApp.pacKafka;

public class MqRequest{

	public MqRequest() {
	}

	private static final long serialVersionUID = 1L;
	private String sqlAction;
	private String className;
	private Object obj; 

	public MqRequest(String sqlAction,String className, Object obj) {
		this.sqlAction = sqlAction;
		this.className = className;
		this.obj = obj;
	}
	public String getSqlAction() {
		return sqlAction;
	}
	public void setSqlAction(String sqlAction) {
		this.sqlAction = sqlAction;
	}
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
}
