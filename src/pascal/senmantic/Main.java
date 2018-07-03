package pascal.senmantic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 */
public class Main {
    private static List<Token> tokenList=null;
    private static List<Quaternary> quaternaryList=null;


    public static void main(String[] args) {
        Reader reader = null;
        Writer tokenWriter = null;
        Writer errWriter = null;
        String text="";
        try {
            String fileName = "code.txt";
            Scanner in=new Scanner(new File(fileName));
            while (in.hasNextLine()){
                text+=in.nextLine();
                text+='\n';
            }
            reader = new FileReader(fileName);
            TextLex textLex=new TextLex(text);

            boolean success = textLex.scannerAll();
            tokenList= textLex.getTokenList();
            quaternaryList=new ArrayList<>();
            List<TextLex.Error> errorList = textLex.getErrorList();
            tokenWriter = new FileWriter("lex.dyd");
            String temp="";
            for (Token token:tokenList){
                temp+=token.getSymbol()+" "+token.getTokenKind().no+" "+token.getIndex();
                temp+="\n";
            }
            tokenWriter.write(temp);
            tokenWriter.flush();
            //输出错误信息
            if (!success){
                String errFile = "code.err";
                errWriter = new FileWriter(errFile);
                errWriter.flush();
                for (TextLex.Error error:errorList){
                    errWriter.write("line"+error.getLine()+":"+error.getMsg()+"\n");
                }
            }

            //语法分析器
            Parser parser = new Parser(tokenList,quaternaryList);
            parser.parse();
            System.out.println("语法正确");
            for (Quaternary t :
                    quaternaryList) {
                System.out.println(t.toString());
            }
            for (Token token:tokenList){
                if (token.getIndex()!=-1){
                    System.out.println(token.getIndex()+" "+token.getSymbol());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (reader!=null){
                    reader.close();
                }
                if (tokenWriter!=null){
                    tokenWriter.flush();
                    tokenWriter.close();
                }
                if(errWriter!=null){
                    errWriter.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }



}
