package lmsb.multiplication.service;

import lmsb.multiplication.domain.Multiplication;
import lmsb.multiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationService
{  
   Multiplication createRandomMultiplication(); 
   
   boolean checkAttempt(final MultiplicationResultAttempt
            resultAttempt);
}
