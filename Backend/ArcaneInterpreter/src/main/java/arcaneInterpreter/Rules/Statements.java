package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule={
        "<Statements> ::= <StatementOrBlock> <Statements>",
        "<Statements> ::= <StatementOrBlock>",
        "<StatementOrBlock> ::= <Statement>",
        "<StatementOrBlock> ::= begin <Statements> end"
})
public class Statements extends Reduction {
    private Reduction statement1;
    private Reduction statement2;

    public Statements(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() >= 1 && reduction.size() <= 2) {
                statement1 = reduction.get(0).asReduction();
                statement2 = (reduction.size() == 2) ? reduction.get(1).asReduction() : null;
            } else {
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count_range", "1", "2", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public void execute() throws ParserException {
        if (statement1 != null) {
            if (statement1 instanceof FunctionInvocation) {
                ((FunctionInvocation) statement1).getValue(); 
            } else {
                statement1.execute();
            }
            setValue(statement1.getValue());
        }
    }
}
