package edu.seminolestate.tickets.controller;
import edu.seminolestate.tickets.service.TicketService; import jakarta.validation.constraints.*; import org.slf4j.*; import org.springframework.http.*; import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.web.bind.annotation.*; import org.springframework.validation.annotation.Validated;
import java.util.NoSuchElementException;
@Controller
@Validated
public class TicketController {
 private static final Logger log=LoggerFactory.getLogger(TicketController.class); private final TicketService service; public TicketController(TicketService service){this.service=service;}
 @GetMapping("/") public String home(){return "redirect:/tickets";}
 @GetMapping("/tickets") public String tickets(@RequestParam(required=false) String q,Model m){m.addAttribute("tickets",q==null?service.all():service.search(q));m.addAttribute("q",q);return "tickets";}
 @GetMapping("/tickets/new") public String form(){return "new-ticket";}
 @PostMapping("/tickets") public String create(@RequestParam @NotBlank @Size(max=255) String requesterName,@RequestParam @NotBlank @Email @Size(max=255) String email,@RequestParam @NotBlank @Size(max=100) String category,@RequestParam @NotBlank @Size(max=4000) String description){var t=service.create(requesterName.trim(),email.trim(),category,description.trim());return "redirect:/tickets/"+t.id()+"?token="+t.accessToken();}
 @GetMapping("/tickets/{id}") public String one(@PathVariable long id,@RequestParam String token,Model m){m.addAttribute("ticket",service.getAuthorized(id,token));m.addAttribute("token",token);return "ticket";}
 @PostMapping("/tickets/{id}/status") public String status(@PathVariable long id,@RequestParam String token,@RequestParam String status){service.changeStatus(id,token,status);return "redirect:/tickets/"+id+"?token="+token;}
 @ExceptionHandler(NoSuchElementException.class) @ResponseStatus(HttpStatus.NOT_FOUND) @ResponseBody public String notFound(){return "Ticket not found";}
 @ExceptionHandler(SecurityException.class) @ResponseStatus(HttpStatus.FORBIDDEN) @ResponseBody public String forbidden(){return "Ticket access denied";}
 @ExceptionHandler({IllegalArgumentException.class,org.springframework.web.bind.MethodArgumentNotValidException.class,org.springframework.web.method.annotation.HandlerMethodValidationException.class,jakarta.validation.ConstraintViolationException.class}) @ResponseStatus(HttpStatus.BAD_REQUEST) @ResponseBody public String badRequest(){return "Invalid request";}
 @ExceptionHandler(Exception.class) @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) @ResponseBody public String error(Exception ex){log.error("Unhandled application error",ex);return "An unexpected error occurred";}
}
