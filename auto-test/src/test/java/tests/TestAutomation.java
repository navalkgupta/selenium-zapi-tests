package tests;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import core.JIRA_Reporter;
import core.JIRA_Reporter.Status;
import testmodels.StepExecutor;

public class TestAutomation{
	
	static JIRA_Reporter jira_reporter;
	
	static String testCycleId=null;
	static String regTestCaseIssueId=null;
	static String regTestCaseExecId=null;
	static JsonArray executionArray=null;
	static JsonArray stepArray=null;
	
	private static final Log log = LogFactory.getLog(TestAutomation.class);
	
	@BeforeTest
	public static void beforeTest() throws InterruptedException{
		// New Test Cycle Creation for Automated tests
		jira_reporter = new JIRA_Reporter();
		testCycleId = jira_reporter.cloneTestCycle();
		log.info("New test cycle created with id : " + testCycleId);
	}
	
	@Test
	public static void sampleTest() throws Exception {
		
		log.info("Starting Test Wizard...");
		
		// Getting Test Case & Test Execution Id for newly created Test Cycle		
		JsonArray executionArray = jira_reporter.getListOfTestCases(testCycleId);
		int numTestCases = 0; //no. of Test Cases
		numTestCases = executionArray.size();
		log.info("No. of test cases in test cycle: "+String.valueOf(numTestCases));
		JsonObject testCase = null; //Test Case object		
		
		for (int caseloop=0; caseloop<numTestCases; caseloop++ ) {
			
			Boolean caseFlag = false; //To update Final status of current Test Case
			testCase = executionArray.get(caseloop).getAsJsonObject();
			regTestCaseExecId = testCase.get("id").getAsString();
			regTestCaseIssueId = testCase.get("issueId").getAsString();
			log.info("Regression test case issue id is : " + regTestCaseIssueId);
			log.info("Regression Execution id is : " + regTestCaseExecId);
			
			//Updating Execution to WIP with status=3			
			log.info("Start execution of test case in JIRA ...");
			JsonObject executeResponse = jira_reporter.updateTestCaseStatus(regTestCaseExecId, Status.WIP);
			log.info("Status of Test Case no. " + (caseloop+1) + " is : " + executeResponse.get("executionStatus").getAsString());
				  
			//Manually click "E" to create execution of Test Steps
			jira_reporter.startExecution(regTestCaseExecId);
			stepArray = jira_reporter.getListOfTestSteps(regTestCaseExecId);
			int numTestSteps = stepArray.size();
		  		  
			Thread.sleep(3000);
			String stepId = null;
			String step = null, data = "test data", result = "test result";
		  
			//Step Id remains constant
			//Getting Test Step Details for fetching Test Data & Updating Test Step Result
			boolean stepFlag = false;
			for(int steploop = 0; steploop < numTestSteps; steploop++) {
				
				if (stepFlag){
					caseFlag = true;
					break;
				}
			  
				//Fetching Test Step Id wrt Test Case
				stepId = jira_reporter.getTestStepId(regTestCaseIssueId, steploop);
		  
				//Fetching Test Step Execution Details - Step, Data & Result
				JsonObject testStepObj = jira_reporter.getTestStepDetails(regTestCaseIssueId, stepId);
		  
				step = testStepObj.get("step").getAsString();
				data = testStepObj.get("data").getAsString();
				result = testStepObj.get("result").getAsString();
		  
				//Using Test Data
				StepExecutor stepExecutor = new StepExecutor(step, data, result);
				String actualResult =  stepExecutor.executeStep();
				
				
				if (! actualResult.equals(result)){
					
					stepFlag = true;
				}
				
				if (stepFlag) {
					
					jira_reporter.updateTestStepResult(stepArray, steploop, Status.FAIL);
				}
				else {
					
					jira_reporter.updateTestStepResult(stepArray, steploop, Status.PASS);
				}
				
				log.info("Test Step no. : " + (steploop+1) + " is Done");
			  
			} // Test Step For Loop ends here
				  
			if (caseFlag) {
				  //Updating Test Case Status to Fail with status=2
				  jira_reporter.updateTestCaseStatus(regTestCaseExecId, Status.FAIL);
			}
				  
			else {
				  //Updating Test Case Status to Pass with status=1
				jira_reporter.updateTestCaseStatus(regTestCaseExecId, Status.PASS);
			}					  
				  
			log.info("Test Case no. : " + (caseloop+1) + " is Done");  
		
		} // Test Case For Loop ends here
	}
	
	@AfterTest
	public static void afterTest() throws InterruptedException{
		jira_reporter.deleteTestCycle(testCycleId);
	}
	
}
