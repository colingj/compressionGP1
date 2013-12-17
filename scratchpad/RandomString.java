public class RandomString
{
  public static void main(String[] args)
  {
    for (int i=0;i<1000000;i++)
      {
	System.out.print(Math.random()<0.5?"0":"1");
      }
    System.out.println();
  }
}