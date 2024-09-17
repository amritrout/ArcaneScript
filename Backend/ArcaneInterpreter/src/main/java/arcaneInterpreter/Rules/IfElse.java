package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule={
        "<Statement> ::= if ( <Expression> ) start <Statements> end",
        "<Statement> ::= if ( <Expression> ) start <Statements> end else start <Statements> end"
})
public class IfElse extends Reduction {
    private Reduction condition;
    private Reduction ifStatements;
    private Reduction elseStatements;

    public IfElse(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            int size = reduction.size();
            if (size == 7) {
                condition = reduction.get(2).asReduction();
                ifStatements = reduction.get(5).asReduction();
            } else if (size == 11) {
                condition = reduction.get(2).asReduction();
                ifStatements = reduction.get(5).asReduction();
                elseStatements = reduction.get(9).asReduction();
            } else {
                String expected = size < 11 ? "7" : "11";
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", expected, String.valueOf(size)));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public void execute() throws ParserException {
        if (condition.getValue().asBool()) {
            ifStatements.execute();
        } else if (elseStatements != null) {
            elseStatements.execute();
        }
    }
}
