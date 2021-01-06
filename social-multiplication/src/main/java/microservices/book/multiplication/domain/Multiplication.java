package microservices.book.multiplication.domain;

public final class Multiplication
{
   private final int operand1;
   private final int operand2;
   private int m_result; 

   public Multiplication()
   {
      this(0, 0);
   }
   public Multiplication(int operand1, int operand2)
   {
      this.operand1 = operand1; 
      this.operand2 = operand2; 
      this.m_result = operand1 * operand2; 
   }
   
   public int getOperand1()
   {
      return operand1;
   }

   public int getOperand2()
   {
      return operand2;
   }

   public int getResult()
   {
      return this.m_result; 
   }
   
   public void calculateResult()
   {
      this.m_result = this.operand1 * this.operand2;
   }

   public String toString()
   {
     String output = ""; 
     
     output += "Multiplication { Operand 1: %d" 
            + " | Operand 2: %d"
            + " | (Operand 1 * Operand 2): %d"
            + " }"; 
     
     
     return String.format(output, this.operand1, this.operand2, this.m_result); 
   }
}
