package edu.seminolestate.tickets.service;
import edu.seminolestate.tickets.model.*; import edu.seminolestate.tickets.repository.TicketRepository;
import org.junit.jupiter.api.*; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.*; import java.util.*;
import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.*;
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TicketServiceTest {
 @Mock TicketRepository repo; @InjectMocks TicketService service;
 @Test void retrievesExistingTicket(){var t=new Ticket(1L,"Jordan Lee","jlee@example.edu","Software","IDE issue",TicketStatus.OPEN,"token");when(repo.findById(1L)).thenReturn(Optional.of(t));assertEquals("Jordan Lee",service.getAuthorized(1L,"token").requesterName());}
 @Test void changesTicketStatus(){var t=new Ticket(2L,"Jordan Lee","jlee@example.edu","Software","IDE issue",TicketStatus.OPEN,"token");when(repo.findById(2L)).thenReturn(Optional.of(t));service.changeStatus(2L,"token","CLOSED");verify(repo).updateStatus(2L,TicketStatus.CLOSED);}
 @Test void searchesForKnownTicket(){when(repo.search("Wi-Fi")).thenReturn(List.of(new Ticket(2L,"Morgan Diaz","mdiaz@example.edu","Network","Cannot connect to campus Wi-Fi",TicketStatus.IN_PROGRESS,"token")));assertEquals(1,service.search("Wi-Fi").size());}
}
