package org.apache.nifi.registry.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ITRenameBucketDuplicate {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private WebDriverWait wait;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        baseUrl = "http://localhost:18080/nifi-registry";
        wait = new WebDriverWait(driver, 30);
    }

    @Test
    public void testRenameBucketDuplicate() throws Exception {
        // go directly to settings by URL
        driver.get(baseUrl + "/administration/workflow");

        // wait for administration route to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-automation-id='no-buckets-message']")));

        // confirm new bucket button exists
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-automation-id='new-bucket-button']")));

        // select new bucket button
        WebElement newBucketButton = driver.findElement(By.cssSelector("[data-automation-id='new-bucket-button']"));
        newBucketButton.click();

        // wait for new bucket dialog
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nifi-registry-admin-create-bucket-dialog")));

        // confirm bucket name field exists
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nifi-registry-admin-create-bucket-dialog input")));

        // place cursor in bucket name field
        WebElement bucketNameInput = driver.findElement(By.cssSelector("#nifi-registry-admin-create-bucket-dialog input"));
        bucketNameInput.clear();

        // name the bucket ABC
        bucketNameInput.sendKeys("ABC");

        // confirm create bucket button exists
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-automation-id='create-new-bucket-button']")));

        // select create bucket button
        WebElement createNewBucketButton = driver.findElement(By.cssSelector("[data-automation-id='create-new-bucket-button']"));
        createNewBucketButton.click();

        // wait for create bucket dialog to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#nifi-registry-admin-create-bucket-dialog")));

        // verify bucket added
        List<WebElement> bucketCount = driver.findElements(By.cssSelector("#nifi-registry-workflow-administration-buckets-list-container > div"));
        assertEquals(1, bucketCount.size());

        // confirm new bucket button exists
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-automation-id='new-bucket-button']")));

        // select new bucket button
        newBucketButton = driver.findElement(By.cssSelector("[data-automation-id='new-bucket-button']"));
        newBucketButton.click();

        // wait for new bucket dialog
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nifi-registry-admin-create-bucket-dialog")));

        // confirm bucket name field exists
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nifi-registry-admin-create-bucket-dialog input")));

        // place cursor in bucket name field
        bucketNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nifi-registry-admin-create-bucket-dialog input")));
        bucketNameInput.clear();

        // name the bucket DEF
        bucketNameInput.sendKeys("DEF");

        // confirm create bucket button exists
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-automation-id='create-new-bucket-button']")));

        // select create bucket button
        createNewBucketButton = driver.findElement(By.cssSelector("[data-automation-id='create-new-bucket-button']"));
        createNewBucketButton.click();

        // wait for new bucket dialog to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#nifi-registry-admin-create-bucket-dialog")));

        // verify bucket added
        bucketCount = driver.findElements(By.cssSelector("#nifi-registry-workflow-administration-buckets-list-container > div"));
        assertEquals(2, bucketCount.size());

        // confirm pencil icon button exists
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fa-pencil")));

        // select pencil icon for ABC bucket
        WebElement manageBucketButton = driver.findElement(By.className("fa-pencil"));
        manageBucketButton.click();

        // confirm bucket name field exists
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-automation-id='nf-registry-manage-bucket-input-name']")));

        // place cursor in Bucket Name Field
        WebElement identityBucketNameInput = driver.findElement(By.cssSelector("[data-automation-id='nf-registry-manage-bucket-input-name']"));
        identityBucketNameInput.clear();

        // name the bucket DEF
        identityBucketNameInput.sendKeys("DEF");

        // confirm save button exists
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-automation-id='nf-registry-manage-bucket-save-side-nav']")));

        // select save button
        WebElement saveBucketButton = driver.findElement(By.cssSelector("[data-automation-id='nf-registry-manage-bucket-save-side-nav']"));
        saveBucketButton.click();

        // wait for duplicate bucket error dialog
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.cdk-overlay-pane")));

         // select OK button
        WebElement selectOKButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("fds-confirm-dialog > fds-dialog > div > div.fds-dialog-actions.ng-star-inserted > fds-dialog-actions > button")));
        Actions actions = new Actions(driver);
        actions.moveToElement(selectOKButton).click().build().perform();

        // wait for the confirm dialog to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.cdk-overlay-pane")));

        // close side nav via "x"
        WebElement closeSideNavX = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-automation-id='nf-registry-manage-bucket-close-side-nav']")));
        actions.moveToElement(closeSideNavX).click().build().perform();

        // wait for side nav to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[data-automation-id='nf-registry-manage-bucket-close-side-nav']")));

        // verify both buckets exists
        bucketCount = driver.findElements(By.cssSelector("#nifi-registry-workflow-administration-buckets-list-container > div"));
        assertEquals(2, bucketCount.size());
    }

    @After
    public void tearDown() throws Exception {
        // wait for side nav
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("mat-sidenav")));

        // bucket cleanup
        // select all buckets checkbox
        WebElement selectAllCheckbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nifi-registry-workflow-administration-buckets-list-container-column-header div.mat-checkbox-inner-container")));
        selectAllCheckbox.click();

        // select actions drop down
        WebElement selectActions = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nifi-registry-workflow-administration-perspective-buckets-container button.mat-fds-primary")));
        selectActions.click();

        // select delete
        WebElement selectDeleteBucket = driver.findElement(By.cssSelector("div.mat-menu-content button.mat-menu-item"));
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", selectDeleteBucket);

        // verify bucket deleted
        WebElement confirmDelete = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.fds-dialog-actions button.mat-fds-warn")));
        confirmDelete.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-automation-id='no-buckets-message']")));

        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}