package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.SystemConsole;
import com.creativewidgetworks.goldparser.parser.Variable;
import arcaneInterpreter.ArcaneInterpreter;
import com.creativewidgetworks.goldparser.util.ConsoleDriver;

@ProcessRule(rule={
        "<Statement> ::= println <Expression> ';'",
        "<Statement> ::= println <Expression> input Identifier ';'",
        "<Statement> ::= println <Expression> <Expression> ';'"
})
public class PrintNewLine extends Reduction {
    private GOLDParser theParser;
    private Reduction dataToPrint;
    private Reduction stringToPrint;
    private String variableName;
    public static String EOLN = "\r\n";
    public static ConsoleDriver ioDriver = new SystemConsole();

    public PrintNewLine(GOLDParser parser) {
        theParser = parser;
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 3) {
                // print <Expression>
                dataToPrint = reduction.get(1).asReduction();
                stringToPrint = null;
                variableName = null;
            } else if (reduction.size() == 4) {
                // print StringLiteral <Expression>
                stringToPrint = reduction.get(1).asReduction(); // Extract the string literal
                dataToPrint = reduction.get(2).asReduction();
                variableName = null;
            } else if (reduction.size() == 5) {
                // print <Expression> read Id
                dataToPrint = reduction.get(1).asReduction();
                stringToPrint = null;
                variableName = reduction.get(3).asString();
            } else {
                if (reduction.size() < 3) {
                    parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", "2", String.valueOf(reduction.size())));
                }
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", "4", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public void execute() throws ParserException {
        // Write the stringToPrint to the console if it's not null
        if (stringToPrint != null) {
            ioDriver.write(stringToPrint.getValue().asObject().toString());
        }

        // Write the data to the console
        if (dataToPrint != null) {
            ioDriver.write(dataToPrint.getValue().asObject().toString());
        }

        if (variableName != null) {
            StringBuilder sb = new StringBuilder(ioDriver.read());
            theParser.setProgramVariable(variableName, new Variable(sb.toString()));
        } else {
            ioDriver.write(EOLN);
        }
    }
}
