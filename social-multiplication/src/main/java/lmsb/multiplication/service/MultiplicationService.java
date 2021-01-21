package lmsb.multiplication.service;

import java.sql.SQLException;
import java.util.List;

import lmsb.multiplication.domain.Multiplication;
import lmsb.multiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationService
{  
   Multiplication createRandomMultiplication(); 
   
   boolean checkAttempt(final MultiplicationResultAttempt
            resultAttempt);
   
   List<MultiplicationResultAttempt> getStatsForUser(final String userAlias) throws SQLException;
   
   public MultiplicationResultAttempt findMRAById(int mraId); 
}
