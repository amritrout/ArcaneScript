package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;

import java.util.ArrayList;
import java.util.List;

@ProcessRule(rule={
        "<ArrayElementList> ::= <Operator> ',' <ArrayElementList>",
        "<ArrayElementList> ::= <Operator>"
})
public class ArrayElementList extends Reduction {
    private List<Reduction> elements = new ArrayList<>();

    public ArrayElementList(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            if (reduction.size() == 3) {
                // <Operator> ',' <ArrayElementList>
                elements.add(reduction.get(0).asReduction());
                elements.addAll(((List<Reduction>) reduction.get(2).asReduction().getValue()));
            } else if (reduction.size() == 1) {
                // <Operator>
                elements.add(reduction.get(0).asReduction());
            } else {
                parser.raiseParserException("Unexpected number of elements in <ArrayElementList>");
            }
        } else {
            parser.raiseParserException("No reduction found for <ArrayElementList>");
        }
    }

    @Override
    public Variable getValue() {
        return new Variable(elements);
    }
}
