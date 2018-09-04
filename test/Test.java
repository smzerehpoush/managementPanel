import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 36; i++) {
            System.out.println(
                    "case '" + (char) ('ا' + i) + "' : \n" +
                            "                    stringBuilder.append( '" + (char) ('À' + i) + "' );\n" +
                            "                break; \n"+
                            "                ");
        }
//        for (int i = 0 ; i <100 ; i++){
//            System.out.println((char)('z'+i));
//        }
    }
}
