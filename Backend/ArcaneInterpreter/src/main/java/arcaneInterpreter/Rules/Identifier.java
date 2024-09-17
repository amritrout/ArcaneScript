package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule="<Value> ::= Identifier")
public class Identifier extends Reduction {
    private GOLDParser parser;
    private String name;

    public Identifier(GOLDParser parser) {
        this.parser = parser;
        Reduction reduction = parser.getCurrentReduction();

        if (reduction != null) {
            if (reduction.size() == 1) {
                this.name = reduction.get(0).asString();
            } else {
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", "1", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public Variable getValue() {
        Variable variable = parser.getProgramVariable(name);
        return (variable != null) ? variable : new Variable("");
    }

    @Override
    public String toString() {
        return name + "=" + getValue();
    }
}
