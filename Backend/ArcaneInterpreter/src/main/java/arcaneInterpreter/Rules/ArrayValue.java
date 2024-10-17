package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import arcaneInterpreter.ArcaneInterpreter;

import java.util.Vector;

@ProcessRule(rule={
    "<Value> ::= Identifier '[' <Expression> ']'"
})
public class ArrayValue extends Reduction {
    private GOLDParser parser;
    private String arrayName;
    private Reduction indexExpression;

    public ArrayValue(GOLDParser parser) {
        this.parser = parser;
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            arrayName = reduction.get(0).asString();
            indexExpression = reduction.get(2).asReduction();
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public Variable getValue() {
        Variable arrayVar = parser.getProgramVariable(arrayName);
        if (arrayVar == null) {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.undefined_variable", arrayName));
        }

        
        Variable indexVar = indexExpression.getValue();
        
        try {
            int index = indexVar.asInt();
            Vector<Variable> array = (Vector<Variable>) arrayVar.asObject();
            
            if (index >= 0 && index < array.size()) {
                return array.get(index);
            } else {
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.index_out_of_bounds", String.valueOf(index)));
                return new Variable("");
            }
        } catch (NumberFormatException e) {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.invalid_index_type"));
            return new Variable("");
        }
    }
}