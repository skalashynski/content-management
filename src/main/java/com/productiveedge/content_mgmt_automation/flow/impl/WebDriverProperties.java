package com.productiveedge.content_mgmt_automation.flow.impl;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

public class WebDriverProperties {

    public static final String CAPABILITY_BROWSER = "browser";
    public static final String CAPABILITY_BROWSER_NAME = "browserName";
    public static final String CAPABILITY_DEVICE = "device";
    public static final String CAPABILITY_DEVICE_NAME = "deviceName";
    public static final String CAPABILITY_AUTOCLOSE = "autoclose";
    public static final String CAPABILITY_DRIVER_VERSION = "driverVersion";
    public static final String CAPABILITY_REMOTE = "remote";
    public static final String CAPABILITY_BROWSER_SIZE = "browserSize";
    public static final String CAPABILITY_VIEWPORT_SIZE = "viewportSize";
    public static final String CAPABILITY_USER_AGENT = "userAgent";
    public static final String CAPABILITY_PIXEL_RATIO = "pixelRatio";
    public static final String CAPABILITY_HEADLESS = "headless";
    private final Map<String, String> properties = new HashMap<>();

    public WebDriverProperties() {
        // no-arg constructor
    }

    public WebDriverProperties(Map<String, String> rawMap) {
        properties.putAll(rawMap);
    }

    public void setProperty(String property, String value) {
        properties.put(property, value);
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public Map<String, String> asMap() {
        return properties;
    }

    public String getBrowser() {
        return Optional.ofNullable(getProperty(CAPABILITY_BROWSER))
                .orElse(Optional.ofNullable(getProperty(CAPABILITY_BROWSER_NAME))
                        .orElse(Optional.ofNullable(System.getProperty(CAPABILITY_BROWSER))
                                .orElse("chrome")));
    }

    public String getBrowserSize() {
        return Optional.ofNullable(getProperty(CAPABILITY_BROWSER_SIZE))
                .orElse(StringUtils.EMPTY);
    }

    public String getViewportSize() {
        return Optional.ofNullable(getProperty(CAPABILITY_VIEWPORT_SIZE))
                .orElse(StringUtils.EMPTY);
    }

    public boolean isAutoClose() {
        return Boolean.valueOf(Optional.ofNullable(getProperty(CAPABILITY_AUTOCLOSE))
                .orElse("false"));
    }

    public boolean isHeadless() {
        return Boolean.valueOf(Optional.ofNullable(getProperty(CAPABILITY_HEADLESS))
                .orElse("false"));
    }

    public String getRemote() {
        return Optional.ofNullable(getProperty(CAPABILITY_REMOTE))
                .orElse(StringUtils.EMPTY);
    }

    public String getDeviceName() {
        return Optional.ofNullable(getProperty(CAPABILITY_DEVICE))
                .orElse(Optional.ofNullable(getProperty(CAPABILITY_DEVICE_NAME))
                        .orElse(Optional.ofNullable(System.getProperty(CAPABILITY_DEVICE))
                                .orElse(StringUtils.EMPTY)));
    }

    public String getDriverVersion() {
        return Optional.ofNullable(getProperty(CAPABILITY_DRIVER_VERSION))
                .orElse(StringUtils.EMPTY);
    }

    public String getUserAgent() {
        return Optional.ofNullable(getProperty(CAPABILITY_USER_AGENT))
                .orElse(StringUtils.EMPTY);
    }

    public double getPixelRatio() {
        return NumberUtils.toDouble(getProperty(CAPABILITY_PIXEL_RATIO));
    }

    public DriverType getDriverType() {
        return isNotEmpty(getRemote()) ? DriverType.REMOTE :
                DriverType.valueOf(getBrowser().toUpperCase());
    }

   /* public WebDriverProperties validate() {
        // validate CAPABILITY_BROWSER when testing in local
        if(isEmpty(getRemote())) {
            DriverType browserType = EnumUtils.getEnum(DriverType.class, getBrowser().toUpperCase());
            checkArgument(DriverType.local.contains(browserType),
                    String.format("Invalid [capabilities.browser, capabilities.browserName, browser] " +
                            "= [%s] not in %s", getBrowser(), DriverType.local.toString()));
        }
        // validate CAPABILITY_BROWSER_SIZE
        if(isNotEmpty(getBrowserSize())) {
            checkArgument(getBrowserSize().matches("\\d+[x]\\d+"),
                    String.format("Invalid [capabilities.browserSize] = [%s] " +
                                    "not in the format 123x456",
                            getBrowserSize()));
        }
        // validate CAPABILITY_VIEWPORT_SIZE
        if(isNotEmpty(getViewportSize())) {
            checkArgument(getViewportSize().matches("\\d+[x]\\d+"),
                    String.format("Invalid [capabilities.viewportSize] = [%s] " +
                                    "not in the format 123x456",
                            getViewportSize()));
        }
        // validate CAPABILITY_DEVICE_NAME over CAPABILITY_USER_AGENT
        if(isNotEmpty(getDeviceName()) && isNotEmpty(getUserAgent())) {
            throw new IllegalArgumentException(
                    "Invalid capabilities setup:" +
                            " [capabilities.deviceName] and [capabilities.userAgent] cannot coexist.");
        }
        // validate CAPABILITY_REMOTE
        if(isNotEmpty(getRemote())) {
            try {
                new URL(getRemote());
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(
                        "Invalid 'capabilities.remote' parameter: " + getRemote(), e);
            }
        }
        // all the properties are valid
        return this;
    }*/
}