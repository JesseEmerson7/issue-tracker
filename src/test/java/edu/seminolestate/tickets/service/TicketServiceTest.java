package edu.seminolestate.tickets.service;
import edu.seminolestate.tickets.model.*; import edu.seminolestate.tickets.repository.TicketRepository;
import ch.qos.logback.classic.spi.ILoggingEvent; import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.*; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.*; import java.util.*;
import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.*;
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TicketServiceTest {
 @Mock TicketRepository repo; @InjectMocks TicketService service;
 @Test void retrievesExistingTicket(){var t=new Ticket(1L,"Jordan Lee","jlee@example.edu","Software","IDE issue",TicketStatus.OPEN,"token");when(repo.findById(1L)).thenReturn(Optional.of(t));assertEquals("Jordan Lee",service.getAuthorized(1L,"token").requesterName());}
 @Test void rejectsIncorrectAccessToken(){var t=new Ticket(1L,"Jordan Lee","jlee@example.edu","Software","IDE issue",TicketStatus.OPEN,"token");when(repo.findById(1L)).thenReturn(Optional.of(t));assertThrows(SecurityException.class,()->service.getAuthorized(1L,"wrong-token"));}
 @Test void changesTicketStatus(){var t=new Ticket(2L,"Jordan Lee","jlee@example.edu","Software","IDE issue",TicketStatus.OPEN,"token");when(repo.findById(2L)).thenReturn(Optional.of(t));service.changeStatus(2L,"token","CLOSED");verify(repo).updateStatus(2L,TicketStatus.CLOSED);}
 @Test void rejectsInvalidStatus(){var t=new Ticket(2L,"Jordan Lee","jlee@example.edu","Software","IDE issue",TicketStatus.OPEN,"token");when(repo.findById(2L)).thenReturn(Optional.of(t));assertThrows(IllegalArgumentException.class,()->service.changeStatus(2L,"token","UNKNOWN"));verify(repo,never()).updateStatus(anyLong(),any());}
 @Test void rejectsInvalidCategory(){assertThrows(IllegalArgumentException.class,()->service.create("Jordan Lee","jlee@example.edu","Invalid","IDE issue"));verify(repo,never()).save(anyString(),anyString(),anyString(),anyString(),anyString());}
 @Test void doesNotLogTicketPersonalDataOrToken(){
  var ticket=new Ticket(3L,"Jordan Lee","jlee@example.edu","Software","Private details",TicketStatus.OPEN,"secret-token");
  when(repo.save(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(ticket);
  var logger=(ch.qos.logback.classic.Logger)org.slf4j.LoggerFactory.getLogger(TicketService.class);
  var appender=new ListAppender<ILoggingEvent>();appender.start();logger.addAppender(appender);
  try{service.create("Jordan Lee","jlee@example.edu","Software","Private details");String message=appender.list.get(0).getFormattedMessage();assertTrue(message.contains("3"));assertFalse(message.contains("Jordan Lee"));assertFalse(message.contains("jlee@example.edu"));assertFalse(message.contains("secret-token"));}finally{logger.detachAppender(appender);}
 }
 @Test void searchesForKnownTicket(){when(repo.search("Wi-Fi")).thenReturn(List.of(new Ticket(2L,"Morgan Diaz","mdiaz@example.edu","Network","Cannot connect to campus Wi-Fi",TicketStatus.IN_PROGRESS,"token")));assertEquals(1,service.search("Wi-Fi").size());}
}
