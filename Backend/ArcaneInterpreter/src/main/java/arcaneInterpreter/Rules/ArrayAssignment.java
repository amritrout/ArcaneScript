package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import arcaneInterpreter.ArcaneInterpreter;

import java.util.Vector;

@ProcessRule(rule={
        "<Statement> ::= Identifier '[' NumberLiteral ']' '=' <Expression> ';'"
})
public class ArrayAssignment extends Reduction {
    private GOLDParser parser;
    private String arrayName;
    private int index;
    private Reduction valueExpr;

    public ArrayAssignment(GOLDParser parser) {
        this.parser = parser;
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            arrayName = reduction.get(0).asString();
            index = Integer.parseInt(reduction.get(2).asString());
            valueExpr = reduction.get(5).asReduction();
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public void execute() {
        Variable arrayVar = parser.getProgramVariable(arrayName);

        if (arrayVar == null) {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.undefined_variable", arrayName));
        }

        Vector<Variable> array = (Vector<Variable>) arrayVar.asObject();

        if (index >= 0 && index < array.size()) {
            Variable newValue = valueExpr.getValue();
            array.set(index, newValue);
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.index_out_of_bounds", String.valueOf(index)));
        }
    }

}
