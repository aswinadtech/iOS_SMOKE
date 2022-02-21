package com.twc.ios.app.testcases;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.twc.ios.app.charlesfunctions.CharlesProxy;
import com.twc.ios.app.functions.Functions;
import com.twc.ios.app.general.ParseForVideoOrderedList;
import com.twc.ios.app.general.TestBase;
import com.twc.ios.app.general.TwcIosBaseTest;
import com.twc.ios.app.general.Utils;
import com.twc.ios.app.pages.AddressScreen;
import com.twc.ios.app.pages.BreakingNewsCardScreen;
import com.twc.ios.app.pages.DailyNavTab;
import com.twc.ios.app.pages.HomeNavTab;
import com.twc.ios.app.pages.HourlyNavTab;
import com.twc.ios.app.pages.PlanningCardScreen;
import com.twc.ios.app.pages.RadarNavTab;
import com.twc.ios.app.pages.SeasonalHubCardScreen;
import com.twc.ios.app.pages.SettingsScreen;
import com.twc.ios.app.pages.VideoNavTab;

import java.io.File;

import org.testng.annotations.Listeners;

import org.testng.annotations.BeforeClass;

//import ru.yandex.qatools.allure.annotations.Title;
import io.qameta.allure.Description;

@Listeners(value = com.twc.ios.app.general.MyTestListenerAdapter.class)
public class RegressionTest extends TwcIosBaseTest {

	private static final String CONFIG_FILE_PATH = "charles_common.config";
	private static final String BN_SEVERE1_CONFIG_FILE_PATH = "BNSevere1charles_common.config";
	private static final String BN_SEVERE2_CONFIG_FILE_PATH = "BNSevere2charles_common.config";
	private static final String CRITEO_CONFIG_FILE_PATH = "Criteocharles_common.config";

	// public static CharlesProxy proxy;
	public File configFile;
	
	HourlyNavTab hrTab;
	DailyNavTab dTab;
	HomeNavTab hmTab;
	RadarNavTab rTab;
	VideoNavTab vTab;
	AddressScreen addrScreen;
	PlanningCardScreen pScreen;
	SeasonalHubCardScreen sScreen;
	BreakingNewsCardScreen bnScreen;
	SettingsScreen stScreen;
	

	@BeforeClass(alwaysRun = true)
	@Description("BeforeClass")
	public void beforeClass() {
		System.out.println("****** Regression Test Started");
		logStep("****** Regression Test Started");
		//this.configFile = this.charlesGeneralConfigFile(CONFIG_FILE_PATH);
		//proxy = new CharlesProxy("localhost", 8111, CONFIG_FILE_PATH);
		this.configFile = this.rewriteRuleToOverrideGeoIpCountry(CRITEO_CONFIG_FILE_PATH, "US");
		proxy = new CharlesProxy("localhost", 8111, CRITEO_CONFIG_FILE_PATH);
		proxy.startCharlesProxyWithUI();
		proxy.disableRewriting();
		proxy.stopRecording();
		proxy.disableMapLocal();
	}

	@AfterClass(alwaysRun = true)
	@Description("AfterClass")
	public void afterClass() throws Exception {
		System.out.println("****** After Class Started");
		logStep("****** After Class Started");
		if (this.configFile != null) {
			this.configFile.delete();
		}
		stScreen.getAppVersion();
		Functions.archive_folder("Charles");
		proxy.disableRewriting();
		proxy.quitCharlesProxy();
		try {
			Ad.terminateApp("com.weather.TWC");;
			System.out.println("App closed successfully");
			logStep("App closed successfully");
		} catch (Exception e) {
			System.out.println("An exception while closeApp() executed");
			logStep("An exception while closeApp() executed");
		}

		if (Ad != null) {
			try {
				Ad.quit();
			} catch (Exception ex) {
				// Session crashed/died probably so no big deal, since
				// this exception was thrown when trying to close session.
				// Also, avoids failures in before/after methods for TestNG.
				System.out.println(
						"NoSuchSessionException was thrown while attempting to close session. Ignoring this error.");
				logStep("NoSuchSessionException was thrown while attempting to close session. Ignoring this error.");
			}
			System.out.println("Closing appium session.. Done");
			logStep("Closing appium session.. Done");
		}

		System.out.println("****** Regression Test Ended");
		logStep("****** Regression Test Ended");
	}

	@Test(priority = 0)
	@Description("Updating Device Proxy Details and Launch the App")
	public void beforeTest() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Updating Device Proxy Details and Launch the App test case Started");
		logStep("****** Updating Device Proxy Details and Launch the App test case Started");
		// Preconditions
		Functions.capabilities();
		Functions.Appium_Autostart();
		Utils.getCurrentMacIPAddressAndSetiPhoneProxy(true, true);
		// Enable rewriting on Charles install/launch TWC to rewrite geoipcountry to US
		proxy.enableRewriting();
		proxy.startRecording();
		proxy.clearCharlesSession();
		Functions.archive_folder("Charles");
		Functions.launchtheApp("true");
		System.out.println("App launched ");
		logStep("App launched ");
		hrTab = new HourlyNavTab(Ad);
		dTab = new DailyNavTab(Ad);
		hmTab = new HomeNavTab(Ad);
		rTab = new RadarNavTab(Ad);
		vTab = new VideoNavTab(Ad);
		addrScreen = new AddressScreen(Ad);
		pScreen = new PlanningCardScreen(Ad);
		sScreen = new SeasonalHubCardScreen(Ad);
		bnScreen = new BreakingNewsCardScreen(Ad) ;
		stScreen = new SettingsScreen(Ad);
	}

	@Test(priority = 50, enabled = true)
	@Description("Kill and Launch the app for aax calls verification")
	public void kill_and_Launch_app_for_aax_calls() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Kill and Launch the app for aax calls");
		logStep("Kill and Launch the app for aax calls");
		TestBase.waitForMilliSeconds(5000);
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		addrScreen.enternewAddress(false, "Atlanta, Georgia");
		Functions.archive_folder("Charles");
		TestBase.waitForMilliSeconds(5000);
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();
		Utils.get_v3_wx_forecast_daily_15day_data();
	}

	
	@Test(priority = 51, enabled = true)
	@Description("Verify amazon aax homescreen Adhesive preload ad call")
	public void Verify_amazon_aax_preload_homescreen_adhesive_adcall() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"=========================== amazon aax homescreen adhesive preload ad call ====================");

		System.out.println("****** amazon aax homescreen adhesive preload ad call validation Started");
		logStep("****** amazon aax homescreen adhesive preload ad call validation Started");

		Utils.verifyAAX_SlotId("Smoke", "Pulltorefresh");

	}
	 
	@Test(priority = 52, enabled = true)
	@Description("Verify amazon aax Feed1 preload ad call")
	public void Verify_amazon_aax_preload_feed1_adcall() throws Exception {
		System.out.println("==============================================");
		System.out.println("=========================== amazon aax feed1 preload ad call ====================");

		System.out.println("****** amazon aax feed1 preload ad call validation Started");
		logStep("****** amazon aax feed1 preload ad call validation Started");

		Utils.verifyAAX_SlotId("Smoke", "Feed1");

	}

	@Test(priority = 53, enabled = true)
	@Description("Verify amazon aax Feed2 preload ad call")
	public void Verify_amazon_aax_preload_feed2_adcall() throws Exception {
		System.out.println("==============================================");
		System.out.println("=========================== amazon aax feed2 preload ad call ====================");

		System.out.println("****** amazon aax feed2 preload ad call validation Started");
		logStep("****** amazon aax feed2 preload ad call validation Started");

		Utils.verifyAAX_SlotId("Smoke", "Feed2");

	}

	@Test(priority = 54, enabled = true)
	@Description("Verify amazon aax Feed3 preload ad call")
	public void Verify_amazon_aax_preload_feed3_adcall() throws Exception {
		System.out.println("==============================================");
		System.out.println("=========================== amazon aax feed3 preload ad call ====================");

		System.out.println("****** amazon aax feed3 preload ad call validation Started");
		logStep("****** amazon aax feed3 preload ad call validation Started");

		Utils.verifyAAX_SlotId("Smoke", "Feed3");

	}

	@Test(priority = 55, enabled = true)
	@Description("Verify amazon aax Feed4 preload ad call")
	public void Verify_amazon_aax_preload_feed4_adcall() throws Exception {
		System.out.println("==============================================");
		System.out.println("=========================== amazon aax feed4 preload ad call ====================");

		System.out.println("****** amazon aax feed4 preload ad call validation Started");
		logStep("****** amazon aax feed4 preload ad call validation Started");

		Utils.verifyAAX_SlotId("Smoke", "Feed4");

	}

	@Test(priority = 57, enabled = true)
	@Description("Verify amazon aax PreRollVideo preload ad call")
	public void Verify_amazon_aax_preload_PreRollVideo_adcall() throws Exception {
		System.out.println("==============================================");
		System.out.println("=========================== amazon PreRollVideo preload ad call ====================");

		System.out.println("****** amazon aax PreRollVideo preload ad call validation Started");
		logStep("****** amazon aax PreRollVideo preload ad call validation Started");

		Utils.verifyAAX_SlotId("Smoke", "PreRollVideo");

	}

	@Test(priority = 58, enabled = true)
	@Description("Verify amazon aax map details preload ad call")
	public void Verify_amazon_aax_preload_map_details_adcall() throws Exception {
		System.out.println("==============================================");
		System.out.println("=========================== amazon aax map details preload ad call ====================");

		System.out.println("****** amazon aax Map details preload ad call validation Started");
		logStep("****** amazon aax Map details preload ad call validation Started");

		Utils.verifyAAX_SlotId("Smoke", "Map");

	}

	@Test(priority = 59, enabled = true)
	@Description("Verify amazon aax Daily Details preload ad call")
	public void Verify_amazon_aax_preload_Daily_details_adcall() throws Exception {
		System.out.println("==============================================");
		System.out.println("=========================== amazon aax Daily Details preload ad call ====================");

		System.out.println("****** amazon aax Daily Details preload ad call validation Started");
		logStep("****** amazon aax Daily Details preload ad call validation Started");

		Utils.verifyAAX_SlotId("Smoke", "Daily(10day)");

	}

	@Test(priority = 60, enabled = true)
	@Description("Verify amazon aax Hourly Details preload ad call")
	public void Verify_amazon_aax_preload_Hourly_details_adcall() throws Exception {
		System.out.println("==============================================");
		System.out
				.println("=========================== amazon aax Hourly Details preload ad call ====================");

		System.out.println("****** amazon aax Hourly Details preload ad call validation Started");
		logStep("****** amazon aax Hourly Details preload ad call validation Started");

		Utils.verifyAAX_SlotId("Smoke", "Hourly");
	}
	
	@Test(priority = 65, enabled = true)
	@Description("Validating 'adsdk' parameter of Amazon aax call ")
	public void Validate_Amazon_SDK_adsdk_parameter() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Amazon SDK version i.e. 'adsdk' parameter of Amazon aax call");
		logStep("****** Validating Amazon SDK version i.e. 'adsdk' parameter of Amazon aax call");
		Utils.validate_Amazon_aax_call_parameter("Smoke", "Amazon", "adsdk",
				properties.getProperty("AmazonSDKVersion"));

	}

	@Test(priority = 70, enabled = true)
	@Description("Validating Google Mobile Ads SDK version of gampad call ")
	public void Validate_GMA_SDK_version() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Google Mobile Ads SDK Version i.e. 'js' parameter of gampad call");
		logStep("Validating Google Mobile Ads SDK Version i.e. 'js' parameter of gampad call");

		Utils.validate_Noncustom_param_val_of_gampad("Smoke", "Marquee", "js", properties.getProperty("GMASDKVersion"));

	}
	
	@Test(priority = 75, enabled = true)
	@Description("Verify Criteo SDK inapp v2 call")
	public void Verify_Criteo_SDK_inapp_v2_Call() throws Exception {
		System.out.println("==============================================");
		System.out.println("=========================== Criteo SDK inapp/v2 call ====================");

		System.out.println("****** Criteo SDK inapp/v2 call validation Started");
		logStep("****** Criteo SDK inapp/v2 call validation Started");

		Utils.verifyCriteo_inapp_v2_Call("Smoke", "Criteo");

	}

	@Test(priority = 76, enabled = true)
	@Description("Verify Criteo SDK config app call")
	public void Verify_Criteo_SDK_config_app_Call() throws Exception {
		System.out.println("==============================================");
		System.out.println("=========================== Criteo SDK config/app call ====================");

		System.out.println("****** Criteo SDK config/app call validation Started");
		logStep("****** Criteo SDK config/app call validation Started");

		Utils.verifyCriteo_config_app_Call("Smoke", "Criteo");

	}

	@Test(priority = 77, enabled = true)
	@Description("Validating 'cpId' parameter of Criteo SDK config app call ")
	public void Validate_Criteo_SDK_config_app_Call_cpId_parameter() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating 'cpId' parameter of Criteo SDK config app call");
		logStep("****** Validating 'cpId' parameter of Criteo SDK config app call");
		Utils.validate_Criteo_SDK_config_app_call_parameter("Smoke", "Criteo", "cpId",
				properties.getProperty("CriteoCpId"));

	}

	@Test(priority = 78, enabled = true)
	@Description("Validating 'bundleId' parameter of Criteo SDK config app call ")
	public void Validate_Criteo_SDK_config_app_Call_bundleId_parameter() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating 'bundleId' parameter of Criteo SDK config app call");
		logStep("****** Validating 'bundleId' parameter of Criteo SDK config app call");
		Utils.validate_Criteo_SDK_config_app_call_parameter("Smoke", "Criteo", "bundleId",
				properties.getProperty("CriteoBundleId"));

	}

	@Test(priority = 79, enabled = true)
	@Description("Validating 'sdkVersion' parameter of Criteo SDK config app call ")
	public void Validate_Criteo_SDK_config_app_Call_sdkVersion_parameter() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating 'sdkVersion' parameter of Criteo SDK config app call");
		logStep("****** Validating 'sdkVersion' parameter of Criteo SDK config app call");
		Utils.validate_Criteo_SDK_config_app_call_parameter("Smoke", "Criteo", "sdkVersion",
				properties.getProperty("CriteoSDKVersion"));
	}

	/**
	 * This method validates Criteo Bidder API (invapp v2) call response code
	 */
	@Test(priority = 81, enabled = true)
	@Description("Validating Criteo Bidder API (invapp v2) call response code")
	public void Validate_Criteo_SDK_Bidder_API_Call_Response_Code() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Criteo Bidder API (invapp v2) Call Response Code");
		logStep("****** Validating Criteo Bidder API (invapp v2) Call Response Code");
		Utils.verifyCriteo_inapp_v2_Call_ReponseStatusCode("Smoke", "Criteo", "status",
				properties.getProperty("CriteoResponseCode"));
	}

	/**
	 * This method validates Initialization API (config app) call response code
	 */
	@Test(priority = 82, enabled = true)
	@Description("Validating Criteo Initialization API (config app) call response code")
	public void Validate_Criteo_SDK_Initialization_API_Call_Response_Code() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Criteo Initialization API (config app) Call Response Code");
		logStep("****** Validating Criteo Initialization API (config app) Call Response Code");
		Utils.verifyCriteo_config_app_Call_ReponseStatusCode("Smoke", "Criteo", "status",
				properties.getProperty("CriteoResponseCode"));
	}

	/**
	 * This method validates Initialization API (config app) call response parameter
	 * 'csmEnabled'
	 */
	@Test(priority = 83, enabled = true)
	@Description("Validating Criteo Initialization API (config app) call response parameter 'csmEnabled' value")
	public void Validate_Criteo_SDK_Initialization_API_Call_Response_Parameter_csmEnabled() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"****** Validating csmEnabled parameter value in Criteo Initialization API (config app) Call Response");
		logStep("****** Validating csmEnabled parameter value in Criteo Initialization API (config app) Call Response");
		Utils.validate_Criteo_SDK_config_app_call_response_parameter("Smoke", "Criteo", "csmEnabled",
				properties.getProperty("csmEnabled"));
	}

	/**
	 * This method validates Initialization API (config app) call response parameter
	 * 'liveBiddingEnabled'
	 */
	@Test(priority = 84, enabled = true)
	@Description("Validating Criteo Initialization API (config app) call response parameter 'liveBiddingEnabled' value")
	public void Validate_Criteo_SDK_Initialization_API_Call_Response_Parameter_liveBiddingEnabled() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"****** Validating liveBiddingEnabled parameter value in Criteo Initialization API (config app) Call Response");
		logStep("****** Validating liveBiddingEnabled parameter value in Criteo Initialization API (config app) Call Response");
		Utils.validate_Criteo_SDK_config_app_call_response_parameter("Smoke", "Criteo", "liveBiddingEnabled",
				properties.getProperty("liveBiddingEnabled"));
	}

	/*
	 * This method validates Initialization API (config app) call response parameter
	 * 'liveBiddingTimeBudgetInMillis'
	 * 
	 * @Test(priority = 85, enabled = true)
	 * 
	 * @Description("Validating Criteo Initialization API (config app) call response parameter 'liveBiddingTimeBudgetInMillis' value"
	 * ) public void
	 * Validate_Criteo_SDK_Initialization_API_Call_Response_Parameter_liveBiddingTimeBudgetInMillis
	 * () throws Exception {
	 * System.out.println("==============================================");
	 * System.out.println(
	 * "****** Validating liveBiddingTimeBudgetInMillis parameter value in Criteo Initialization API (config app) Call Response"
	 * );
	 * logStep("****** Validating liveBiddingTimeBudgetInMillis parameter value in Criteo Initialization API (config app) Call Response"
	 * ); Utils.validate_Criteo_SDK_config_app_call_response_parameter("Smoke",
	 * "Criteo", "liveBiddingTimeBudgetInMillis",
	 * properties.getProperty("liveBiddingTimeBudgetInMillis")); }
	 */

	/**
	 * This method validates Initialization API (config app) call response parameter
	 * 'prefetchOnInitEnabled'
	 */
	@Test(priority = 86, enabled = true)
	@Description("Validating Criteo Initialization API (config app) call response parameter 'prefetchOnInitEnabled' value")
	public void Validate_Criteo_SDK_Initialization_API_Call_Response_Parameter_prefetchOnInitEnabled()
			throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"****** Validating prefetchOnInitEnabled parameter value in Criteo Initialization API (config app) Call Response");
		logStep("****** Validating prefetchOnInitEnabled parameter value in Criteo Initialization API (config app) Call Response");
		Utils.validate_Criteo_SDK_config_app_call_response_parameter("Smoke", "Criteo", "prefetchOnInitEnabled",
				properties.getProperty("prefetchOnInitEnabled"));
	}

	/**
	 * This method validates Initialization API (config app) call response parameter
	 * 'remoteLogLevel'
	 */
	@Test(priority = 87, enabled = true)
	@Description("Validating Criteo Initialization API (config app) call response parameter 'remoteLogLevel' value")
	public void Validate_Criteo_SDK_Initialization_API_Call_Response_Parameter_remoteLogLevel() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"****** Validating remoteLogLevel parameter value in Criteo Initialization API (config app) Call Response");
		logStep("****** Validating remoteLogLevel parameter value in Criteo Initialization API (config app) Call Response");
		Utils.validate_Criteo_SDK_config_app_call_response_parameter("Smoke", "Criteo", "remoteLogLevel",
				properties.getProperty("remoteLogLevel"));
	}

	/**
	 * Below script validates amazon UUID's of amazon calls of feed ads from 5th
	 * feed onwards
	 */

	/*
	 * @Test(priority = 227, enabled = true)
	 * 
	 * @Description("Verify amazon aax values of feed ads") public void
	 * Verify_amazon_aax_feed_ads() throws Exception {
	 * System.out.println("==============================================");
	 * System.out.
	 * println("=========================== amazon slot id's of new feed ad cards ===================="
	 * );
	 * 
	 * System.out.
	 * println("****** amazon aax slot ids of new feed ads validation Started"); //
	 * Functions.verifyAAX_SlotId("Smoke", "News(details)");
	 * Utils.verifyFeedAds_AAX_SlotIds("Smoke", "CleanLaunch"); }
	 */

	@Test(priority = 250, enabled = true)
	@Description("Verify preroll video iu")
	public void Verify_video_adcall_iu() throws Exception {
		System.out.println("==============================================");
		System.out.println("=========================== preroll video ad call iu ====================");

		System.out.println("****** preroll video ad call iu validation Started");
		logStep("****** preroll video ad call iu validation Started");
		proxy.clearCharlesSession();
		hmTab.clickonHomeTab();
		proxy.clearCharlesSession();
		// navigate to Video tab
		vTab.navigateToVideoTab(true, proxy);
		TestBase.waitForMilliSeconds(10000);
		Functions.archive_folder("Charles");
		TestBase.waitForMilliSeconds(5000);
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();
		Utils.get_iu_value_of_Feedcall("Smoke", "PreRollVideo");
	}

	@Test(priority = 251, enabled = true)
	@Description("Validating Google Interactive Media Ads SDK version of Preroll video call ")
	public void Validate_IMA_SDK_version() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"****** Validating Google Interactive Media Ads SDK version i.e. 'js' parameter of Preroll video call");
		logStep("Validating Google Interactive Media Ads SDK version i.e. 'js' parameter of Preroll video call");

		Utils.validate_Noncustom_param_val_of_gampad("Smoke", "PreRollVideo", "js",
				properties.getProperty("IMASDKVersion"));

	}

	@Test(priority = 325, enabled = true)
	@Description("Validating Privacy Card and its options existance")
	public void validate_Privacy_card_and_Options() throws Exception {
		System.out.println("==============================================");
		System.out.println("Started Validating Privacy Card");
		System.out.println("****** Navigating to Privacy Card ");
		logStep("Navigating to Privacy Card ");
		proxy.clearCharlesSession();
		// Utils.navigateTofeedCard("privacy");
		System.out.println("****** Validating Privacy Card and its options existance");
		logStep("Validating Privacy Card and its options existance ");
		stScreen.verify_PrivacyCard_Options_From_Settings("Smoke", "Privacy");

	}

	@Test(priority = 350, enabled = true)
	@Description("Enabling Preconfiguration for Severe1 Breaking News Card")
	public void enable_PreConfiguration_for_servere1_BreakingNewsCard() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Enable Preconfiguration for Severe1 Breaking News Card");
		logStep("Enable Preconfiguration for Severe1 Breaking News Card");
		proxy.quitCharlesProxy();
		this.configFile = this.changeVt1ContentMode(BN_SEVERE1_CONFIG_FILE_PATH, "severe1");
		proxy = new CharlesProxy("localhost", 8111, BN_SEVERE1_CONFIG_FILE_PATH);
		proxy.startCharlesProxyWithUI();
		proxy.enableRewriting();
		proxy.startRecording();
		proxy.disableMapLocal();
		Functions.close_launchApp();
		Functions.checkForAppState();
		stScreen.clear_Airlock_Cache();
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		addrScreen.enternewAddress(false, "Atlanta, Georgia");
		TestBase.waitForMilliSeconds(10000);
		Utils.navigateTofeedCard("breakingnews");
		Functions.archive_folder("Charles");
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();
	}

	@Test(priority = 351, enabled = true)
	@Description("Verify BreakingNews Severe1 ad call iu")
	public void Verify_BreakingNews_Severe1_AdCall() throws Exception {
		System.out.println("==============================================");
		
		System.out.println("****** Breaking News Severe1 Adcall verification test case Started");
		logStep("****** Breaking News Severe1 Adcall verification test case Started");
		
		Utils.verifyPubadCal("Smoke", "BreakingNews");
	}

	/**
	 * This method validates bn custom parameter of Breaking News Severe1 call
	 */
	@Test(priority = 352, enabled = true)
	@Description("Validating 'bn' custom parameter of BreakingNews Severe1 call ")
	public void Validate_BreakingNews_Severe1_bn_Custom_param() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating bn custom parameter of Breaking News Severe1 call");
		logStep("Validating bn custom parameter of Breaking News Severe1 call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "BreakingNews", "bn", "h");

	}

	/**
	 * This method validates pos custom parameter of Breaking News Severe1 call
	 */
	@Test(priority = 353, enabled = true)
	@Description("Validating 'pos' custom parameter of BreakingNews Severe1 call ")
	public void Validate_BreakingNews_Severe1_pos_Custom_param() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating pos custom parameter of Breaking News Severe1 call");
		logStep("Validating pos custom parameter of Breaking News Severe1 call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "BreakingNews", "pos", "app_sl");

	}

	@Test(priority = 361, enabled = true)
	@Description("Verify BreakingNews Severe1 Video1 ad call iu")
	public void Verify_BreakingNews_Video1_Severe1_AdCall() throws Exception {
		System.out.println("==============================================");
		
		System.out.println("****** Breaking News Severe1 Video1 Adcall verification test case Started");
		logStep("****** Breaking News Severe1 Video1 Adcall verification test case Started");
		proxy.clearCharlesSession();
		bnScreen.navigateToBreakingNewsDetailsPage();
		Functions.archive_folder("Charles");
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();
		Utils.get_iu_value_of_Feedcall("Smoke", "PreRollVideo");
		navigateBackToFeedCard();

	}

	/**
	 * This method validates bn custom parameter of Breaking News Severe1 Video1
	 * call
	 */
	@Test(priority = 362, enabled = true)
	@Description("Validating 'bn' custom parameter of BreakingNews Severe1 Video1 call ")
	public void Validate_BreakingNews_Video1_Severe1_bn_Custom_param() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating bn custom parameter of Breaking News Severe1 Video1 call");
		logStep("Validating bn custom parameter of Breaking News Severe1 Video1 call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "PreRollVideo", "bn", "h");

	}

	/**
	 * This method validates ref custom parameter of Breaking News Severe1 Video1
	 * call
	 */
	@Test(priority = 363, enabled = true)
	@Description("Validating 'ref' custom parameter of BreakingNews Severe1 Video1 call ")
	public void Validate_BreakingNews_Video1_Severe1_ref_Custom_param() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating ref custom parameter of Breaking News Severe1 Video1 call");
		logStep("Validating ref custom parameter of Breaking News Severe1 Video1 call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "PreRollVideo", "ref", "brn");

	}

	@Test(priority = 375, enabled = true)
	@Description("Enabling Preconfiguration for Severe2 Breaking News Card")
	public void enable_PreConfiguration_for_servere2_BreakingNewsCard() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Enable Preconfiguration for Severe2 Breaking News Card");
		logStep("Enable Preconfiguration for Severe2 Breaking News Card");
		proxy.quitCharlesProxy();
		this.configFile = this.changeVt1ContentMode(BN_SEVERE2_CONFIG_FILE_PATH, "severe2");
		proxy = new CharlesProxy("localhost", 8111, BN_SEVERE2_CONFIG_FILE_PATH);
		proxy.startCharlesProxyWithUI();
		proxy.enableRewriting();
		proxy.startRecording();
		proxy.disableMapLocal();
		Functions.close_launchApp();
		Functions.checkForAppState();
		stScreen.clear_Airlock_Cache();
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		addrScreen.enternewAddress(false, "Atlanta, Georgia");
		TestBase.waitForMilliSeconds(10000);
		Utils.navigateTofeedCard("breakingnews");
		Functions.archive_folder("Charles");
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();

	}

	@Test(priority = 376, enabled = true)
	@Description("Verify BreakingNews Severe2 ad call iu")
	public void Verify_BreakingNews_Severe2_AdCall() throws Exception {
		System.out.println("==============================================");
		
		System.out.println("****** Breaking News Severe2 Adcall verification test case Started");
		logStep("****** Breaking News Severe2 Adcall verification test case Started");
		
		Utils.verifyPubadCal("Smoke", "BreakingNews");
	}

	/**
	 * This method validates bn custom parameter of Breaking News Severe2 call
	 */
	@Test(priority = 377, enabled = true)
	@Description("Validating 'bn' custom parameter of BreakingNews Severe2 call ")
	public void Validate_BreakingNews_Severe2_bn_Custom_param() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating bn custom parameter of Breaking News Severe2 call");
		logStep("Validating bn custom parameter of Breaking News Severe2 call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "BreakingNews", "bn", "y");

	}

	/**
	 * This method validates pos custom parameter of Breaking News Severe2 call
	 */
	@Test(priority = 378, enabled = true)
	@Description("Validating 'pos' custom parameter of BreakingNews Severe2 call ")
	public void Validate_BreakingNews_Severe2_pos_Custom_param() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating pos custom parameter of Breaking News Severe2 call");
		logStep("Validating pos custom parameter of Breaking News Severe2 call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "BreakingNews", "pos", "app_sl");

	}

	@Test(priority = 381, enabled = true)
	@Description("Verify BreakingNews Severe2 Video1 ad call iu")
	public void Verify_BreakingNews_Video1_Severe2_AdCall() throws Exception {
		System.out.println("==============================================");
		
		System.out.println("****** Breaking News Severe2 Video1 Adcall verification test case Started");
		logStep("****** Breaking News Severe2 Video1 Adcall verification test case Started");
		proxy.clearCharlesSession();
		bnScreen.navigateToBreakingNewsDetailsPage();
		Functions.archive_folder("Charles");
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();
		Utils.get_iu_value_of_Feedcall("Smoke", "PreRollVideo");
		navigateBackToFeedCard();
	}

	/**
	 * This method validates bn custom parameter of Breaking News Severe2 Video1
	 * call
	 */
	@Test(priority = 382, enabled = true)
	@Description("Validating 'bn' custom parameter of BreakingNews Severe2 Video1 call ")
	public void Validate_BreakingNews_Video1_Severe2_bn_Custom_param() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating bn custom parameter of Breaking News Severe2 Video1 call");
		logStep("Validating bn custom parameter of Breaking News Severe2 Video1 call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "PreRollVideo", "bn", "y");

	}

	/**
	 * This method validates ref custom parameter of Breaking News Severe1 Video1
	 * call
	 */
	@Test(priority = 383, enabled = true)
	@Description("Validating 'ref' custom parameter of BreakingNews Severe2 Video1 call ")
	public void Validate_BreakingNews_Video1_Severe2_ref_Custom_param() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating ref custom parameter of Breaking News Severe2 Video1 call");
		logStep("Validating ref custom parameter of Breaking News Severe2 Video1 call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "PreRollVideo", "ref", "brn");

	}
	
	@Test(priority = 400, enabled = true)
	@Description("Enabling Preconfiguration for Severe1 Editorial Video Headline Card Breaking News Card")
	public void enable_PreConfiguration_for_servere1_EditorialVideoHeadlineCard_BreakingNewsCard() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Enable Preconfiguration for Severe1 Editorial Video Headline Card Breaking News Card");
		logStep("Enable Preconfiguration for Severe1 Editorial Video Headline Card Breaking News Card");
		proxy.quitCharlesProxy();
		this.configFile = this.changeVt1ContentMode(BN_SEVERE1_CONFIG_FILE_PATH, "severe1");
		proxy = new CharlesProxy("localhost", 8111, BN_SEVERE1_CONFIG_FILE_PATH);
		proxy.startCharlesProxyWithUI();
		proxy.enableRewriting();
		proxy.startRecording();
		proxy.disableMapLocal();
		Functions.close_launchApp();
		Functions.checkForAppState();
		
		stScreen.select_Airlock_UserGroup("IOSFLAG-6963");
		
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		addrScreen.enternewAddress(false, "Atlanta, Georgia");
		TestBase.waitForMilliSeconds(10000);
			
		Utils.navigateTofeedCard("Breaking News");
		Functions.archive_folder("Charles");
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();
	}

	@Test(priority = 401, enabled = true)
	@Description("Verify Editorial Video Headline Card Breaking News Severe1 Video1 ad call iu")
	public void Verify_Editorial_Video_Headline_Card_Breaking_News_Card_Video1_Severe1_AdCalls() throws Exception {
		System.out.println("==============================================");
		
		System.out.println("****** Editorial Video Headline Card Breaking News Card Severe1 Video1 Adcall verification test case Started");
		logStep("****** Editorial Video Headline Card Breaking News Card Severe1 Video1 Adcall verification test case Started");
		proxy.clearCharlesSession();
		bnScreen.navigateToEditorialVideoHeadlineCardBreakingNewsDetailsPage();
		Functions.archive_folder("Charles");
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();
		Utils.get_iu_value_of_Feedcall("Smoke", "PreRollVideo");
		navigateBackToFeedCard();

	}

	
	/**
	 * This method validates ref custom parameter of Editorial Video Headline Card Breaking News Severe1 Video1
	 * call
	 */
	@Test(priority = 402, enabled = true)
	@Description("Validating 'ref' custom parameter of Editorial Video Headline Card Breaking News Severe1 Video1 call ")
	public void Validate_Editorial_Video_Headline_Card_Breaking_News_Card_Video1_Severe1_ref_Custom_params() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating ref custom parameter of Editorial Video Headline Card Breaking News Severe1 Video1 call");
		logStep("Validating ref custom parameter of Editorial Video Headline Card Breaking News Severe1 Video1 call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "PreRollVideo", "ref", "brn");

	}
	
	@Test(priority = 425, enabled = true)
	@Description("Enabling Preconfiguration for Severe2 Editorial Video Headline Card Breaking News Card")
	public void enable_PreConfiguration_for_servere2_EditorialVideoHeadlineCard_BreakingNewsCard() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Enable Preconfiguration for Severe2 Editorial Video Headline Card Breaking News Card");
		logStep("Enable Preconfiguration for Severe2 Editorial Video Headline Card Breaking News Card");
		proxy.quitCharlesProxy();
		this.configFile = this.changeVt1ContentMode(BN_SEVERE1_CONFIG_FILE_PATH, "severe2");
		proxy = new CharlesProxy("localhost", 8111, BN_SEVERE1_CONFIG_FILE_PATH);
		proxy.startCharlesProxyWithUI();
		proxy.enableRewriting();
		proxy.startRecording();
		proxy.disableMapLocal();
		Functions.close_launchApp();
		Functions.checkForAppState();
		
		//stScreen.select_Airlock_UserGroup("IOSFLAG-6963");
		stScreen.clear_Airlock_Cache();
		
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		addrScreen.enternewAddress(false, "Atlanta, Georgia");
		TestBase.waitForMilliSeconds(10000);
			
		Utils.navigateTofeedCard("Breaking News");
		Functions.archive_folder("Charles");
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();
	}

	@Test(priority = 426, enabled = true)
	@Description("Verify Editorial Video Headline Card Breaking News Severe2 Video1 ad call iu")
	public void Verify_Editorial_Video_Headline_Card_Breaking_News_Card_Video1_Severe2_AdCalls() throws Exception {
		System.out.println("==============================================");
		
		System.out.println("****** Editorial Video Headline Card Breaking News Card Severe2 Video1 Adcall verification test case Started");
		logStep("****** Editorial Video Headline Card Breaking News Card Severe2 Video1 Adcall verification test case Started");
		proxy.clearCharlesSession();
		bnScreen.navigateToEditorialVideoHeadlineCardBreakingNewsDetailsPage();
		Functions.archive_folder("Charles");
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();
		Utils.get_iu_value_of_Feedcall("Smoke", "PreRollVideo");
		navigateBackToFeedCard();

	}

	
	/**
	 * This method validates ref custom parameter of Editorial Video Headline Card Breaking News Severe2 Video1
	 * call
	 */
	@Test(priority = 427, enabled = true)
	@Description("Validating 'ref' custom parameter of Editorial Video Headline Card Breaking News Severe2 Video1 call ")
	public void Validate_Editorial_Video_Headline_Card_Breaking_News_Card_Video1_Severe2_ref_Custom_params() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating ref custom parameter of Editorial Video Headline Card Breaking News Severe2 Video1 call");
		logStep("Validating ref custom parameter of Editorial Video Headline Card Breaking News Severe2 Video1 call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "PreRollVideo", "ref", "brn");

	}

	/**
	 * This Script Validate NextGen IM Ad and its parameters
	 * @throws Exception
	 */
	@Test(priority = 601, enabled = true)
	@Description("Validating NextGen IM Static Ad when app in test mode")
	public void Validate_NextGenIM_StaticAd() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating NextGen IM Static Ad in test mode");
		logStep("Validating NextGen IM Static Ad in test mode ");
		proxy.disableRewriting();
		proxy.clearCharlesSession();
		hmTab.clickonHomeTab();
		stScreen.select_Airlock_UserGroup("AdsTestAdUnitOnly");
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		addrScreen.enternewAddress(false, "07095", "Woodbridge Township, New Jersey");
		TestBase.waitForMilliSeconds(20000);
		Functions.archive_folder("Charles");
		TestBase.waitForMilliSeconds(5000);
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();
		Utils.verifyPubadCal("Smoke", "NextGenIM");
		
	}

	/**
	 * This Script Validate NextGen IM Ad response
	 * @throws Exception
	 */
	@Test(priority = 602, enabled = true)
	@Description("Validating NextGen IM Static Ad response when app in test mode")
	public void Validate_NextGenIM_StaticAd_response() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating NextGen IM Static Ad response in test mode");
		logStep("Validating NextGen IM Static Ad response in test mode ");

		Utils.verifyMarqueeAd_byCallResponse("Smoke", "NextGenIM");

	}

	/**
	 * This Script Validate NextGen IM Ad and its parameters
	 * @throws Exception
	 */

	@Test(priority = 603, enabled = true)

	@Description("Validating NextGen IM Static Ad BackGround Asset Call")
	public void Validate_NextGenIM_StaticAd_bgAssetCall() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating NextGen IM Static Ad BG Asset Call");
		logStep("Validating NextGen IM Static Ad BG Asset Call ");

		Utils.verifyBGAd_byCallResponse("Smoke", "NextGenIM", "Static");

	}

	/**
	 * This Script Validate NextGen IM Ad and its parameters
	 * @throws Exception
	 */

	@Test(priority = 604, enabled = true)
	@Description("Validating NextGen IM Static Ad ForeGround Asset Call")
	public void Validate_NextGenIM_StaticAd_fgAssetCall() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating NextGen IM Static Ad FG Asset Call");
		logStep("Validating NextGen IM Static Ad FG Asset Call ");

		Utils.verifyFGAd_byCallResponse("Smoke", "NextGenIM", "Static");

	}

	@Test(priority = 605, enabled = true)
	@Description("Validating NextGen IM Static Ad sz parameter")
	public void Validate_NextGenIM_StaticAd_Size() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating NextGen IM Static Ad sz parameter in charles");
		logStep("Validating NextGen IM Static Ad sz parameter in charles ");

		Utils.verify_Ad_Size("Smoke", "NextGenIM");

	}

	@Test(priority = 611, enabled = true)
	@Description("Validating NextGen IM Video Ad and its Parameters when app in test mode")
	public void Validate_NextGenIM_VideoAd() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating NextGen IM Video Ad in test mode");
		logStep("Validating NextGen IM Video Ad in test mode ");
		proxy.clearCharlesSession();
		addrScreen.enternewAddress(false, "61920", "Charleston, Illinois");
		TestBase.waitForMilliSeconds(20000);
		Functions.archive_folder("Charles");
		TestBase.waitForMilliSeconds(5000);
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();
		Utils.verifyPubadCal("Smoke", "NextGenIM");

	}

	@Test(priority = 612, enabled = true)
	@Description("Validating NextGen IM Video Ad response when app in test mode")
	public void Validate_NextGenIM_VideoAd_response() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating NextGen IM Video Ad response in test mode");
		logStep("Validating NextGen IM Video Ad response in test mode ");
		Utils.verifyMarqueeAd_byCallResponse("Smoke", "NextGenIM");

	}

	/**
	 * This Script Validate NextGen IM Ad and its parameters
	 * @throws Exception
	 */
	@Test(priority = 613, enabled = true)
	@Description("Validating NextGen IM Video Ad BackGround Asset Call")
	public void Validate_NextGenIM_VideoAd_bgAssetCall() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating NextGen IM Video Ad BG Asset Call");
		logStep("Validating NextGen IM Video Ad BG Asset Call ");
		Utils.verifyBGAd_byCallResponse("Smoke", "NextGenIM", "Video");

	}

	/**
	 *This Script Validate NextGen IM Ad and its parameters Video
	 *ad will not be having only fg asset call always, it has
	 *only bg asset call hence commenting
	 *@throws Exception 
	 */
	/*
	 * @Test(priority = 614, enabled = true)
	 * 
	 * @Description("Validating NextGen IM Video Ad ForeGround Asset Call") public
	 * void Validate_NextGenIM_VideoAd_fgAssetCall() throws Exception {
	 * System.out.println("==============================================");
	 * System.out.println("****** Validating NextGen IM Video Ad FG Asset Call");
	 * logStep("Validating NextGen IM Video Ad FG Asset Call ");
	 * 
	 * Utils.verifyFGAd_byCallResponse("Smoke", "NextGenIM", "Video");
	 * 
	 * }
	 */

	@Test(priority = 615, enabled = true)
	@Description("Validating NextGen IM Video Ad sz parameter")
	public void Validate_NextGenIM_VideoAd_Size() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating NextGen IM Video Ad sz parameter in charles");
		logStep("Validating NextGen IM Video Ad sz parameter in charles ");
		Utils.verify_Ad_Size("Smoke", "NextGenIM");

	}
	
	@Test(priority = 651, enabled = true)
	@Description("Validating Tapability Of HomeScreen Sticky Test Ad when app in test mode")
	public void Validate_TapabilityOfHomeScreenStickyTestAd() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Tapability Of HomeScreen Sticky Test Ad in test mode");
		logStep("Validating Tapability Of HomeScreen Sticky Test Ad in test mode ");
		proxy.disableRewriting();
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		hmTab.clickonHomeTab();
		hmTab.verifyTapabilityOfStickyTestAdOnHomeScreen();
		
	}
	
	@Test(priority = 652, enabled = true)
	@Description("Validating Tapability Of Daily Nav Tab Test Ad when app in test mode")
	public void Validate_TapabilityOfDailyNavTabTestAd() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Tapability Of Daily Nav Tab Test Ad in test mode");
		logStep("Validating Tapability Of Daily Nav Tab Test Ad in test mode ");
		proxy.disableRewriting();
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		dTab.navigateToDailyTab();
		dTab.verifyTapabilityOfTestAdOnDailyNavTab();
		
	}
	
	@Test(priority = 653, enabled = true)
	@Description("Validating Tapability Of Radar Nav Tab Test Ad when app in test mode")
	public void Validate_TapabilityOfRadarNavTabTestAd() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Tapability Of Radar Nav Tab Test Ad in test mode");
		logStep("Validating Tapability Of Radar Nav Tab Test Ad in test mode ");
		proxy.disableRewriting();
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		hmTab.clickonHomeTab();
		rTab.navigateToRadarTab();
		rTab.verifyTapabilityOfTestAdOnRadarNavTab();
		
	}
	
	@Test(priority = 654, enabled = true)
	@Description("Validating Tapability Of Seasonal Hub Details Page Test Ad when app in test mode")
	public void Validate_TapabilityOfSeasonalHubDetailsPageTestAd() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Tapability Of Seasonal Hub Details Page Test Ad in test mode");
		logStep("Validating Tapability Of Seasonal Hub Details Page Test Ad in test mode ");
		proxy.disableRewriting();
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		hmTab.clickonHomeTab();
		Utils.navigateTofeedCard("seasonalhub");
		sScreen.navigateToDetailsOfFirstIndexOfSeasonalHubCard();
		sScreen.verifyTapabilityOfTestAdOnSeasonalHubDetailsPage();
		
	}

	/**
	 *This Script Validate Integrated Daily Details Ad Call and its response
	 * @throws Exception 
	 */
	@Test(priority = 701, enabled = true)
	@Description("Validating IDD Ad when app in test mode")
	public void Validate_IDD_Ad() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating IDD Ad in test mode");
		logStep("Validating IDD Ad in test mode ");
		hmTab.clickonHomeTab();
		stScreen.select_Airlock_UserGroup("IOSFLAG-4200");
		Functions.close_launchApp();
		Functions.checkForAppState();
		proxy.clearCharlesSession();
		addrScreen.enternewAddress(false, "30124", "Cave Spring, Georgia");
		TestBase.waitForMilliSeconds(10000);
		dTab.navigateToDailyTab();
		TestBase.waitForMilliSeconds(10000);
		Functions.archive_folder("Charles");
		TestBase.waitForMilliSeconds(5000);
		proxy.getXml();
		Utils.createXMLFileForCharlesSessionFile();
		Utils.get_v3_wx_forecast_daily_15day_data();
		Utils.verifyPubadCal("Smoke", "IDD");

	}

	@Test(priority = 702, enabled = true)
	@Description("Validating IDD Ad response")
	public void Validate_IDD_Ad_response() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating IDD Ad response");
		logStep("Validating IDD Ad response");
		dTab.verifyIDDAd_byCallResponse("Smoke", "IDD");

	}

	/**
	 * This Script Validate IDD Ad and its parameters
	 * @throws Exception 
	 */

	@Test(priority = 703, enabled = true)
	@Description("Validating IDD Static Ad BackGround Asset Call")
	public void Validate_IDD_StaticAd_bgAssetCall() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating IDD Static Ad BG Asset Call");
		logStep("Validating IDD Static Ad BG Asset Call ");
		Utils.verifyBGAd_byCallResponse("Smoke", "IDD", "Static");

	}

	/**
	 * This Script Validate IDD Ad and its parameters
	 * @throws Exception 
	 */

	@Test(priority = 704, enabled = true)
	@Description("Validating IDD Static Ad ForeGround Asset Call")
	public void Validate_IDD_StaticAd_fgAssetCall() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating IDD Static Ad FG Asset Call");
		logStep("Validating IDD Static Ad FG Asset Call ");
		Utils.verifyFGAd_byCallResponse("Smoke", "IDD", "Static");
	}

	@Test(priority = 705, enabled = true)
	@Description("Validating IDD Ad sz parameter")
	public void Validate_IDD_Ad_Size() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating IDD Ad sz parameter in charles");
		logStep("Validating IDD Ad sz parameter in charles ");
		Utils.verify_Ad_Size("Smoke", "IDD");

	}

	/**
	 * This Script Validate Integrated Feed Card Static Ad Call and its response
	 * @throws Exception                 
	 */
	@Test(priority = 751, enabled = true)
	@Description("Validating Integrated Feed Card Static Ad i.e. Feed1 Card when app in test mode")
	public void Validate_Integrated_FeedCard_StaticAd() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Integrated Feed Card Static Ad in test mode");
		logStep("Validating Integrated Feed Card Static Ad in test mode ");
		try{
			hmTab.clickonHomeTab();
			stScreen.select_Airlock_UserGroup("IntegratedAdCard");
			proxy.clearCharlesSession();
			Functions.close_launchApp();
			Functions.checkForAppState();
			addrScreen.enternewAddress(false, "30124", "Cave Spring, Georgia");
			TestBase.waitForMilliSeconds(10000);
			// Since as part of sticky ad implementation on UI weather.feed1 appears as weather.feed0 and so on
			//hence modified Integrated feed card name to weather.feed0 from weather.feed1
			Utils.navigateTofeedCard("weather.feed0");
			TestBase.waitForMilliSeconds(20000);
			Functions.archive_folder("Charles");
			
		} finally {
			proxy.getXml();
			Utils.createXMLFileForCharlesSessionFile();
			Utils.verifyPubadCal("Smoke", "IntegratedFeedCard");
		}
	}

	@Test(priority = 752, enabled = true)
	@Description("Validating Integrated Feed Card Static Ad response")
	public void Validate_Integrated_FeedCard_StaticAd_response() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Integrated Feed Card Static Ad response");
		logStep("Validating Integrated Feed Card Static Ad response");
		Utils.verifyIntegratedFeedCardAd_byCallResponse("Smoke", "IntegratedFeedCard");
	}

	/**
	 * This Script Validate Integrated Feed Card Static Ad and its parameters
	 * @throws Exception                
	 */

	@Test(priority = 753, enabled = true)
	@Description("Validating Integrated Feed Card Static Ad BackGround Asset Call")
	public void Validate_Integrated_FeedCard_StaticAd_bgAssetCall() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Integrated Feed Card Static Ad BG Asset Call");
		logStep("Validating Integrated Feed Card Static Ad BG Asset Call ");
		
		Utils.verifyAssetCallWithHostandPath("s.w-x.co", "/cl/prototype/", "-ifc-stc", "-ifc-stc");
	}

	/**
	 * This Script Validate Integrated Feed Card Static Ad and its parameters
	 * @throws Exception              
	 */
	@Test(priority = 754, enabled = true)
	@Description("Validating Integrated Feed Card Static Ad ForeGround Asset Call")
	public void Validate_Integrated_FeedCard_StaticAd_fgAssetCall() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Integrated Feed Card Static Ad FG Asset Call");
		logStep("Validating Integrated Feed Card Static Ad FG Asset Call ");
		
		Utils.verifyAssetCallWithHostandPath("s.w-x.co", "/cl/prototype/", "-ifc-stc", "-fg");

	}

	@Test(priority = 755, enabled = true)
	@Description("Validating Integrated Feed Card Static Ad sz parameter")
	public void Validate_Integrated_FeedCard_StaticAd_Size() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Integrated Feed Card Static Ad sz parameter in charles");
		logStep("Validating Integrated Feed Card Static Ad sz parameter in charles ");
		Utils.verify_Ad_Size("Smoke", "IntegratedFeedCard");

	}

	/**
	 * This Script Validate Integrated Feed Card Video Ad Call and its response
	 * @throws Exception          
	 */
	@Test(priority = 761, enabled = true)
	@Description("Validating Integrated Feed Card Video Ad i.e. Feed1 Card when app in test mode")
	public void Validate_Integrated_FeedCard_VideoAd() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Integrated Feed Card Video Ad in test mode");
		logStep("Validating Integrated Feed Card Video Ad in test mode ");
		try {
			hmTab.clickonHomeTab();
			proxy.clearCharlesSession();
			addrScreen.enternewAddress(false, "08824", "South Brunswick, New Jersey");
			TestBase.waitForMilliSeconds(10000);
			// Since as part of sticky ad implementation on UI weather.feed1 appears as weather.feed0 and so on
			//hence modified Integrated feed card name to weather.feed0 from weather.feed1
			Utils.navigateTofeedCard("weather.feed0");
			TestBase.waitForMilliSeconds(20000);
			Functions.archive_folder("Charles");
			
		} finally {
			proxy.getXml();
			Utils.createXMLFileForCharlesSessionFile();
			Utils.verifyPubadCal("Smoke", "IntegratedFeedCard");
		}
	}

	@Test(priority = 762, enabled = true)
	@Description("Validating Integrated Feed Card Video Ad response")
	public void Validate_Integrated_FeedCard_VideoAd_response() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Integrated Feed Card Video Ad response");
		logStep("Validating Integrated Feed Card Video Ad response");
		Utils.verifyIntegratedFeedCardAd_byCallResponse("Smoke", "IntegratedFeedCard");

	}

	/**
	 * This Script Validate Integrated Feed Card Video Ad and its parameters
	 * @throws Exception            
	 */

	@Test(priority = 763, enabled = true)
	@Description("Validating Integrated Feed Card Video Ad BackGround Asset Call")
	public void Validate_Integrated_FeedCard_VideoAd_bgAssetCall() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Integrated Feed Card Video Ad BG Asset Call");
		logStep("Validating Integrated Feed Card Video Ad BG Asset Call ");
		
		Utils.verifyAssetCallWithHostandPath("v.w-x.co", "/digital_video/",
				"The_Weather_Channel_-_Reach_Engine%2F8e82ac41-bb5b-49f1-b608-c00b6860375b", "-bg");
	}

	/**
	 * This Script Validate Integrated Feed Card Static Ad and its
	 * parameters Since fg asset call is same for static and video
	 * calls, once it displays in static mode it doesnt occur in
	 * video mode as its get cached. hence below case is commented
	 *                   
	 * @throws Exception 
	 */

	/*
	 * @Test(priority = 764, enabled = true)
	 * 
	 * @Description("Validating Integrated Feed Card Video Ad ForeGround Asset Call"
	 * ) public void Validate_Integrated_FeedCard_VideoAd_fgAssetCall() throws
	 * Exception {
	 * System.out.println("==============================================");
	 * System.out.
	 * println("****** Validating Integrated Feed Card Video Ad FG Asset Call");
	 * logStep("Validating Integrated Feed Card Video Ad FG Asset Call ");
	 * 
	 * Utils.verifyFGAd_byCallResponse("Smoke", "IntegratedFeedCard", "Video"); }
	 */

	@Test(priority = 765, enabled = true)
	@Description("Validating Integrated Feed Card Video Ad sz parameter")
	public void Validate_Integrated_FeedCard_VideoAd_Size() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Integrated Feed Card Video Ad sz parameter in charles");
		logStep("Validating Integrated Feed Card Video Ad sz parameter in charles ");

		Utils.verify_Ad_Size("Smoke", "IntegratedFeedCard");

	}

	@Test(priority = 801, enabled = true)
	@Description("Verify Entry Interstitial Ads of Hourly Tab")
	public void Verify_interstitial_ads_hourlyTab() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"=========================== Entry Interstitial Ad Verification of Hourly Tab====================");

		System.out.println("****** Entry Interstitial Ad validation of Hourly Tab Started");
		logStep("Entry Interstitial Ad validation of Hourly Tab Started ");

		stScreen.select_Airlock_Branch("UnlimitedInterstitialAutomation01");
		stScreen.select_Airlock_UserGroup("UnlimitedInterstitial");
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		Functions.unlimitedInterstitial = true;
		Utils.assertinterStitialAd("Smoke", "Hourly", "hourlyTab", "Entry");

	}

	@Test(priority = 802, enabled = true)
	@Description("Verify Exit Interstitial Ads of Daily Tab")
	public void Verify_interstitial_ads_dailyTab() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"=========================== Exit Interstitial Ad Verification of Daily Tab====================");

		System.out.println("****** Exit Interstitial Ad validation of Daily Tab Started");
		logStep("Exit Interstitial Ad validation of Daily Tab Started ");
		TestBase.waitForMilliSeconds(40000);
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		Functions.unlimitedInterstitial = true;
		Utils.assertinterStitialAd("Smoke", "Daily(10day)", "dailyTab", "Exit");
	}

	@Test(priority = 803, enabled = true)
	@Description("Verify Exit Interstitial Ads of Radar Tab")
	public void Verify_interstitial_ads_radarTab() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"=========================== Exit Interstitial Ad Verification of Radar Tab====================");

		System.out.println("****** Exit Interstitial Ad validation of Radar Tab Started");
		logStep("Exit Interstitial Ad validation of Radar Tab Started ");
		TestBase.waitForMilliSeconds(40000);
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		Functions.unlimitedInterstitial = true;
		Utils.assertinterStitialAd("Smoke", "Map", "mapTab", "Exit");
	}

	@Test(priority = 804, enabled = true)
	@Description("Verify Exit Interstitial Ads of Video Tab")
	public void Verify_interstitial_ads_videoTab() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"=========================== Exit Interstitial Ad Verification of Video Tab====================");

		System.out.println("****** Exit Interstitial Ad validation of Video Tab Started");
		logStep("Exit Interstitial Ad validation of Video Tab Started ");
		TestBase.waitForMilliSeconds(40000);
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		Functions.unlimitedInterstitial = true;
		Utils.assertinterStitialAd("Smoke", "PreRollVideo", "videoTab", "Exit");
		// Functions.unlimitedInterstitial = false;
	}

	@Test(priority = 805, enabled = true)
	@Description("Verify Entry Interstitial Ads of Hourly Details Banner from Planning Card")
	public void Verify_interstitial_ads_hourlyDetailsBanner_fromPlanningCard() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"=========================== Entry Interstitial Ad Verification of Hourly Details Banner from Planning Card====================");

		System.out
				.println("****** Entry Interstitial Ad validation of Hourly Details Banner from Planning Card Started");
		logStep("Exit Interstitial Ad validation of Hourly Details Banner from Planning Card Started ");
		TestBase.waitForMilliSeconds(40000);
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		Functions.unlimitedInterstitial = true;
		Utils.assertinterStitialAd("Smoke", "Hourly", "hourlybanner", "Entry");

	}

	@Test(priority = 806, enabled = true)
	@Description("Verify Exit Interstitial Ads of Daily Details Banner from Planning Card")
	public void Verify_interstitial_ads_dailyDetailsBanner_fromPlanningCard() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"=========================== Exit Interstitial Ad Verification of Daily Details Banner from Planning Card====================");

		System.out.println("****** Exit Interstitial Ad validation of Daily Details Banner from Planning Card Started");
		logStep("Exit Interstitial Ad validation of Daily Details Banner from Planning Card Started ");
		TestBase.waitForMilliSeconds(40000);
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		Functions.unlimitedInterstitial = true;
		Utils.assertinterStitialAd("Smoke", "Daily(10day)", "dailybanner", "Exit");
	}

	@Test(priority = 807, enabled = true)
	@Description("Verify Exit Interstitial Ads of Daily Card")
	public void Verify_interstitial_ads_dailyCard() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"=========================== Exit Interstitial Ad Verification of Daily Card====================");

		System.out.println("****** Exit Interstitial Ad validation of Daily Card Started");
		logStep("Exit Interstitial Ad validation of Daily Card Started ");
		TestBase.waitForMilliSeconds(40000);
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		Functions.unlimitedInterstitial = true;
		Utils.assertinterStitialAd("Smoke", "Daily(10day)", "daily", "Exit");
		
	}

	
	
	@Test(priority = 808, enabled = true)
	@Description("Verify Exit Interstitial Ads of Map Card")
	public void Verify_interstitial_ads_mapCard() throws Exception {
		System.out.println("==============================================");
		System.out.println(
				"=========================== Exit Interstitial Ad Verification of Map Card====================");

		System.out.println("****** Exit Interstitial Ad validation of Map Card Started");
		logStep("Exit Interstitial Ad validation of Map Card Started ");
		TestBase.waitForMilliSeconds(40000);
		proxy.clearCharlesSession();
		Functions.close_launchApp();
		Functions.checkForAppState();
		Functions.unlimitedInterstitial = true;
		Utils.assertinterStitialAd("Smoke", "Map", "radar.largead", "Exit");
		Functions.unlimitedInterstitial = false;
	}
	 

	/*
	 * @Test(priority = 805, enabled = true)
	 * 
	 * @Description("Verify Exit Interstitial Ads of video feed card") public void
	 * Verify_interstitial_ads_video_feedcard() throws Exception {
	 * System.out.println("==============================================");
	 * System.out.
	 * println("=========================== Exit Interstitial Ad Verification of video feed card===================="
	 * );
	 * 
	 * System.out.
	 * println("****** Exit Interstitial Ad validation of video feed card Started");
	 * 
	 * //Functions.Setappinto_TestMode("UnlimitedInterstitial"); try {
	 * Utils.assertinterStitialAd("Smoke", "PreRollVideo", "video");
	 * }catch(Exception e) { System.out.
	 * println("There is an exception in validating Interstitial Ads of video feed card"
	 * );
	 * logStep("There is an exception in validating Interstitial Ads of video feed card"
	 * ); }finally { Functions.unlimitedInterstitial = false;
	 * Functions.Setappinto_TestMode("UnSelect");
	 * 
	 * } }
	 */

	/**
	 * This Script Enable preconfiguration for spotlight cards i.e. Flu, Allergy, Week Ahead, Weekend
	 * @throws Exception    
	 */
	@Test(priority = 901, enabled = true)
	@Description("Enabling Preconfiguration for Watson Moment and Planning Moment Cards")
	public void enable_PreConfiguration_for_WatsonAndPlanningMomentCards() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Enable Preconfiguration for Watson Moment and Planning Moment Cards");
		logStep("Enable Preconfiguration for Watson Moment and Planning Moment Cards ");
		hmTab.clickonHomeTab();
		stScreen.select_Airlock_Branch("WM Cards New");
		stScreen.select_Airlock_UserGroup("WM Cards");
		Functions.close_launchApp();
		Functions.checkForAppState();
		proxy.clearCharlesSession();
		addrScreen.enternewAddress(false, "New York City, New York");
		TestBase.waitForMilliSeconds(10000);
		try {
			Utils.navigateToAllCards(false, false);
		} catch (Exception e) {
			System.out.println("There is an exception while navigting to all the feed cards.");
			logStep("There is an exception while navigting to all the feed cards.");
		} finally {
			Functions.archive_folder("Charles");
			TestBase.waitForMilliSeconds(5000);
			proxy.getXml();
			Utils.createXMLFileForCharlesSessionFile();
		}
	}

	@Test(priority = 902, enabled = true)
	@Description("Verify Week Ahead ad call iu")
	public void Verify_Week_Ahead_AdCall() throws Exception {
		System.out.println("==============================================");
		
		System.out.println("****** Week Ahead Adcall verification test case Started");
		logStep("****** Week Ahead Adcall verification test case Started");

		Utils.verifyPubadCal("Smoke", "WeekAhead");
	}

	/**
	 * This method validates pos custom parameter of Week Ahead call
	 */
	@Test(priority = 903, enabled = true)
	@Description("Validating 'pos' custom parameter of Week Ahead call ")
	public void Validate_Week_Ahead_pos_Custom_param() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating pos custom parameter of Week Ahead call");
		logStep("Validating pos custom parameter of Week Ahead call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "WeekAhead", "pos", "app_sl");

	}

	@Test(priority = 904, enabled = true)
	@Description("Validating Week Ahead call Ad sz parameter")
	public void Validate_Week_Ahead_Ad_Size() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Week Ahead call Ad sz parameter in charles");
		logStep("Validating Week Ahead call Ad sz parameter in charles ");

		Utils.verify_Ad_Size("Smoke", "WeekAhead");

	}

	@Test(priority = 905, enabled = true)
	@Description("Validating Week Ahead call Response")
	public void Validate_Week_Ahead_Call_Response() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Week Ahead call response in charles");
		logStep("Validating Week Ahead call response in charles ");

		Utils.Verify_Gampad_Call_ByResponseText("Smoke", "WeekAhead");

	}

	@Test(priority = 911, enabled = true)
	@Description("Verify Weekend ad call iu")
	public void Verify_Weekend_AdCall() throws Exception {
		System.out.println("==============================================");
	
		System.out.println("****** Weekend Adcall verification test case Started");
		logStep("****** Weekend Adcall verification test case Started");

		Utils.verifyPubadCal("Smoke", "Weekend");
	}

	/**
	 * This method validates pos custom parameter of Weekend call
	 */
	@Test(priority = 912, enabled = true)
	@Description("Validating 'pos' custom parameter of Weekend call ")
	public void Validate_Weekend_pos_Custom_param() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating pos custom parameter of Weekend call");
		logStep("Validating pos custom parameter of Weekend call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "Weekend", "pos", "app_sl");

	}

	@Test(priority = 913, enabled = true)
	@Description("Validating Weekend call Ad sz parameter")
	public void Validate_Weekend_Ad_Size() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Weekend call Ad sz parameter in charles");
		logStep("Validating Weekend call Ad sz parameter in charles ");

		Utils.verify_Ad_Size("Smoke", "Weekend");

	}

	@Test(priority = 914, enabled = true)
	@Description("Validating Weekend call Response")
	public void Validate_Weekend_Call_Response() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating Weekend call response in charles");
		logStep("Validating Weekend call response in charles ");

		Utils.Verify_Gampad_Call_ByResponseText("Smoke", "Weekend");

	}

	@Test(priority = 921, enabled = true)
	@Description("Verify WM Flu ad call iu")
	public void Verify_WMFlu_AdCall() throws Exception {
		System.out.println("==============================================");
		
		System.out.println("****** WM Flu Adcall verification test case Started");
		logStep("****** WM Flu Adcall verification test case Started");

		Utils.verifyPubadCal("Smoke", "WMFlu");
	}

	/**
	 * This method validates pos custom parameter of WM Flu call
	 */
	@Test(priority = 922, enabled = true)
	@Description("Validating 'pos' custom parameter of WM Flu call ")
	public void Validate_WMFlu_pos_Custom_param() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating pos custom parameter of WM Flu call");
		logStep("Validating pos custom parameter of WM Flu call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "WMFlu", "pos", "app_sl");

	}

	@Test(priority = 923, enabled = true)
	@Description("Validating WM Flu call Ad sz parameter")
	public void Validate_WMFlu_Ad_Size() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating WM Flu call Ad sz parameter in charles");
		logStep("Validating WM Flu call Ad sz parameter in charles ");

		Utils.verify_Ad_Size("Smoke", "WMFlu");

	}

	@Test(priority = 924, enabled = true)
	@Description("Validating WM Flu call Response")
	public void Validate_WMFlu_Call_Response() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating WM Flu call response in charles");
		logStep("Validating WM Flu call response in charles ");

		Utils.Verify_Gampad_Call_ByResponseText("Smoke", "WMFlu");

	}

	@Test(priority = 931, enabled = true)
	@Description("Verify WM Allergy ad call iu")
	public void Verify_WMAllergy_AdCall() throws Exception {
		System.out.println("==============================================");
	
		System.out.println("****** WM Allergy Adcall verification test case Started");
		logStep("****** WM Allergy Adcall verification test case Started");

		Utils.verifyPubadCal("Smoke", "WMAllergy");
	}

	/**
	 * This method validates pos custom parameter of WM Allergy call
	 */
	@Test(priority = 932, enabled = true)
	@Description("Validating 'pos' custom parameter of WM Allergy call ")
	public void Validate_WMAllergy_pos_Custom_param() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating pos custom parameter of WM Allergy call");
		logStep("Validating pos custom parameter of WM Allergy call ");

		Utils.validate_custom_param_val_of_gampad("Smoke", "WMAllergy", "pos", "app_sl");

	}

	@Test(priority = 933, enabled = true)
	@Description("Validating WM Allergy call Ad sz parameter")
	public void Validate_WMAllergy_Ad_Size() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating WM Allergy call Ad sz parameter in charles");
		logStep("Validating WM Allergy call Ad sz parameter in charles ");

		Utils.verify_Ad_Size("Smoke", "WMAllergy");

	}

	@Test(priority = 934, enabled = true)
	@Description("Validating WM Allergy call Response")
	public void Validate_WMAllergy_Call_Response() throws Exception {
		System.out.println("==============================================");
		System.out.println("****** Validating WM Allergy call response in charles");
		logStep("Validating WM Allergy call response in charles ");

		Utils.Verify_Gampad_Call_ByResponseText("Smoke", "WMAllergy");

	}

}
