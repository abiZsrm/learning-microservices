package lmsb.multiplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
      MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(multiplicationResultAttempt.getUser(), multiplicationResultAttempt.getMultiplication(), multiplicationResultAttempt.getResultAttempt(), correct); 
      return ResponseEntity.ok(attempt);
   }
}
