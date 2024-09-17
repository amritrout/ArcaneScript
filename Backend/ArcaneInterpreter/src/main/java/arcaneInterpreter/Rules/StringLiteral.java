package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import com.creativewidgetworks.goldparser.parser.GOLDParser;

@ProcessRule(rule="<Value> ::= StringLiteral")
public class StringLiteral extends Reduction {

    public StringLiteral(GOLDParser parser) {
        String str = parser.getCurrentReduction().get(0).asString();
        StringBuilder sb = new StringBuilder(str);
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(0);
        setValue(new Variable(sb.toString()));
    }
}
