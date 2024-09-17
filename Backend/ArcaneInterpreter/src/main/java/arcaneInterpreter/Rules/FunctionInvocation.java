package arcaneInterpreter.Rules;

import java.util.List;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Scope;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule="<Value> ::= Identifier ( <OptionalArgumentList> )")

public class FunctionInvocation extends Reduction {
    private final GOLDParser parser;
    private final String functionName;
    private final List<Reduction> argsList;

    public FunctionInvocation(GOLDParser parser) {
        this.parser = parser;
        Reduction currentReduction = parser.getCurrentReduction();
        functionName = currentReduction.get(0).getData().toString();

        if (currentReduction.get(2).asReduction() instanceof Arguments) {
            argsList = (List<Reduction>)currentReduction.get(2).asReduction().getValue().asObject();
        } else {
            // Handles cases with a single parameter
            Arguments args = new Arguments(currentReduction.get(2).asReduction());
            argsList = (List<Reduction>)args.getValue().asObject();
        }
    }

    @Override
    public Variable getValue() {
        Scope newScope = new Scope(FunctionDefinition.FUNC_PREFIX + functionName, parser.getCurrentScope());
        Scope previousScope = parser.setCurrentScope(newScope);

        try {
            // Retrieve function then set parameter and then execute
            Variable functionVar = parser.getProgramVariable(FunctionDefinition.FUNC_PREFIX + functionName);
            if (functionVar != null) {
                FunctionDefinition functionDef = (FunctionDefinition)functionVar.asObject();

                List<String> paramNames = functionDef.getParamList();

                // Check if argument count matches parameter count
                if (paramNames.size() != argsList.size()) {
                    throw new ParserException(ArcaneInterpreter.formatMessage("error.function_argument_count",
                            String.valueOf(paramNames.size()), String.valueOf(argsList.size())));
                }

                // Set function parameters
                for (int i = 0; i < paramNames.size(); i++) {
                    parser.setProgramVariable(paramNames.get(i), argsList.get(i).getValue());
                }

                Reduction functionStatements = functionDef.getBody();
                if (functionStatements != null) {
                    functionStatements.execute();
                    functionVar = functionStatements.getValue();
                }

                // Default return value if nothing is given
                if (functionVar == null) {
                    functionVar = new Variable("");
                }
            } else {
                throw new ParserException(ArcaneInterpreter.formatMessage("error.function_undefined", functionName));
            }

            return functionVar;

        } finally {
            parser.setCurrentScope(previousScope);
        }
    }
}
