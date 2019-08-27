package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.AppChromeDriverMain;
import com.productiveedge.content_mgmt_automation.entity.FolderName;
import com.productiveedge.content_mgmt_automation.entity.request.TakeScreenshotRequest;
import com.productiveedge.content_mgmt_automation.entity.response.TakeScreenshotResponse;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.repository.PageContainer;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class TakeScreenshotFlow implements Flow<TakeScreenshotResponse> {
    private static final Logger logger = LoggerFactory.getLogger(TakeScreenshotFlow.class);
    private static final String PROPERTY = "webdriver.chrome.driver";
    private static final String CHROMEDRIVER_NAME = "chromedriver.exe";
    private static final String CHROME_DRIVER_PATH = AppChromeDriverMain.class.getClassLoader().getResource(CHROMEDRIVER_NAME).getPath();
    private static final String JAVASCRIPT_COMMAND = "window.scrollBy(0,?)";
    private static final String GET_HTML_PAGE_HEIGHT_SCRIPT = "return document.body.scrollHeight";
    //private String PAGE_SCROLL_VALUE = "";

    private WebDriver driver;
    private TakeScreenshotRequest request;

    public TakeScreenshotFlow(TakeScreenshotRequest request) {
        this.request = request;
        System.setProperty(PROPERTY, CHROME_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--start-maximized");
        options.addArguments("--kiosk");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);


        //тут, в зависимости от request-а нужно инициализировтаь драйвер (Chrome /Safari)
        driver = new ChromeDriver(options);
    }

    @Override
    public synchronized TakeScreenshotResponse run() {
        PageContainer.getAllProcessedWebsiteLinks().forEach(e -> {
            String url = e.getValue().getUrl();
            try {
                driver.get(url);
                TakesScreenshot ts = (TakesScreenshot) driver;
                if (driver instanceof JavascriptExecutor) {
                    JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
                    int pageHeight = Integer.parseInt(jsDriver.executeScript(GET_HTML_PAGE_HEIGHT_SCRIPT).toString());
                    int pageScrollValue = Integer.valueOf(request.getPageScrollValue());
                    int amountScreens = pageHeight / pageScrollValue + 1;
                    String domainFolderName = e.getKey().replaceAll("\\.", "_");
                    for (int i = 0; i < amountScreens; i++) {
                        File source = ts.getScreenshotAs(OutputType.FILE);
                        String fileName = (i + 1) + ".png";
                        File destination = new File(Paths.get(request.getRootFolderPath(), FolderName.SCREEN.name(), domainFolderName, fileName).toString());
                        FileUtils.copyFile(source, destination);
                        jsDriver.executeScript(JAVASCRIPT_COMMAND.replaceFirst("[?]", request.getPageScrollValue()));
                    }
                    logger.info("Screenshots of site " + e.getValue().getUrl() + " are taken");
                }
            } catch (IOException ex) {
                logger.error("Can't take screenshot by url " + url + ".\n" + ex.getMessage());
                //переделать
                System.out.println(ex.getMessage());
            }
        });
        driver.quit();
        //переделать
        return null;
    }
}
