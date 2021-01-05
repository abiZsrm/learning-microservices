package microservices.book.multiplication.domain;

public final class Multiplication
{
   private final int m_operand1;
   private final int m_operand2;
   private int m_result; 

   public Multiplication(int operand1, int operand2)
   {
      this.m_operand1 = operand1; 
      this.m_operand2 = operand2; 
      this.m_result = operand1 * operand2; 
   }
   
   public int getOperand1()
   {
      return m_operand1;
   }

   public int getOperand2()
   {
      return m_operand2;
   }

   public int getResult()
   {
      return this.m_result; 
   }
   
   public void calculateResult()
   {
      this.m_result = this.m_operand1 * this.m_operand2;
   }

   public String toString()
   {
     String output = ""; 
     
     output += "Multiplication { Operand 1: %d" 
            + " | Operand 2: %d"
            + " | (Operand 1 * Operand 2): %d"
            + " }"; 
     
     
     return String.format(output, this.m_operand1, this.m_operand2, this.m_result); 
   }
}
