package basic.ma;

public class MAComputeTemplate
{
  protected static String nl;
  public static synchronized MAComputeTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    MAComputeTemplate result = new MAComputeTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = " ";
  protected final String TEXT_2 = NL + "\tpublic double[] compute(double[] input, int period) {" + NL + "\t\tMInteger begin = new MInteger();" + NL + "\t\tMInteger length = new MInteger();" + NL + "\t\tdouble[] out = new double[input.length - period + 1];" + NL + "" + NL + "\t\tCore c = new Core();" + NL + "\t\tc.";
  protected final String TEXT_3 = "(0, input.length - 1, input, period, begin, length, out);" + NL + "" + NL + "\t\treturn out;" + NL + "\t}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(argument);
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}
