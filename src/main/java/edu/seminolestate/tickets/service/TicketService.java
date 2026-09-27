package edu.seminolestate.tickets.service;
import edu.seminolestate.tickets.model.*; import edu.seminolestate.tickets.repository.TicketRepository;
import org.slf4j.*; import org.springframework.stereotype.Service; import java.util.*; import java.nio.charset.StandardCharsets; import java.security.MessageDigest;
@Service
public class TicketService {
 private static final Logger log=LoggerFactory.getLogger(TicketService.class); private final TicketRepository repo;
 public TicketService(TicketRepository repo){this.repo=repo;}
 public List<Ticket> all(){return repo.findAll();}
 public Ticket getAuthorized(long id,String token){Ticket ticket=repo.findById(id).orElseThrow(()->new NoSuchElementException("Ticket not found"));if(token==null||!MessageDigest.isEqual(ticket.accessToken().getBytes(StandardCharsets.UTF_8),token.getBytes(StandardCharsets.UTF_8)))throw new SecurityException("Ticket access denied");return ticket;}
 public List<Ticket> search(String q){return repo.search(q==null?"":q);}
 public Ticket create(String name,String email,String category,String description){if(!Set.of("Software","Network","Account","Other").contains(category))throw new IllegalArgumentException("Invalid category");String token=UUID.randomUUID().toString();Ticket t=repo.save(name,email,category,description,token);log.info("Created support ticket id={}",t.id());return t;}
 public void changeStatus(long id,String token,String status){getAuthorized(id,token);try{repo.updateStatus(id,TicketStatus.valueOf(status));}catch(IllegalArgumentException ex){throw new IllegalArgumentException("Invalid status");}}
}
