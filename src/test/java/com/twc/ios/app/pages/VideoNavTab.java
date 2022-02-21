package com.twc.ios.app.pages;

import org.openqa.selenium.By;

import com.twc.ios.app.charlesfunctions.CharlesProxy;
import com.twc.ios.app.general.Driver;
import com.twc.ios.app.general.TestBase;
import com.twc.ios.app.general.Utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.qameta.allure.Step;

public class VideoNavTab extends Utils{
	AppiumDriver<MobileElement> Ad;
	String videoTab_AccessibilityId = "videoDetailTab";
	String winterTab_AccessibilityId = "Winter";

	By byVideoNavTab = MobileBy.AccessibilityId(videoTab_AccessibilityId);
	By byWinterNavTab = MobileBy.AccessibilityId(winterTab_AccessibilityId);
	

	MobileElement videoNavTab = null;
	

	public VideoNavTab(AppiumDriver<MobileElement> Ad) {
		this.Ad = Ad;
	}

	@Step("Navigate To Video Tab")
	public void navigateToVideoTab() throws Exception {
		try {
			videoNavTab = Ad.findElement(byVideoNavTab);
			TestBase.clickOnElement(byVideoNavTab, videoNavTab, "Video Nav Tab");
			TestBase.waitForMilliSeconds(20000);
			System.out.println("Navigated to Video tab ");
			logStep("Navigated to Video tab");
			attachScreen();
		} catch (Exception e) {
			try {
				videoNavTab = Ad.findElement(byWinterNavTab);
				TestBase.clickOnElement(byWinterNavTab, videoNavTab, "Winter Nav Tab");
				TestBase.waitForMilliSeconds(20000);
				System.out.println("Navigated to Winter tab ");
				logStep("Navigated to Winter tab");
				attachScreen();
			} catch (Exception ex) {
				System.out.println("Video tab not displayed");
				logStep("Video tab not displayed");
				attachScreen();
			}
		}

	}
	
	@Step("Navigate To Video Tab: {0}")
	public void navigateToVideoTab(boolean clearCharles, CharlesProxy proxy) throws Exception {
		try {
			videoNavTab = Ad.findElement(byVideoNavTab);
			if (clearCharles) {
				proxy.clearCharlesSession();
			}
			TestBase.clickOnElement(byVideoNavTab, videoNavTab, "Video Nav Tab");
			TestBase.waitForMilliSeconds(20000);
			System.out.println("Navigated to Video tab ");
			logStep("Navigated to Video tab");
			attachScreen();
		} catch (Exception e) {
			try {
				videoNavTab = Ad.findElement(byWinterNavTab);
				if (clearCharles) {
					proxy.clearCharlesSession();
				}
				TestBase.clickOnElement(byWinterNavTab, videoNavTab, "Winter Nav Tab");
				TestBase.waitForMilliSeconds(20000);
				System.out.println("Navigated to Winter tab ");
				logStep("Navigated to Winter tab");
				attachScreen();
			} catch (Exception ex) {
				System.out.println("Video tab not displayed");
				logStep("Video tab not displayed");
				attachScreen();
			}
		}

	}

}
