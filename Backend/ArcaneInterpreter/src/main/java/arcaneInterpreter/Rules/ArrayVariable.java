package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import arcaneInterpreter.ArcaneInterpreter;

import java.util.Vector;

@ProcessRule(rule = "<Value> ::= Identifier [ NumberLiteral ]")
public class ArrayVariable extends Reduction {
    GOLDParser Thisparser;

    private String arrayName;
    private int index;
    private Variable arrvar;

    public ArrayVariable(GOLDParser parser) {
        Thisparser=parser;
        Reduction reduction = parser.getCurrentReduction();

        if (reduction != null) {
            if (reduction.size() == 4) {
                arrayName =reduction.get(0).asString();
                index = Integer.parseInt(reduction.get(2).asString());
            } else {
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", "4", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public Variable getValue() {
        Variable arrayVar = Thisparser.getProgramVariable(arrayName);

        Vector<Variable> array = (Vector<Variable>) arrayVar.asObject();
        if (index >= 0 && index < array.size()) {
            Variable value = array.get(index);
            arrvar=value;
        } else {
            Thisparser.raiseParserException(ArcaneInterpreter.formatMessage("error.index_out_of_bounds", String.valueOf(index)));
        }

        return arrvar;

    }

}
