package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule="<Statement> ::= log <Expression> ';'")
public class Log extends Reduction {

    private Reduction expression;

    public Log(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 3) {
                expression = reduction.get(1).asReduction();
            } else {
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", "2", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public void execute() throws ParserException {
        if (expression != null) {
            expression.execute();
            setValue(expression.getValue());
        }
    }
}
