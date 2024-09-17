package arcaneInterpreter;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.util.FormatHelper;

import java.io.*;

public class ArcaneInterpreter {

    private static final String RESOURCE_FILE = "ArcaneGrammar";

    public static String formatMessage(String message, Object... parameters) {
        return FormatHelper.formatMessage(RESOURCE_FILE, message, parameters);
    }

    /*----------------------------------------------------------------------------*/

    public String executeProgram(String sourceCode, boolean wantTree) {
        try (InputStream grammarStream = getClass().getResourceAsStream("/ArcaneGrammar7.egt")) {
            GOLDParser parser = new GOLDParser(
                    grammarStream,
                    "arcaneInterpreter",  // rule handler package
                    true);  // trim reductions

            parser.setGenerateTree(wantTree);

            boolean parsedWithoutError = parser.parseSourceStatements(sourceCode);
            String tree = parser.getParseTree();

            if (parsedWithoutError) {
                parser.getCurrentReduction().execute();
            } else {
                System.out.println(parser.getErrorMessage());
            }

            return tree;

        } catch (ParserException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.println("Error loading grammar file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /*----------------------------------------------------------------------------*/

    public String loadSourceFile(String filename) throws IOException {
        InputStream is = getResourceAsStream(filename);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return baos.toString("UTF-8");
        } finally {
            is.close();
        }
    }

    private InputStream getResourceAsStream(String filename) throws FileNotFoundException {
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            if (!filename.startsWith("/")) {
                filename = "/" + filename;
            }
            is = getClass().getResourceAsStream(filename);
            if (is == null) {
                throw e;
            }
        }
        return is;
    }

    /*----------------------------------------------------------------------------*/

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                ArcaneInterpreter parser = new ArcaneInterpreter();
                String source = parser.loadSourceFile(args[0]);
                boolean wantTree = args.length > 1 && args[1].equalsIgnoreCase("-tree");
                String tree = parser.executeProgram(source, wantTree);
                if (wantTree) {
                    System.out.println(tree);
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Usage: java -jar ArcaneInterpreter.jar <sourcefile> [-tree]");
        }
    }
}
