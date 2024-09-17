package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import arcaneInterpreter.ArcaneInterpreter;

import java.util.Vector;

@ProcessRule(rule={
        "<Statement> ::= Identifier '=' Identifier '[' NumberLiteral ']' ';'"
})
public class ArrayAccess extends Reduction {
    private GOLDParser parser;
    private String arrayName;
    private String targetIdentifier;
    private int index;

    public ArrayAccess(GOLDParser parser) {
        this.parser = parser;
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            targetIdentifier = reduction.get(0).asString();
            arrayName = reduction.get(2).asString();
            index = Integer.parseInt(reduction.get(4).asString());
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
            Variable value = array.get(index);
            parser.setProgramVariable(targetIdentifier, value);
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.index_out_of_bounds", String.valueOf(index)));
        }
    }
}
