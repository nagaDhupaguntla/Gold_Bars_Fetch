import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TESTNGBASE {

    WebDriver driver;
    Properties prop;

    /**
     * Initializes the WebDriver based on the browser specified in the properties file.
     *
     * @param prop The Properties object containing configuration details.
     * @return Initialized WebDriver instance.
     */
    public WebDriver init_driver(Properties prop) {
        String browser = prop.getProperty("browser");
        LoggerUtil.info("Setting up the test environment");
        if (browser.equals("chrome")) {
            // Setup Chrome WebDriver using WebDriverManager
            WebDriverManager.chromedriver().setup();
            // Create ChromeOptions object
            ChromeOptions options = new ChromeOptions();
            // Add the command-line argument
            options.addArguments("--remote-allow-origins=*");
            // Initialize ChromeDriver with options
            driver = new ChromeDriver(options);
        } else if (browser.equals("firefox")) {
            // Setup Firefox WebDriver using WebDriverManager
            WebDriverManager.firefoxdriver().setup();
            // Initialize FirefoxDriver
            driver = new FirefoxDriver();
        } else {
            System.out.println("Please provide a proper browser value..");
        }

        // Maximize the browser window and delete all cookies
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        return driver;
    }

    /**
     * Initializes and loads properties from the configuration file.
     *
     * @return Properties object containing configuration details.
     */
    public Properties init_properties() {
        prop = new Properties();
        try {
            // Load properties from the configuration file
            FileInputStream ip = new FileInputStream("src/main/resources/config.properties");
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
