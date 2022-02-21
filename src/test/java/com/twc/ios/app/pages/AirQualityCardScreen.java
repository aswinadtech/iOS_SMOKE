package com.twc.ios.app.pages;

import org.openqa.selenium.By;

import com.twc.ios.app.functions.Functions;
import com.twc.ios.app.general.Driver;
import com.twc.ios.app.general.TestBase;
import com.twc.ios.app.general.Utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.qameta.allure.Step;

public class AirQualityCardScreen extends Utils {
	AppiumDriver<MobileElement> Ad;
	String aQCard_AccessibilityId = "air-quality-card_detailedButton";
	String closeLight_AccessibilityId = "close light";
	
	By byAQCard = MobileBy.AccessibilityId(aQCard_AccessibilityId);
	By byCloseLight = MobileBy.AccessibilityId(closeLight_AccessibilityId);
	
	

	MobileElement aQCard = null;
	MobileElement closeLight = null;

	public AirQualityCardScreen(AppiumDriver<MobileElement> Ad) {
		this.Ad = Ad;
	}

	@Step("Navigate To AirQuality Card Content Page")
	public void navigateToAirQualityCardContentPage() throws Exception {
		aQCard = Ad.findElement(byAQCard);
		TestBase.clickOnElement(byAQCard, aQCard, "AQ Card Details");
		TestBase.waitForMilliSeconds(6000);
		try {
			closeLight = Ad.findElement(byCloseLight);
			TestBase.clickOnElement(byCloseLight, closeLight, "Siri PopUp");
			TestBase.waitForMilliSeconds(4000);
		} catch (Exception e) {
			System.out.println("Siri popup not present validating aq page ad");
			logStep("Siri popup not present validating aq page ad");
		}
		attachScreen();
		navigateBackToFeedCard();
		if (unlimitedInterstitial) {
			handle_Interstitial_Ad();
		}
	}
	
	@Step("Navigate To AirQuality Card Content Page And Not to Handle Interstitials")
	public void navigateToAirQualityCardContentPageAndDontHandleInterstitials() throws Exception {
		aQCard = Ad.findElement(byAQCard);
		TestBase.clickOnElement(byAQCard, aQCard, "AQ Card Details");
		TestBase.waitForMilliSeconds(6000);
		try {
			closeLight = Ad.findElement(byCloseLight);
			TestBase.clickOnElement(byCloseLight, closeLight, "Siri PopUp");
			TestBase.waitForMilliSeconds(4000);
		} catch (Exception e) {
			System.out.println("Siri popup not present validating aq page ad");
			logStep("Siri popup not present validating aq page ad");
		}
		attachScreen();
	}

}
