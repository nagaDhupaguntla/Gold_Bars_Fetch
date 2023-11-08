import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Properties;

public class HomePage {

    /**
     * Main method to execute Selenium test steps.
     *
     * @param args Command-line arguments (not used in this case).
     */
    @Test
    public static void main(String[] args) {
        // Initialize TESTNGBASE and properties
        TESTNGBASE testBase = new TESTNGBASE();
        Properties prop = testBase.init_properties();

        // Initialize WebDriver using properties
        WebDriver driver = testBase.init_driver(prop);

        // Launch Chrome browser and navigate to the specified URL
        driver.get(prop.getProperty("url"));

        // Instantiate ChallengePage class
        ChallengePage chpage = new ChallengePage(driver);

        // Check labels on the ChallengePage
        chpage.checkLabels();

        // List of bars to check for fake gold bar
        List<Integer> bars = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8);
        int i = 0;

        // Check for the fake gold bar; this method returns the fake gold bar among 9 bars
        int fakebar = chpage.checkFakeGoldBar(bars, i);
        System.out.println("Fakebar is " + fakebar);


        // Close the browser when done
        driver.quit();
    }
}
