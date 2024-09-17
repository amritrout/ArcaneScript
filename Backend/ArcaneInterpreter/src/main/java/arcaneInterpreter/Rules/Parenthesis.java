package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule="<Value> ::= ( <Expression> )")

public class Parenthesis extends Reduction {

    Reduction innerReduction;

    public Parenthesis(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 3) {
                innerReduction = parser.getCurrentReduction().get(1).asReduction();
            } else {
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", "3", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public Variable getValue() {
        return innerReduction.getValue();
    }

}
