package lmsb.multiplication.repo;

import java.sql.SQLException;
import java.util.List;

import lmsb.multiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationResultAttemptRepoService
{
   public List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias)
            throws SQLException;
   
   public void saveMultiplicationResultAttempt(MultiplicationResultAttempt attempt); 
}
