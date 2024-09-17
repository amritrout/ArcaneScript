package arcaneInterpreter.Rules;

import java.util.List;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;

@ProcessRule(rule="<Statement> ::= invoke Identifier ( <OptionalParamList> ) start <Statements> end")

public class FunctionDefinition extends Reduction {
    public static final String FUNC_PREFIX = "[fn]";

    private final String name;
    private final List<String> paramList;
    private final Reduction body;

    public FunctionDefinition(GOLDParser parser) {
        Reduction currentReduction = parser.getCurrentReduction();

        name = currentReduction.get(1).getData().toString();
        paramList = (List<String>) currentReduction.get(3).asReduction().getValue().asObject();
        body = currentReduction.get(6).asReduction();

        // Clear existing parameter call stack
        parser.clearProgramVariable(Parameters.KEY_PARAMETERS);

        // Register the function for later calls
        parser.setProgramVariable(FUNC_PREFIX + name, new Variable(this));
    }

    public List<String> getParamList() {
        return paramList;
    }

    public Reduction getBody() {
        return body;
    }
}
