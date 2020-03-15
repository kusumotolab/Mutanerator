package mutanerator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

/**
 * This Class represent mutator.
 * A mutator means an operation how a given program is changed.
 *
 * ミューテイター（変異子）を表すクラスである．
 * 変異子とは，変更操作を表す言葉である．
 */
public enum Mutator {

  ConditionalsBoundary(true) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {

      // 対象ノードの二項演算子を取得
      assert targetNode instanceof InfixExpression : "illegal statement";
      final InfixExpression targetInfix = (InfixExpression) targetNode;
      final InfixExpression.Operator operator = targetInfix.getOperator();

      // 新しいノードを作成
      final ASTNode rootNode = targetNode.getRoot();
      final InfixExpression newInfix = (InfixExpression) ASTNode.copySubtree(rootNode.getAST(),
          targetInfix);

      // "<" -> "<="
      if (operator.equals(InfixExpression.Operator.LESS)) {
        newInfix.setOperator(InfixExpression.Operator.LESS_EQUALS);
      }

      // "<=" -> "<"
      else if (operator.equals(InfixExpression.Operator.LESS_EQUALS)) {
        newInfix.setOperator(InfixExpression.Operator.LESS);
      }

      // ">" -> ">="
      else if (operator.equals(InfixExpression.Operator.GREATER)) {
        newInfix.setOperator(InfixExpression.Operator.GREATER_EQUALS);
      }

      // ">=" -> ">"
      else if (operator.equals(InfixExpression.Operator.GREATER_EQUALS)) {
        newInfix.setOperator(InfixExpression.Operator.GREATER);
      }

      rewrite.replace(targetNode, newInfix, null);
    }
  },
  Increments(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {

      if (targetNode instanceof PostfixExpression) {

        final PostfixExpression targetPostfix = (PostfixExpression) targetNode;
        final PostfixExpression.Operator operator = targetPostfix.getOperator();

        // 新しいノードを作成
        final ASTNode rootNode = targetNode.getRoot();
        final PostfixExpression newPostfix = (PostfixExpression) ASTNode.copySubtree(
            rootNode.getAST(),
            targetPostfix);

        // "--" -> "++"
        if (operator.equals(PostfixExpression.Operator.DECREMENT)) {
          newPostfix.setOperator(PostfixExpression.Operator.INCREMENT);
        }

        // "++" -> "--"
        else if (operator.equals(PostfixExpression.Operator.INCREMENT)) {
          newPostfix.setOperator(PostfixExpression.Operator.DECREMENT);
        }

        rewrite.replace(targetNode, newPostfix, null);

      } else if (targetNode instanceof PrefixExpression) {

        final PrefixExpression targetPrefix = (PrefixExpression) targetNode;
        final PrefixExpression.Operator operator = targetPrefix.getOperator();

        // 新しいノードを作成
        final ASTNode rootNode = targetNode.getRoot();
        final PrefixExpression newPrefix = (PrefixExpression) ASTNode.copySubtree(
            rootNode.getAST(),
            targetPrefix);

        // "--" -> "++"
        if (operator.equals(PrefixExpression.Operator.DECREMENT)) {
          newPrefix.setOperator(PrefixExpression.Operator.INCREMENT);
        }

        // "++" -> "--"
        else if (operator.equals(PrefixExpression.Operator.INCREMENT)) {
          newPrefix.setOperator(PrefixExpression.Operator.DECREMENT);
        }

        rewrite.replace(targetNode, newPrefix, null);

      } else {
        assert false : "illegal statement";
      }
    }
  },
  InvertNegatives(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {

      // 対象ノードのオペランドを取得
      assert targetNode instanceof PrefixExpression : "illegal statement";
      final PrefixExpression targetPrefix = (PrefixExpression) targetNode;
      final Expression operand = targetPrefix.getOperand();

      // 新しいノードを作成
      final ASTNode rootNode = targetNode.getRoot();
      final Expression newOperand = (Expression) ASTNode.copySubtree(rootNode.getAST(), operand);

      rewrite.replace(targetPrefix, newOperand, null);
    }
  },
  Math(true) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {

      // 対象ノードの二項演算子を取得
      assert targetNode instanceof InfixExpression : "illegal statement";
      final InfixExpression targetInfix = (InfixExpression) targetNode;
      final InfixExpression.Operator operator = targetInfix.getOperator();

      // 新しいノードを作成
      final ASTNode rootNode = targetNode.getRoot();
      final InfixExpression newInfix = (InfixExpression) ASTNode.copySubtree(rootNode.getAST(),
          targetInfix);

      // "+" -> "-"
      if (operator.equals(InfixExpression.Operator.PLUS)) {
        newInfix.setOperator(InfixExpression.Operator.MINUS);
      }

      // "-" -> "+"
      else if (operator.equals(InfixExpression.Operator.MINUS)) {
        newInfix.setOperator(InfixExpression.Operator.PLUS);
      }

      // "*" -> "/"
      else if (operator.equals(InfixExpression.Operator.TIMES)) {
        newInfix.setOperator(InfixExpression.Operator.DIVIDE);
      }

      // "/" -> "*"
      else if (operator.equals(InfixExpression.Operator.DIVIDE)) {
        newInfix.setOperator(InfixExpression.Operator.TIMES);
      }

      // "%" -> "*"
      else if (operator.equals(InfixExpression.Operator.REMAINDER)) {
        newInfix.setOperator(InfixExpression.Operator.TIMES);
      }

      // "&" -> "|"
      else if (operator.equals(InfixExpression.Operator.AND)) {
        newInfix.setOperator(InfixExpression.Operator.OR);
      }

      // "|" -> "&"
      else if (operator.equals(InfixExpression.Operator.OR)) {
        newInfix.setOperator(InfixExpression.Operator.AND);
      }

      // "^" -> "&"
      else if (operator.equals(InfixExpression.Operator.XOR)) {
        newInfix.setOperator(InfixExpression.Operator.AND);
      }

      // "<<" -> ">>"
      else if (operator.equals(InfixExpression.Operator.LEFT_SHIFT)) {
        newInfix.setOperator(InfixExpression.Operator.RIGHT_SHIFT_SIGNED);
      }

      // ">>" -> "<<"
      else if (operator.equals(InfixExpression.Operator.RIGHT_SHIFT_SIGNED)) {
        newInfix.setOperator(InfixExpression.Operator.LEFT_SHIFT);
      }

      // ">>>" -> "<<"
      else if (operator.equals(InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED)) {
        newInfix.setOperator(InfixExpression.Operator.LEFT_SHIFT);
      }

      rewrite.replace(targetNode, newInfix, null);
    }
  },

  NegateConditionals(true) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {

      // 対象ノードの二項演算子を取得
      assert targetNode instanceof InfixExpression : "illegal statement";
      final InfixExpression targetInfix = (InfixExpression) targetNode;
      final InfixExpression.Operator operator = targetInfix.getOperator();

      // 新しいノードを作成
      final ASTNode rootNode = targetNode.getRoot();
      final InfixExpression newInfix = (InfixExpression) ASTNode.copySubtree(rootNode.getAST(),
          targetInfix);

      // "==" -> "!="
      if (operator.equals(InfixExpression.Operator.EQUALS)) {
        newInfix.setOperator(InfixExpression.Operator.NOT_EQUALS);
      }

      // "!=" -> "=="
      else if (operator.equals(InfixExpression.Operator.NOT_EQUALS)) {
        newInfix.setOperator(InfixExpression.Operator.EQUALS);
      }

      // "<=" -> ">"
      else if (operator.equals(InfixExpression.Operator.LESS_EQUALS)) {
        newInfix.setOperator(InfixExpression.Operator.GREATER);
      }

      // ">=" -> "<"
      else if (operator.equals(InfixExpression.Operator.GREATER_EQUALS)) {
        newInfix.setOperator(InfixExpression.Operator.LESS);
      }

      // "<" -> ">="
      else if (operator.equals(InfixExpression.Operator.LESS)) {
        newInfix.setOperator(InfixExpression.Operator.GREATER_EQUALS);
      }

      // ">" -> "<="
      else if (operator.equals(InfixExpression.Operator.GREATER)) {
        newInfix.setOperator(InfixExpression.Operator.LESS_EQUALS);
      }

      rewrite.replace(targetNode, newInfix, null);
    }
  },
  ReturnValues(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  VoidMethodCalls(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {

      // 対象ノードのオペランドを取得
      assert targetNode instanceof ExpressionStatement : "illegal statement";
      final ExpressionStatement statement = (ExpressionStatement) targetNode;

      rewrite.remove(statement, null);
    }
  },
  ConstructorCalls(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  EmptyReturns(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  FalseReturns(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  InlineConstant(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  NullReturns(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  NonVoidMethodCalls(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  PrimitiveReturns(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  RemoveConditionals(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  RemoveIncrements(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  TrueReturns(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  ExperimentalArgumentPropagation(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  ExperimentalBigInteger(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  ExperimentalMemberVariable(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  ExperimentalNakedReceiver(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  ExperimentalSwitch(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  Negation(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  ArithmeticOperatorReplacement(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  ArithmeticOperatorDeletion(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  ConstantReplacement(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  BitwiseOperator(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  RelationalOperatorReplacement(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  },
  UnaryOperatorInsertion(false) {
    @Override
    void manipulateAST(final ASTNode targetNode, final ASTRewrite rewrite) {
    }
  };
  public final boolean available;

  Mutator(final boolean available) {
    this.available = available;
  }

  public boolean isAvailable() {
    return this.available;
  }

  abstract void manipulateAST(ASTNode targetNode, ASTRewrite rewrite);
}
