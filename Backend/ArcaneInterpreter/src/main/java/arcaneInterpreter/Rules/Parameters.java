package arcaneInterpreter.Rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.parser.GOLDParser;

@ProcessRule(rule={
        "<ParamList> ::= Identifier , <ParamList>",
        "<ParamList> ::= Identifier",
        "<OptionalParamList> ::= <ParamList>",
        "<OptionalParamList> ::= "
})
public class Parameters extends Reduction {

    public static final String KEY_PARAMETERS = "[ParameterStack]";

    public Parameters(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        List<String> parameters = new ArrayList<>();

        if (reduction.size() > 0) {
            Stack<String> stack = getOrCreateParameterStack(parser);

            stack.push(reduction.get(0).getData().toString());

            // Copy values from stack to the parameters list
            for (int i = stack.size() - 1; i >= 0; i--) {
                parameters.add(stack.get(i));
            }
        }

        setValue(new Variable(parameters));
    }

    private Stack<String> getOrCreateParameterStack(GOLDParser parser) {
        Variable var = parser.getProgramVariable(KEY_PARAMETERS);
        if (var == null) {
            Stack<String> newStack = new Stack<>();
            parser.setProgramVariable(KEY_PARAMETERS, new Variable(newStack));
            return newStack;
        } else {
            return (Stack<String>) var.asObject();
        }
    }
}
