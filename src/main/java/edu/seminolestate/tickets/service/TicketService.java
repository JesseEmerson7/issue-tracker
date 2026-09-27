package edu.seminolestate.tickets.service;
import edu.seminolestate.tickets.model.*; import edu.seminolestate.tickets.repository.TicketRepository;
import org.slf4j.*; import org.springframework.stereotype.Service; import java.util.*;
@Service
public class TicketService {
 private static final Logger log=LoggerFactory.getLogger(TicketService.class); private final TicketRepository repo;
 public TicketService(TicketRepository repo){this.repo=repo;}
 public List<Ticket> all(){return repo.findAll();} public Ticket get(long id){return repo.findById(id).orElseThrow(()->new IllegalArgumentException("No ticket with id "+id));}
 public List<Ticket> search(String q){return repo.search(q==null?"":q);}
 public Ticket create(String name,String email,String category,String description){String token=UUID.randomUUID().toString(); Ticket t=repo.save(name,email,category,description,token); log.info("Created support ticket: {}",t); return t;}
 public void changeStatus(long id,String status){repo.updateStatus(id,TicketStatus.valueOf(status));}
}
