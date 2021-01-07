package lmsb.multiplication.repo;

import lmsb.multiplication.domain.Multiplication;

public interface MultiplicationRepoService
{
   public Multiplication findMultiplicationByID(int mulID); 

   public void saveMultiplication(Multiplication multiplication); 
}
