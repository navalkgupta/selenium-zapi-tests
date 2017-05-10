package testmodels;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StepExecutor {

	
	static final String STEPS_PROPERTIES_PATH =  "src/main//resources//StepKeywords.properties";	
	private String step=null, data=null, result=null;
	public static Method functions[];
	private static final Log log = LogFactory.getLog(StepExecutor.class);	
	
	FunctionLibrary functionLibrary;	
	
	public StepExecutor(String step, String data, String result) {
		
		this.step = step;
		this.data = data;
		this.result = result;
		this.functionLibrary = new FunctionLibrary();
	}
	
	public StepExecutor(String step, String data) {
		
		this.step = step;
		this.data = data;
		this.functionLibrary = new FunctionLibrary();
	}
	
	public String executeStep() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		String step_keyword = step.substring(0, step.indexOf(' '));
		functions=functionLibrary.getClass().getMethods();
		String actualResult = "Not as expected";
	    try {
	    	
	        for(int i=0;i<functions.length;i++) {
	        	
	            String function_name=functions[i].getName();
	            if(function_name.toLowerCase().contains(step_keyword.toLowerCase())) {
	            	
	            	Method method = functionLibrary.getClass().getMethod(function_name, String.class, String.class);
	            	actualResult =  (String) method.invoke(functionLibrary, data, result);
	            	log.info("Completed executing test step:"+step);
	            	break;
	            }
	        }
	    } catch(Exception ex) {
	    	
	        log.error(ex.getMessage());
	    }
	    
	    return actualResult;
	}	

}