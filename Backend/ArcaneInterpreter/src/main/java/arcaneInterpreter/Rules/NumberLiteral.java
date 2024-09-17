package arcaneInterpreter.Rules;

import java.math.BigDecimal;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule="<Value> ::= NumberLiteral")

public class NumberLiteral extends Reduction {

    public NumberLiteral(GOLDParser parser) throws ParserException  {
        String literal = parser.getCurrentReduction().get(0).asString();
        try {
            setValue(new Variable(new BigDecimal(literal)));
        } catch (NumberFormatException e) {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.token", literal));
        }
    }

}
