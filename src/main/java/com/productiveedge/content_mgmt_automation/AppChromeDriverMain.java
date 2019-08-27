package com.productiveedge.content_mgmt_automation;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;

public class AppChromeDriverMain {
    private static final String CHROMEDRIVER_NAME = "chromedriver.exe";
    public static void main(String[] args) throws Exception{
        final String path = AppChromeDriverMain.class.getClassLoader ()
                .getResource (CHROMEDRIVER_NAME)
                .getPath ();


        System.setProperty("webdriver.chrome.driver", path);


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--start-maximized");
        options.addArguments("--kiosk");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);



        //тут, в зависимости от request-а нужно инициализировтаь драйвер

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.productiveedge.com");
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String dest = "C:\\Users\\Сергей\\screenshotName.png";
            File destination = new File(dest);
            FileUtils.copyFile(source, destination);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
