package com.twc.ios.app.pages;

import org.openqa.selenium.By;

import com.twc.ios.app.charlesfunctions.CharlesProxy;
import com.twc.ios.app.functions.Functions;
import com.twc.ios.app.general.Driver;
import com.twc.ios.app.general.TestBase;
import com.twc.ios.app.general.Utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.qameta.allure.Step;

public class BreakingNewsCardScreen extends Utils {
	AppiumDriver<MobileElement> Ad;

	String breakingNews_AccessibilityId = "breaking_news_item_0_cell_container";
	String breakingNow_AccessibilityId = "breaking-now_item_0_cell_container";
	
	String editorialVideobreakingNews_AccessibilityId = "video-play";
	
	By byEditorialVideobreakingNews = MobileBy.AccessibilityId(editorialVideobreakingNews_AccessibilityId);
	
	MobileElement breakingNews = null;
	MobileElement editorialVideobreakingNews = null;
	
	

	public BreakingNewsCardScreen(AppiumDriver<MobileElement> Ad) {
		this.Ad = Ad;
	}

	@Step("Navigate to Breaking News Details Page")
	public void navigateToBreakingNewsDetailsPage() throws Exception {
		try {
			By byBreakingNews = null;
			try {
				CharlesProxy.proxy.clearCharlesSession();
				byBreakingNews = MobileBy.AccessibilityId(breakingNews_AccessibilityId);
				breakingNews = Ad.findElement(byBreakingNews);
				
				System.out.println("Navigated to Breaking News Details page");
				logStep("Navigated to Breaking News Details page");
				attachScreen();
			} catch (Exception e) {
				CharlesProxy.proxy.clearCharlesSession();
				byBreakingNews = MobileBy.AccessibilityId(breakingNow_AccessibilityId);
				breakingNews = Ad.findElement(byBreakingNews);
				System.out.println("Navigated to Breaking News Details page");
				logStep("Navigated to Breaking News Details page");
				attachScreen();
			}
			TestBase.clickOnElement(byBreakingNews, breakingNews, "Breaking News Item");
			TestBase.waitForMilliSeconds(15000);

		} catch (Exception e) {
			System.out.println("Failed to Navigate to Breaking News Details page");
			logStep("Failed to Navigate to Breaking News Details page");
		}

	}
	
	@Step("Navigate to Editorial Video Headline Card Breaking News Details Page")
	public void navigateToEditorialVideoHeadlineCardBreakingNewsDetailsPage() throws Exception {
		try {
			CharlesProxy.proxy.clearCharlesSession();
			
			editorialVideobreakingNews = Ad.findElement(byEditorialVideobreakingNews);

			System.out.println("Navigated to Breaking News Details page");
			logStep("Navigated to Breaking News Details page");
			attachScreen();

			TestBase.clickOnElement(byEditorialVideobreakingNews, editorialVideobreakingNews, "Editorial Video Headline Card Breaking News Item");
			TestBase.waitForMilliSeconds(15000);

		} catch (Exception e) {
			System.out.println("Failed to Navigate to Editorial Video Headline Card Breaking News Details page");
			logStep("Failed to Navigate to Editorial Video Headline Card Breaking News Details page");
		}

	}

}
