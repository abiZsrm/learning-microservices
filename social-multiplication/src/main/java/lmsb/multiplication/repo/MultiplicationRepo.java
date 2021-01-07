package lmsb.multiplication.repo;

import lmsb.multiplication.domain.Multiplication;

public interface MultiplicationRepo
{
   public Multiplication findMultiplicationByID(int mulID); 

   public void saveMultiplication(Multiplication multiplication); 
}
