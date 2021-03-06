import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materials.Helpers
import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.MaterialRepositoryFactory
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

class MyTestListener {
	
	static Path reportDir
	static Path materialsFolder
	
	static {
		// for example, reportDir = C:/Users/username/katalon-workspace/ImageDiffByAShot/Reports/TS1/20180618_165141
		reportDir = Paths.get(RunConfiguration.getReportFolder())
		materialsFolder = Paths.get(RunConfiguration.getProjectDir()).resolve('Materials')
		// for example, materialsFolder == C:/Users/username/katalon-workspace/ImageDiffByAShot/Materials
		Helpers.ensureDirs(materialsFolder)
	}
	
	/**
	 * Executes before every test suite starts.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		def testSuiteId = testSuiteContext.getTestSuiteId()
		def testSuiteTimestamp = reportDir.getFileName().toString()    // e.g., '20180618_165141'
		MaterialRepository mr = MaterialRepositoryFactory.createInstance(materialsFolder)
		mr.putCurrentTestSuite(testSuiteId, testSuiteTimestamp)	
		GlobalVariable.MATERIAL_REPOSITORY = mr
		WebUI.comment(">>> testSuiteId is '${mr.getCurrentTestSuiteId()}', testSuiteTimestamp is '${mr.getCurrentTestSuiteId()}'")
		WebUI.comment(">>> Instance of MaterialRepository(${mr.getBaseDir().toString()})" +
			" is set to GlobalVariable.MATERIAL_REPOSITORY")
	}
		
		
	/**
	 * Executes before every test case starts.
	 * @param testCaseContext relate information of the executed test case.
	 */
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		if (GlobalVariable.MATERIAL_REPOSITORY == null) {
			MaterialRepository mr = MaterialRepositoryFactory.createInstance(materialsFolder)
			GlobalVariable.MATERIAL_REPOSITORY = mr
			WebUI.comment(">>> testSuiteId is '${mr.getCurrentTestSuiteId()}', testSuiteTimestamp is '${mr.getCurrentTestSuiteId()}'")
			WebUI.comment(">>> Instance of MaterialRepository(${mr.getBaseDir().toString()})" +
			" is set to GlobalVariable.MATERIAL_REPOSITORY")
		}
		GlobalVariable.CURRENT_TESTCASE_ID = testCaseContext.getTestCaseId()   //  e.g., 'Test Cases/TC1'
	}

}