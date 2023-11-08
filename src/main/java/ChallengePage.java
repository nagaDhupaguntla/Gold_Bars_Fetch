import io.opentelemetry.api.internal.Utils;
import org.checkerframework.checker.signature.qual.FieldDescriptor;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class ChallengePage {
    private static WebDriver driver;

    @FindBy(className = "status")
    private List<WebElement> boxlabel;

    @FindBy(css = ".result > button")
    private WebElement resLabel;

    @FindBy(xpath = "//button[@id='weigh']/preceding-sibling::button[@id='reset']")
    private WebElement resetBtn;

    @FindBy(id = "weigh")
    private WebElement weighBtn;

    @FindBy(css = ".coins > button")
    private WebElement goldbars;

    @FindBy(xpath = "//div[@class=\"game-info\"]/div'")
    private WebElement weighlabel;


    @FindBy(xpath = "//div[@class='game-info']/ol/li")
    private List<WebElement> weighComparision;

    @FindBy(css = ".board-row>input.square[data-side='left']")
    private List<WebElement> lefttable;

    @FindBy(css = ".board-row>input.square[data-side='right']")
    private List<WebElement> righttable;

    /**
     * Constructor to initialize the ChallengePage class.
     *
     * @param driver The WebDriver instance to be used by this page.
     */
    public ChallengePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Checks the labels of the boxes and returns them as an array.
     *
     * @return An array containing the labels of the left and right boxes.
     */
    public String[] checkLabels() {
        String leftBoxlabel = boxlabel.get(0).getText();
        String rightBoxlabel = boxlabel.get(1).getText();
        String[] labels = new String[2];
        labels[0] = leftBoxlabel;
        labels[1] = rightBoxlabel;

        return labels;

    }
    /**
     * Checks for the fake gold bar among the given bars.
     *
     * @param bars The list of integers representing the bars.
     * @param i The index indicating the starting position in the list of bars.
     * @return The  fake gold bar.
     */
    public int checkFakeGoldBar(List<Integer> bars, int i) {

            //Fakebars will get the 2 bars which among them 1 is fake from the below ebternumberinbowls method
        List<Integer> fakebars = enterNumbersinBowls(bars, i);
        //if it returns only 8 it will return 8 as a fake gold bar or else it will another number with the above return two bars
        if (fakebars.size() == 1) {
            int fakebar = fakebars.get(0);
            return fakebar;
        } else {
            if (fakebars.size() == 2) {
                //add additonal number to bars list to get exact fake bar
                for (int j = 0; j < 9; j++) {
                    if (!fakebars.contains(j)) {
                        fakebars.add(j);
                        break;
                    }
                }
            }
            //resetting the bowls to get the fakebar among 3 bars
            resetBtn.click();
            int fakebar = getFakeNumber(fakebars, i);
            return fakebar;

        }
    }
    //this method will return the actual fakebar among 3
    private int getFakeNumber(List<Integer> fakebars, int i) {

        for (int x = 0; x <= fakebars.size() - 1; x++) {
            int count = 0;
            if (x ==1) {
                return fakebars.get(1);
            }
            for (int j = 0; j <= fakebars.size() - 1; j++) {
                if (x != j) {
                    lefttable.get(fakebars.get(i)).sendKeys(Integer.toString(fakebars.get(x)));

                    righttable.get(fakebars.get(i + 1)).sendKeys(Integer.toString(fakebars.get(j)));

                    clickWeighBtn();
                    waitToCheckWeight();

                    checkWeight();
                    if (resLabel.getText().trim().equals("<")|| resLabel.getText().trim().equals(">")) {
                        count++;
                        resetBtn.click();
                    }
                    if (count == 2) {

                        return fakebars.get(x);
                    }
                }

            }
        }
       return 0;
    }
    /**
     * Waits for a specified duration to check the weight after weighing the bars.
     */
    public void waitToCheckWeight()
    {
        try {
            Thread.sleep(3000); // 5000 milliseconds = 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * Checks the weight comparison result after weighing the bars.
     */
    private void checkWeight() {
        String lastElement = weighComparision.get(weighComparision.size() - 1).getText();
        System.out.println(lastElement);
    }

    private void clickWeighBtn() {
        weighBtn.click();
    }





    /**
     * Recursively enters the numbers in the bowls until the comparison result is found.
     *
     * @param bars The list of integers representing the bars.
     * @param i The index indicating the starting position in the list of bars.
     * @return A list containing the bars to be compared for weighing.
     */

        //Enter the bars in left bowl and right bowl till we get the > grater or < less weights in left or right bowl when we weigh both bowls
    private List enterNumbersinBowls(List<Integer> bars, int i) {
        List<Integer> numlist = new ArrayList<>(2);
        int y=0;
        for (int j = 0; j <= 4; j++) {

            System.out.println(resLabel.getText());
            if (i >= 8 && resetBtn.getText() == "=") {
                numlist.add(i);
                return numlist;
            }

            if (resLabel.getText().equals(">") || resLabel.getText().equals("<")) {
                String splitchar = resLabel.getText();
                String weightList = weighComparision.get(weighComparision.size() - 1).getText();
                List<String> weightbars = List.of(weightList.split(splitchar));
                List<String> leftnum = List.of(weightbars.get(0).replaceAll("\\[|\\]", "").split(","));
                List<String> rightnum = List.of(weightbars.get(1).replaceAll("\\[|\\]", "").split(","));
                // Ensure leftnum and rightnum have at least one element each
                if (!leftnum.isEmpty() && !rightnum.isEmpty()) {
                    try {
                        numlist.add(Integer.parseInt(leftnum.get(leftnum.size() - 1).trim()));
                        numlist.add(Integer.parseInt(rightnum.get(rightnum.size() - 1).trim()));
                    } catch (NumberFormatException e) {
                        // Handle the case where the strings cannot be parsed to integers
                        System.out.println("Invalid number format: " + e.getMessage());
                    }
                    return numlist;
                }

            } else {
                if(i==8){
                    numlist.add(8);
                    return numlist;
                }
                String num = Integer.toString(bars.get(i));
                lefttable.get(bars.get(i)).sendKeys(num);

                System.out.println(i+"printing i");

                i = i + 1;
                String num2 = Integer.toString(bars.get(i));
                righttable.get(bars.get(i)).sendKeys(num2);
                i = i + 1;
                clickWeighBtn();
                waitToCheckWeight();
                    checkWeight();
                    numlist = enterNumbersinBowls(bars, i);
                    return numlist;
                }
            }
            return numlist;
        }
    }

