package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule="<Statement> ::= while ( <Expression> ) start <Statements> end")


public class WhileLoop extends Reduction {
    private Reduction conditional;
    private Reduction statements;

    public WhileLoop(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 7) {
                conditional = reduction.get(2).asReduction();
                statements  = reduction.get(5).asReduction();
            } else {
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", "5", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }          
    }

    @Override
    public void execute() throws ParserException {
        conditional.execute();
        while (conditional.getValue().asBool()) {
            statements.execute();
            conditional.execute();
        }
    }

}
