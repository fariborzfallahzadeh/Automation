package Academy;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.net.HttpURLConnection;
import java.net.URL;

public class BrowserTest {

    @Test
    public void getData() {
        System.out.println("🔹 شروع تست Selenium");

        try {
            // بررسی در دسترس بودن صفحه قبل از اجرای تست
            String testUrl = "https://srv-webapp.azurewebsites.net/webapp/";
            if (!isUrlAccessible(testUrl)) {
                System.out.println("⚠️ خطا: صفحه در دسترس نیست. تست متوقف شد.");
                return;
            }

            // دانلود و تنظیم اتوماتیک ChromeDriver
            WebDriverManager.chromedriver().setup();

            // تنظیمات Chrome برای اجرای Headless در CI/CD
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");  // بدون UI گرافیکی
            options.addArguments("--no-sandbox");  // حل مشکل دسترسی در محیط‌های CI
            options.addArguments("--disable-dev-shm-usage");  // حل مشکل منابع در سرورهای CI

            // مقداردهی WebDriver با تنظیمات جدید
            WebDriver driver = new ChromeDriver(options);

            // باز کردن صفحه وب
            System.out.println("🌐 باز کردن صفحه: " + testUrl);
            driver.get(testUrl);

            // دریافت متن h1
            String text = driver.findElement(By.cssSelector("h1")).getText();
            System.out.println("📌 متن استخراج‌شده: " + text);

            // بررسی مقدار متنی
            assertTrue("⚠️ خطا: متن هدر مطابقت ندارد!", text.equalsIgnoreCase("Author Fariborz Fallahzadeh"));

            // بستن مرورگر
            driver.quit();
            System.out.println("✅ تست با موفقیت انجام شد!");

        } catch (Exception e) {
            System.err.println("🚨 خطای غیرمنتظره: " + e.getMessage());
        }
    }

    // متد برای بررسی دسترسی به URL قبل از اجرا
    private boolean isUrlAccessible(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (Exception e) {
            return false;
        }
    }
}
