package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.Constant;
import com.productiveedge.content_mgmt_automation.entity.FolderName;
import com.productiveedge.content_mgmt_automation.entity.request.TakeScreenshotRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper;
import com.productiveedge.content_mgmt_automation.repository.container.impl.PageContainer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
import java.util.stream.Stream;

public class TakeScreenshotFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(TakeScreenshotFlow.class);

    private static final String CHROME_PROPERTY = "webdriver.chrome.driver";
    private static final String WINDOW_SCROLL_BY_JS_COMMAND = "window.scrollBy(0,?)";
    private static final String RETURN_DOCUMENT_BODY_SCROLL_HEIGHT_JS_COMMAND = "return document.body.scrollHeight";
    private static final String[] CHROME_ARGUMENT_OPTIONS = {
            "--incognito",
            "--start-maximized",
            "--kiosk"
    };

    private WebDriver driver;
    private final TakeScreenshotRequest request;
    private final PageContainer pageContainer;

    public TakeScreenshotFlow(TakeScreenshotRequest request) throws InvalidJarRequestException {
        this.request = request;
        this.pageContainer = PageContainer.getInstance();
        System.setProperty(CHROME_PROPERTY, request.getDriverPath());
        String browser = request.getBrowserName().toUpperCase();
        switch (browser) {
            case "CHROME":
                ChromeOptions options = new ChromeOptions();
                Stream.of(CHROME_ARGUMENT_OPTIONS).forEach(options::addArguments);
                DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                driver = new ChromeDriver(options);
                break;
            case "SAFARI":
                break;
            case "FIREFOX":
                break;
            default:
                throw new InvalidJarRequestException("Incorrect browser name value.");
        }
    }

    @Override
    public synchronized void run() {
        pageContainer.getProcessedPageEntries().forEach(e -> {
            String url = e.getValue().getUrl();
            try {
                driver.get(url);
                TakesScreenshot ts = (TakesScreenshot) driver;
                if (driver instanceof JavascriptExecutor) {
                    JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
                    int pageHeight = Integer.parseInt(jsDriver.executeScript(RETURN_DOCUMENT_BODY_SCROLL_HEIGHT_JS_COMMAND).toString());
                    int pageScrollValue = Integer.valueOf(request.getPageScrollValue());
                    int amountScreens = pageHeight / pageScrollValue + 1;
                    String dateFolderName = Constant.generateDate();
                    String domainFolderName = GrabAllLinksHelper.generateNameByKey(e.getKey());
                    for (int i = 0; i < amountScreens; i++) {
                        File source = ts.getScreenshotAs(OutputType.FILE);
                        String fileName = (i + 1) + ".png";
                        File destination = new File(Paths.get(request.getRootFolderPath(), FolderName.SCREEN.name(), dateFolderName, domainFolderName, fileName).toString());
                        FileUtils.copyFile(source, destination);
                        jsDriver.executeScript(WINDOW_SCROLL_BY_JS_COMMAND.replaceFirst("[?]", request.getPageScrollValue()));
                    }
                    logger.info("Screenshots of site " + e.getValue().getUrl() + " are taken");
                } else {
                    logger.error("Driver can't takes screenshots. Please, change to another one driver type.");
                }
            } catch (IOException ex) {
                logger.error("Can't take screenshot by processUrl " + url + "." + IOUtils.LINE_SEPARATOR + ex.getMessage());
            }
        });
        driver.quit();
    }
}
