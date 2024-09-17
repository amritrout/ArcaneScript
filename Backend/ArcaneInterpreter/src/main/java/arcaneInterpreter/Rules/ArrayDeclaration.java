package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import arcaneInterpreter.ArcaneInterpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@ProcessRule(rule={
        "<Statement> ::= array Identifier '[' NumberLiteral ']' '=' start <ArgumentList> end ';'",
        "<Statement> ::= array Identifier '[' ']' '=' start <ArgumentList> end ';'",
        "<Statement> ::= array Identifier '[' NumberLiteral ']' ';'"
})

public class ArrayDeclaration extends Reduction {
    private GOLDParser theParser;
    private String arrayName;
    private int size;
    private List<Reduction> elements;

    public ArrayDeclaration(GOLDParser parser) {
        theParser = parser;
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 10) {
                // for array Identifier '[' NumberLiteral ']' = start <ArgumentList> end ';'
                arrayName = reduction.get(1).asString();
                size = Integer.parseInt(reduction.get(3).asString());
                elements = extractArguments(reduction.get(7).asReduction());
            } else if (reduction.size() == 9) {
                // for array Identifier '[' ']' = start <ArgumentList> end ';'
                arrayName = reduction.get(1).asString();
                size = -1;
                elements = extractArguments(reduction.get(6).asReduction());
            } else if (reduction.size() == 6) {
                // for array Identifier '[' NumberLiteral ']' ';'
                arrayName = reduction.get(1).asString();
                size = Integer.parseInt(reduction.get(3).asString());
                elements = new ArrayList<>(); // Default to empty
            } else {
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", "5, 7, or 8", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    private List<Reduction> extractArguments(Reduction reduction) {
        Arguments arguments;
        if (reduction instanceof Arguments) {
            arguments = (Arguments) reduction;
        } else {
            arguments = new Arguments(reduction);
        }

        List<Reduction> argsList = (List<Reduction>) arguments.getValue().asObject();
        return argsList;
    }

    @Override
    public void execute() {
        Vector<Variable> array = new Vector<>();
        if (elements != null && !elements.isEmpty()) {
            for (int i = 0; i < elements.size(); i++) {
                Variable value = elements.get(i).getValue();
                array.add(i, value);
            }
        } else if (size > 0) {
            for (int i = 0; i < size; i++) {
                array.add(i, new Variable(0));
            }
        }
        theParser.setProgramVariable(arrayName, new Variable(array));
    }
}
