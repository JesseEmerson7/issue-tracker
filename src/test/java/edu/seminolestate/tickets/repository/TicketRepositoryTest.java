package edu.seminolestate.tickets.repository;

import edu.seminolestate.tickets.model.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:repository-test;DB_CLOSE_DELAY=-1")
class TicketRepositoryTest {
    @Autowired
    private TicketRepository repository;

    @Test
    void searchTreatsSqlSyntaxAsText() {
        List<Ticket> results = repository.search("' OR '1'='1");

        assertTrue(results.isEmpty());
    }
}
