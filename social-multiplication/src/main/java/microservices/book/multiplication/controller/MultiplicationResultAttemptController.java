package microservices.book.multiplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.service.MultiplicationService;

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
   ResponseEntity<ResultResponse> postResult(@RequestBody MultiplicationResultAttempt multiplicationResultAttempt)
   {
      return ResponseEntity.ok(new ResultResponse(multiplicationService.checkAttempt(multiplicationResultAttempt)));
   }
   
   static final class ResultResponse
   {
      private final boolean m_correct;
      
      public ResultResponse()
      {
         this(false); 
      }
      
      public ResultResponse(boolean correct)
      {
         m_correct = correct; 
      }
      
      public boolean getCorrect()
      {
         return this.m_correct; 
      }
   }
}
