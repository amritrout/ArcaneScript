package arcaneInterpreter.Rules;

import java.math.RoundingMode;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import com.creativewidgetworks.goldparser.parser.Variable;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule = {
        "<Expression> ::= <Expression> > <Operator>",
        "<Expression> ::= <Expression> < <Operator>",
        "<Expression> ::= <Expression> <= <Operator>",
        "<Expression> ::= <Expression> >= <Operator>",
        "<Expression> ::= <Expression> '==' <Operator>",
        "<Expression> ::= <Operator>",
        "<Operator> ::= <Operator> '+' <Operator>",
        "<Operator> ::= <Operator> '-' <Operator>",
        "<Operator> ::= <Operator> '*' <Operator>",
        "<Operator> ::= <Operator> '/' <Operator>",
        "<Operator> ::= <NegativeValue>",
        "<NegativeValue> ::= <Value>"
})


public class Expression extends Reduction {
    private static final int PRECISION = 5;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final String validOperators = "== < <= > >= + - * / ";

    private GOLDParser theParser;
    private String theOperator;
    private Reduction leftExpression;
    private Reduction rightExpression;

    public Expression(GOLDParser parser) {
        theParser = parser;
        Reduction reduction = parser.getCurrentReduction();
        if (reduction!=null) {
            if (reduction.size()==3) {
                leftExpression =reduction.get(0).asReduction();
                theOperator =reduction.get(1).asString();
                rightExpression =reduction.get(2).asReduction();
                if (validOperators.indexOf(theOperator + " ") == -1) {
                    parser.raiseParserException(ArcaneInterpreter.formatMessage("error.invalid_operator", validOperators, theOperator));
                }
            } else {
                parser.raiseParserException(ArcaneInterpreter.formatMessage("error.param_count", "3", String.valueOf(reduction.size())));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public Variable getValue() throws ParserException {
        Variable result = null;
        Variable lValue = leftExpression.getValue();
        Variable rValue = rightExpression.getValue();

        boolean b = false;
        if (theOperator.equals("==")) {
            if (bothValuesAreNumbers()) {
                b = lValue.asNumber().compareTo(rValue.asNumber()) == 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (bothValuesAreBooleans()) {
                b = lValue.asBool() == rValue.asBool();
                result = new Variable(Boolean.valueOf(b));
            } else if (bothValuesAreTimestamps()) {
                b = lValue.asTimestamp().compareTo(rValue.asTimestamp()) == 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (oneOrBothValuesAreStrings()) {
                b = lValue.toString().compareTo(rValue.toString()) == 0;
                result = new Variable(Boolean.valueOf(b));
            } else {
                theParser.raiseParserException(ArcaneInterpreter.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals("<")) {
            if (bothValuesAreNumbers()) {
                b = lValue.asNumber().compareTo(rValue.asNumber()) < 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (bothValuesAreTimestamps()) {
                b = lValue.asTimestamp().compareTo(rValue.asTimestamp()) < 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (oneOrBothValuesAreStrings()) {
                b = lValue.toString().compareTo(rValue.toString()) < 0;
                result = new Variable(Boolean.valueOf(b));
            } else {
                theParser.raiseParserException(ArcaneInterpreter.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals("<=")) {
            if (bothValuesAreNumbers()) {
                b = lValue.asNumber().compareTo(rValue.asNumber()) <= 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (bothValuesAreTimestamps()) {
                b = lValue.asTimestamp().compareTo(rValue.asTimestamp()) <= 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (oneOrBothValuesAreStrings()) {
                b = lValue.toString().compareTo(rValue.toString()) <= 0;
                result = new Variable(Boolean.valueOf(b));
            } else {
                theParser.raiseParserException(ArcaneInterpreter.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals(">")) {
            if (bothValuesAreNumbers()) {
                b = lValue.asNumber().compareTo(rValue.asNumber()) > 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (bothValuesAreTimestamps()) {
                b = lValue.asTimestamp().compareTo(rValue.asTimestamp()) > 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (oneOrBothValuesAreStrings()) {
                b = lValue.toString().compareTo(rValue.toString()) > 0;
                result = new Variable(Boolean.valueOf(b));
            } else {
                theParser.raiseParserException(ArcaneInterpreter.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals(">=")) {
            if (bothValuesAreNumbers()) {
                b = lValue.asNumber().compareTo(rValue.asNumber()) >= 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (bothValuesAreTimestamps()) {
                b = lValue.asTimestamp().compareTo(rValue.asTimestamp()) >= 0;
                result = new Variable(Boolean.valueOf(b));
            } else if (oneOrBothValuesAreStrings()) {
                b = lValue.toString().compareTo(rValue.toString()) >= 0;
                result = new Variable(Boolean.valueOf(b));
            } else {
                theParser.raiseParserException(ArcaneInterpreter.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals("+")) {
            if (bothValuesAreNumbers()) {
                result = new Variable(lValue.asNumber().add(rValue.asNumber()));
            } else {
                // + handles both number addition and string concatenation
                result = new Variable(lValue.toString() + rValue.toString());
            }
        } else if (theOperator.equals("-")) {
            if (bothValuesAreNumbers()) {
                result = new Variable(lValue.asNumber().subtract(rValue.asNumber()));
            } else {
                theParser.raiseParserException(ArcaneInterpreter.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals("*")) {
            if (bothValuesAreNumbers()) {
                result = new Variable(lValue.asNumber().multiply(rValue.asNumber()));
            } else {
                theParser.raiseParserException(ArcaneInterpreter.formatMessage("error.type_mismatch"));
            }
        } else if (theOperator.equals("/")) {
            if (bothValuesAreNumbers()) {
                try {
                    result = new Variable(lValue.asNumber().divide(rValue.asNumber()));
                } catch (Exception e) {
                    result = new Variable(lValue.asNumber().divide(rValue.asNumber(), PRECISION, ROUNDING_MODE));
                }
            } else {
                theParser.raiseParserException(ArcaneInterpreter.formatMessage("error.type_mismatch"));
            }
        }

        return result;
    }

    /*----------------------------------------------------------------------------*/

    public boolean bothValuesAreBooleans() throws ParserException {
        return leftExpression.getValue().asBoolean() != null && rightExpression.getValue().asBoolean() != null;
    }

    public boolean bothValuesAreNumbers() throws ParserException {
        return leftExpression.getValue().asNumber() != null && rightExpression.getValue().asNumber() != null;
    }

    public boolean bothValuesAreTimestamps() throws ParserException {
        return leftExpression.getValue().asTimestamp() != null && rightExpression.getValue().asTimestamp() != null;
    }

    public boolean oneOrBothValuesAreStrings() throws ParserException {
        return leftExpression.getValue().asString() != null || rightExpression.getValue().asString() != null;
    }
}