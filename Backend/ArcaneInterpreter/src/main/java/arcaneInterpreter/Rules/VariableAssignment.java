package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule={
        "<Statement> ::= Identifier '=' <Operator> ';'"
})

public class VariableAssignment extends Reduction {
    private String assignedVariable;
    private Reduction expressionValue;
    private GOLDParser parser;


    public VariableAssignment(GOLDParser parser) {
        this.parser = parser;
        Reduction currentReduction = parser.getCurrentReduction();

        if (currentReduction != null) {
            if (currentReduction.size() == 4) {
                assignedVariable = currentReduction.get(0).asString();
                expressionValue = currentReduction.get(2).asReduction();
            } else {
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", "3", String.valueOf(currentReduction.size())));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public void execute() throws ParserException {
        parser.setProgramVariable(assignedVariable, expressionValue.getValue());
    }
}
