package arcaneInterpreter.Rules;

import com.creativewidgetworks.goldparser.engine.ParserException;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;
import com.creativewidgetworks.goldparser.parser.ProcessRule;
import arcaneInterpreter.ArcaneInterpreter;

@ProcessRule(rule={
        "<Statement> ::= if ( <Expression> ) start <Statements> end",
        "<Statement> ::= if ( <Expression> ) start <Statements> end else start <Statements> end",
        "<Statement> ::= if ( <Expression> ) start <Statements> end else if ( <Expression> ) start <Statements> end",
        "<Statement> ::= if ( <Expression> ) start <Statements> end else if ( <Expression> ) start <Statements> end else start <Statements> end"
})
public class IfElse extends Reduction {
    private Reduction condition;
    private Reduction ifStatements;
    private Reduction elseIfCondition;
    private Reduction elseIfStatements;
    private Reduction elseStatements;

    public IfElse(GOLDParser parser) {
        Reduction reduction = parser.getCurrentReduction();
        if (reduction != null) {
            int size = reduction.size();
            switch (size) {
                case 7: // simple if
                    condition = reduction.get(2).asReduction();
                    ifStatements = reduction.get(5).asReduction();
                    break;
                    
                case 11: // if-else
                    condition = reduction.get(2).asReduction();
                    ifStatements = reduction.get(5).asReduction();
                    elseStatements = reduction.get(9).asReduction();
                    break;
                    
                case 15: // if-else if
                    condition = reduction.get(2).asReduction();
                    ifStatements = reduction.get(5).asReduction();
                    elseIfCondition = reduction.get(10).asReduction();
                    elseIfStatements = reduction.get(13).asReduction();
                    break;
                    
                case 19: // if-else if-else
                    condition = reduction.get(2).asReduction();
                    ifStatements = reduction.get(5).asReduction();
                    elseIfCondition = reduction.get(10).asReduction();
                    elseIfStatements = reduction.get(13).asReduction();
                    elseStatements = reduction.get(17).asReduction();
                    break;
                    
                default:
                    parser.raiseParserException(ArcaneInterpreter.formatMessage(
                        "error.param_count", "7, 11, 15, or 19", String.valueOf(size)));
            }
        } else {
            parser.raiseParserException(ArcaneInterpreter.formatMessage("error.no_reduction"));
        }
    }

    @Override
    public void execute() throws ParserException {
        if (condition.getValue().asBool()) {
            ifStatements.execute();
        } 
        else if (elseIfCondition != null && elseIfCondition.getValue().asBool()) {
            elseIfStatements.execute();
        }
        else if (elseStatements != null) {
            elseStatements.execute();
        }
    }
}