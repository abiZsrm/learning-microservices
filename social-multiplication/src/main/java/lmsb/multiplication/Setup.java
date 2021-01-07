package lmsb.multiplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lmsb.multiplication.service.impl.MultiplicationServiceImpl;
import lmsb.multiplication.service.impl.RandomGeneratorServiceImpl;

@Configuration
public class Setup
{
   @Bean
   public MultiplicationServiceImpl mulService()
   {
      return new MultiplicationServiceImpl(new RandomGeneratorServiceImpl()); 
   }
}
