package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule="<Statement> ::= for ( <Statement> <Expression> ; <Statement> ) start <Statements> end")
public class ForLoop extends Reduction {
    private Reduction initialization;
    private Reduction condition;
    private Reduction iteration;
    private Reduction statements;

    public ForLoop(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();

        if (reduction != null) {
            if (reduction.size() == 10) {
                initialization = reduction.get(2).asReduction();
                condition= reduction.get(3).asReduction();
                iteration= reduction.get(5).asReduction();
                statements= reduction.get(8).asReduction();
            } else {
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", "10", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public void execute() throws ParserException {
        initialization.execute();
        while (condition.getValue().asBool()) {
            statements.execute();
            iteration.execute();
            condition.execute();
        }
    }
}
