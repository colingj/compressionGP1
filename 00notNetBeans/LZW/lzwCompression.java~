public class myLZW 
{
  private static final int R = 256;        // number of input chars
  private static final int L = 4096;       // number of codewords = 2^W
  private static final int W = 12;         // codeword width
  
  public static String compress(String inputString) { 
    String input = inputString;
    TST<Integer> st = new TST<Integer>();
    for (int i = 0; i < R; i++)
      st.put("" + (char) i, i);
    int code = R+1;  // R is codeword for EOF
    
    String output = new String();
    while (input.length() > 0) {
      String s = st.longestPrefixOf(input);  // Find max prefix match s.
      output += st.get(s)+" ";
      int t = s.length();
      if (t < input.length() && code < L)    // Add s to symbol table.
	st.put(input.substring(0, t + 1), code++);
      input = input.substring(t);            // Scan past s in input.
    }
    return output;
  } 

}
