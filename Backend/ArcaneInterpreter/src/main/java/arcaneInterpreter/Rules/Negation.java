package arcaneInterpreter.Rules;

import java.math.BigDecimal;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule="<NegativeValue> ::= - <Value>")

public class Negation extends Reduction {
    private GOLDParser parser;
    private Reduction valueExpression;

    public Negation(GOLDParser parser) {
        this.parser = parser;
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 2) {
                valueExpression = reduction.get(1).asReduction();
            } else {
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", "2", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public Variable getValue() throws ParserException {
        BigDecimal number = valueExpression.getValue().asNumber();
        if (number == null) {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.negation_number_expected"));
        }
        return new Variable(number.negate());
    }
}
