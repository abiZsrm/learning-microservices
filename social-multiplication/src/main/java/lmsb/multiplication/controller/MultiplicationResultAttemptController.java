package lmsb.multiplication.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lmsb.multiplication.domain.MultiplicationResultAttempt;
import lmsb.multiplication.service.MultiplicationService;

@RestController
@RequestMapping("/results")
public class MultiplicationResultAttemptController
{
   private final MultiplicationService multiplicationService;
   
   @Autowired
   MultiplicationResultAttemptController( final MultiplicationService multiplicationService )
   {
      this.multiplicationService = multiplicationService;
   }
   
   @PostMapping
   ResponseEntity<MultiplicationResultAttempt> postResult(@RequestBody MultiplicationResultAttempt multiplicationResultAttempt)
   {
      boolean correct = multiplicationService.checkAttempt(multiplicationResultAttempt); 
      MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(multiplicationResultAttempt.getUser(), multiplicationResultAttempt.getMultiplication(), multiplicationResultAttempt.getResultAttempt(), correct, multiplicationResultAttempt.getId()); 
      return ResponseEntity.ok(attempt);
   }
   
   @GetMapping
   ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(@RequestParam("alias") String alias) throws SQLException {
       return ResponseEntity.ok(
               multiplicationService.getStatsForUser(alias)
       );
   }
}
