package core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import utils.ReadProperties;
import utils.ZAPI;

public class JIRA_Reporter {

	private static String projectId;
	private static String versionId;
	private static String cycleName;
	private static String cycleId;
	private static String jiraURL;
	private static String zapiURL;
	
	static final String JIRA_PROPERTIES_PATH = "src//main//resources//Jira.properties";
	private static final Log log = LogFactory.getLog(JIRA_Reporter.class);
	JsonParser parser = new JsonParser();

	public enum Status {
		PASS(1), FAIL(2), WIP(3), BLOCKED(4);
		private final int value;

		private Status(final int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public JIRA_Reporter() {
		getJIRAProperties();
		zapiURL = jiraURL+"/rest/zapi/latest";
	}

	public void getJIRAProperties() {
		Properties jiraProperties = new ReadProperties().loadProperties(JIRA_PROPERTIES_PATH);
		projectId = jiraProperties.getProperty("JIRA_ProjectID");
		versionId = jiraProperties.getProperty("JIRA_VersionID");
		cycleName = jiraProperties.getProperty("JIRA_CycleName");
		cycleId = jiraProperties.getProperty("JIRA_CycleID");
		jiraURL = jiraProperties.getProperty("JIRA_URL");
		
		log.info("JIRA Test Cycle: " + cycleName);
	}

	public String cloneTestCycle() throws InterruptedException {
		JsonObject testCycle = new JsonObject();
		testCycle.addProperty("clonedCycleId", cycleId);
		testCycle.addProperty("name", "NG AutomatedTest" + new Date());
		testCycle.addProperty("build", "");
		testCycle.addProperty("environment", "");
		testCycle.addProperty("description", "NG AutomatedTest" + new Date().toString());
		SimpleDateFormat dateformatter = new SimpleDateFormat("YYYY-MM-dd");
		testCycle.addProperty("startDate", dateformatter.format(new Date()));
		testCycle.addProperty("endDate", dateformatter.format(new Date()));
		testCycle.addProperty("projectId", projectId);
		testCycle.addProperty("versionId", versionId);

		String testCycleResponse = ZAPI.postOrPut(zapiURL + "/cycle", testCycle.toString(), "POST");
		JsonObject responseObject = parser.parse(testCycleResponse).getAsJsonObject();
		return responseObject.get("id").getAsString();
	}

	public JsonArray getListOfTestCases(String cycleId) throws InterruptedException {
		String testCaseResponse = ZAPI.get(zapiURL + "/execution?cycleId=" + cycleId);
		JsonObject testCaseResponseObj = parser.parse(testCaseResponse).getAsJsonObject();
		return testCaseResponseObj.get("executions").getAsJsonArray();
	}

	public JsonArray getListOfTestSteps(String testCaseExecId) throws InterruptedException {
		String testStepResponse = ZAPI.get(zapiURL + "/stepResult?executionId=" + testCaseExecId);
		return parser.parse(testStepResponse).getAsJsonArray();
	}

	public void startExecution(String testCaseExecId) throws InterruptedException {
		ZAPI.get(zapiURL + "/execution/" + testCaseExecId + "?expand=checksteps");
	}

	public String getTestStepId(String testCaseIssueId, int stepNumber) throws InterruptedException {
		String testStepWithId = ZAPI.get(zapiURL + "/teststep/" + testCaseIssueId);
		JsonArray testStepIdArray = parser.parse(testStepWithId).getAsJsonArray();
		JsonObject testStepIdObj = testStepIdArray.get(stepNumber).getAsJsonObject();
		return testStepIdObj.get("id").getAsString();
	}

	public JsonObject getTestStepDetails(String testCaseIssueId, String testStepId) throws InterruptedException {
		String testStepExeDetails = ZAPI.get(zapiURL + "/teststep/" + testCaseIssueId + "/" + testStepId);
		return parser.parse(testStepExeDetails).getAsJsonObject();
	}

	public JsonObject updateTestCaseStatus(final String testCaseExecId, final Status status)
			throws NumberFormatException, InterruptedException {
		JsonObject object = new JsonObject();
		object.addProperty("status", String.valueOf(status.getValue()));
		JsonParser parser = new JsonParser();
		String testExecuteResponse = ZAPI.postOrPut(
				zapiURL + "/execution/" + Integer.parseInt(testCaseExecId) + "/execute", object.toString(), "PUT");
		return parser.parse(testExecuteResponse).getAsJsonObject();
	}

	public void updateTestStepResult(final String testCaseIssueId, final String testCaseExecId, final String testStepId,
			final Status status) throws InterruptedException {
		JsonObject statusUpdate = new JsonObject();
		statusUpdate.addProperty("issueId", testCaseIssueId);
		statusUpdate.addProperty("executionId", testCaseExecId);
		statusUpdate.addProperty("status", String.valueOf(status.getValue()));
		log.info("Updating test step result as: " + statusUpdate.toString());
		ZAPI.postOrPut(zapiURL + "/stepResult/" + testStepId, statusUpdate.toString(), "PUT");
	}

	public void updateTestStepResult(JsonArray stepArray, int i, final Status status) throws InterruptedException {

		JsonObject testStep = stepArray.get(i).getAsJsonObject();
		String testStepExecutionId = testStep.get("id").getAsString();

		JsonObject statusUpdate = new JsonObject();
		statusUpdate.addProperty("status", String.valueOf(status.getValue()));

		log.info("Updating test step result as: " + statusUpdate.toString());
		ZAPI.postOrPut(zapiURL + "/stepResult/" + testStepExecutionId, statusUpdate.toString(), "PUT");

	}

	public void deleteTestCycle(String cycleId) throws InterruptedException {

		ZAPI.delete(zapiURL + "/cycle/" + cycleId);
	}

}
