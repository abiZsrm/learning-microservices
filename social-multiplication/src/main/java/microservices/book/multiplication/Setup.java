package microservices.book.multiplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import microservices.book.multiplication.service.MultiplicationServiceImpl;
import microservices.book.multiplication.service.RandomGeneratorServiceImpl;

@Configuration
public class Setup
{
   @Bean
   public MultiplicationServiceImpl mulService()
   {
      return new MultiplicationServiceImpl(new RandomGeneratorServiceImpl()); 
   }
}
