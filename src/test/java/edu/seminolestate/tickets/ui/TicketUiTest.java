package edu.seminolestate.tickets.ui;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.datasource.url=jdbc:h2:mem:ui-test;DB_CLOSE_DELAY=-1")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TicketUiTest {
    @LocalServerPort
    private int port;

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeAll
    void startBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true));
    }

    @BeforeEach
    void openBrowserPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeBrowserPage() {
        context.close();
    }

    @AfterAll
    void stopBrowser() {
        browser.close();
        playwright.close();
    }

    @Test
    void userCanCreateAndViewTicket() {
        page.navigate(url("/tickets/new"));
        page.locator("input[name='requesterName']").fill("Alex Johnson");
        page.locator("input[name='email']").fill("alex@example.com");
        page.locator("select[name='category']").selectOption("Software");
        page.locator("textarea[name='description']")
                .fill("The application crashes when opening a project.");

        page.locator("form button").click();

        assertTrue(page.locator("body").innerText().contains("Alex Johnson"));
        assertTrue(page.locator("body").innerText()
                .contains("The application crashes when opening a project."));
        assertTrue(page.url().contains("token="));
    }

    @Test
    void browserRejectsInvalidTicketInput() {
        page.navigate(url("/tickets/new"));
        page.locator("input[name='requesterName']").fill("Alex Johnson");
        page.locator("input[name='email']").fill("not-an-email");
        page.locator("select[name='category']").selectOption("Software");
        page.locator("textarea[name='description']").fill("Test");

        assertFalse((Boolean) page.locator("input[name='email']")
                .evaluate("element => element.validity.valid"));
        page.locator("form button").click();
        assertTrue(page.url().endsWith("/tickets/new"));
    }

    @Test
    void searchDisplaysMatchingSeededTicket() {
        page.navigate(url("/tickets?q=Wi-Fi"));

        assertTrue(page.locator("body").innerText().contains("Network"));
        assertTrue(page.locator("body").innerText().contains("IN_PROGRESS"));
    }

    @Test
    void validTokenAllowsStatusUpdate() {
        page.navigate(url("/tickets/1?token=sample-token-1001"));
        assertTrue(page.locator("body").innerText().contains("Jordan Lee"));

        page.locator("select[name='status']").selectOption("CLOSED");
        page.locator("form button").click();

        assertTrue(page.locator("body").innerText().contains("CLOSED"));
    }

    @Test
    void incorrectTokenIsDenied() {
        page.navigate(url("/tickets/1?token=wrong-token"));

        assertTrue(page.locator("body").innerText().contains("Ticket access denied"));
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }
}
