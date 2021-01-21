package lmsb.multiplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lmsb.multiplication.domain.Multiplication;
import lmsb.multiplication.service.MultiplicationService;

@RestController
@RequestMapping("/multiplications")
public class MultiplicationController
{
   private final MultiplicationService m_multiplicationService;

   @Autowired
   public MultiplicationController( MultiplicationService multiplicationService)
   {
      this.m_multiplicationService = multiplicationService;
   }

   @GetMapping("/random")
   Multiplication getRandomMultiplication()
   {
      return this.m_multiplicationService.createRandomMultiplication();
   }
}
