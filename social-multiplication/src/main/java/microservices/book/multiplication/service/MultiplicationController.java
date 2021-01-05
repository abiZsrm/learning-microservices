package microservices.book.multiplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.multiplication.domain.Multiplication; 

@RestController
@RequestMapping("/multiplications")
public class MultiplicationController
{
   private final MultiplicationService m_multiplicationService; 
   
   @Autowired
   public MultiplicationController(MultiplicationService multiplicationService)
   {
      this.m_multiplicationService = multiplicationService; 
   }
   
   @GetMapping("/random")
   Multiplication getRandomMultiplication() {
   return this.m_multiplicationService.createRandomMultiplication();
   }
}
