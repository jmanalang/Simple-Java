package util.jrtl;

public class CallResult {
	
	private boolean _error;
	private String _problem;
	private String _fix;
	private DynamicObject _result;

	private static CallResult VOID_SINGLETON;
	
	public static CallResult newErrorCallResult(String p, String f) {
		return new CallResult(true, p, f, null);
	}
	
	public static CallResult VoidCallResult() {
		if (VOID_SINGLETON == null) {
			VOID_SINGLETON = new CallResult(false, "", "", null);
		}
		return VOID_SINGLETON;
	}
	
	public static CallResult newReturnValueCallResult(Object result) {
		return new CallResult(false, "", "", new DynamicObject(result));
	}
	
	private CallResult(boolean error, String problem, String fix, DynamicObject result) {
		_error = error;
		_problem = problem;
		_fix = fix;
		_result = result;
	}
	
	public boolean isError() {
		return _error;
	}

	public String getProblem() {
		return _problem;
	}

	public String getFix() {
		return _fix;
	}
	
	public DynamicObject getResult() {
		return _result;
	}
}
