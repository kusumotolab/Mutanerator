package mutanerator;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

public enum Mutator {

  ConditionalsBoundary(true) {
    @Override
    void mutate(final Mutation mutation) {
      assert mutation.node instanceof InfixExpression : "illegal statement";
      final InfixExpression infix = (InfixExpression) mutation.node;
      final InfixExpression.Operator operator = infix.getOperator();
      ORIGINAL_INFIX_OPERATORS.put(mutation, operator);

      // "<" -> "<="
      if (operator.equals(InfixExpression.Operator.LESS)) {
        infix.setOperator(InfixExpression.Operator.LESS_EQUALS);
      }

      // "<=" -> "<"
      else if (operator.equals(InfixExpression.Operator.LESS_EQUALS)) {
        infix.setOperator(InfixExpression.Operator.LESS);
      }

      // ">" -> ">="
      else if (operator.equals(InfixExpression.Operator.GREATER)) {
        infix.setOperator(InfixExpression.Operator.GREATER_EQUALS);
      }

      // ">=" -> ">"
      else if (operator.equals(InfixExpression.Operator.GREATER_EQUALS)) {
        infix.setOperator(InfixExpression.Operator.GREATER);
      }
    }

    @Override
    void recover(final Mutation mutation) {
      super.recoverInfixOperator(mutation);
    }
  },
  Increments(true) {
    @Override
    void mutate(final Mutation mutation) {
      if (mutation.node instanceof PostfixExpression) {

        final PostfixExpression postfix = (PostfixExpression) mutation.node;
        final PostfixExpression.Operator operator = postfix.getOperator();
        ORIGINAL_POSTFIX_OPERATORS.put(mutation, operator);

        // "--" -> "++"
        if (operator.equals(PostfixExpression.Operator.DECREMENT)) {
          postfix.setOperator(PostfixExpression.Operator.INCREMENT);
        }

        // "++" -> "--"
        else if (operator.equals(PostfixExpression.Operator.INCREMENT)) {
          postfix.setOperator(PostfixExpression.Operator.DECREMENT);
        }

      } else if (mutation.node instanceof PrefixExpression) {

        final PrefixExpression prefix = (PrefixExpression) mutation.node;
        final PrefixExpression.Operator operator = prefix.getOperator();
        ORIGINAL_PREFIX_OPERATORS.put(mutation, operator);

        // "--" -> "++"
        if (operator.equals(PrefixExpression.Operator.DECREMENT)) {
          prefix.setOperator(PrefixExpression.Operator.INCREMENT);
        }

        // "++" -> "--"
        else if (operator.equals(PrefixExpression.Operator.INCREMENT)) {
          prefix.setOperator(PrefixExpression.Operator.DECREMENT);
        }
      } else {
        assert false : "illegal statement";
      }
    }

    @Override
    void recover(final Mutation mutation) {
      if (mutation.node instanceof PostfixExpression) {
        super.recoverPostfixOperator(mutation);
      } else if (mutation.node instanceof PrefixExpression) {
        super.recoverPrefixOperator(mutation);
      }
    }
  },
  InvertNegatives(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  Math(true) {
    @Override
    void mutate(final Mutation mutation) {

      assert mutation.node instanceof InfixExpression : "illegal statement";
      final InfixExpression infix = (InfixExpression) mutation.node;
      final InfixExpression.Operator operator = infix.getOperator();
      ORIGINAL_INFIX_OPERATORS.put(mutation, operator);

      // "+" -> "-"
      if (operator.equals(InfixExpression.Operator.PLUS)) {
        infix.setOperator(InfixExpression.Operator.MINUS);
      }

      // "-" -> "+"
      else if (operator.equals(InfixExpression.Operator.MINUS)) {
        infix.setOperator(InfixExpression.Operator.PLUS);
      }

      // "*" -> "/"
      else if (operator.equals(InfixExpression.Operator.TIMES)) {
        infix.setOperator(InfixExpression.Operator.DIVIDE);
      }

      // "/" -> "*"
      else if (operator.equals(InfixExpression.Operator.DIVIDE)) {
        infix.setOperator(InfixExpression.Operator.TIMES);
      }

      // "%" -> "*"
      else if (operator.equals(InfixExpression.Operator.REMAINDER)) {
        infix.setOperator(InfixExpression.Operator.TIMES);
      }

      // "&" -> "|"
      else if (operator.equals(InfixExpression.Operator.AND)) {
        infix.setOperator(InfixExpression.Operator.OR);
      }

      // "|" -> "&"
      else if (operator.equals(InfixExpression.Operator.OR)) {
        infix.setOperator(InfixExpression.Operator.AND);
      }

      // "^" -> "&"
      else if (operator.equals(InfixExpression.Operator.XOR)) {
        infix.setOperator(InfixExpression.Operator.AND);
      }

      // "<<" -> ">>"
      else if (operator.equals(InfixExpression.Operator.LEFT_SHIFT)) {
        infix.setOperator(InfixExpression.Operator.RIGHT_SHIFT_SIGNED);
      }

      // ">>" -> "<<"
      else if (operator.equals(InfixExpression.Operator.RIGHT_SHIFT_SIGNED)) {
        infix.setOperator(InfixExpression.Operator.LEFT_SHIFT);
      }

      // ">>>" -> "<<"
      else if (operator.equals(InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED)) {
        infix.setOperator(InfixExpression.Operator.LEFT_SHIFT);
      }
    }

    @Override
    void recover(final Mutation mutation) {
      super.recoverInfixOperator(mutation);
    }
  },

  NegateConditionals(true) {
    @Override
    void mutate(final Mutation mutation) {

      assert mutation.node instanceof InfixExpression : "illegal statement";
      final InfixExpression infix = (InfixExpression) mutation.node;
      final InfixExpression.Operator operator = infix.getOperator();
      ORIGINAL_INFIX_OPERATORS.put(mutation, operator);

      // "==" -> "!="
      if (operator.equals(InfixExpression.Operator.EQUALS)) {
        infix.setOperator(InfixExpression.Operator.NOT_EQUALS);
      }

      // "!=" -> "=="
      else if (operator.equals(InfixExpression.Operator.NOT_EQUALS)) {
        infix.setOperator(InfixExpression.Operator.EQUALS);
      }

      // "<=" -> ">"
      else if (operator.equals(InfixExpression.Operator.LESS_EQUALS)) {
        infix.setOperator(InfixExpression.Operator.GREATER);
      }

      // ">=" -> "<"
      else if (operator.equals(InfixExpression.Operator.GREATER_EQUALS)) {
        infix.setOperator(InfixExpression.Operator.LESS);
      }

      // "<" -> ">="
      else if (operator.equals(InfixExpression.Operator.LESS)) {
        infix.setOperator(InfixExpression.Operator.GREATER_EQUALS);
      }

      // ">" -> "<="
      else if (operator.equals(InfixExpression.Operator.GREATER)) {
        infix.setOperator(InfixExpression.Operator.LESS_EQUALS);
      }
    }

    @Override
    void recover(final Mutation mutation) {
      super.recoverInfixOperator(mutation);
    }
  },
  ReturnValues(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  VoidMetthodCalls(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  ConstructorCalls(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  EmptyReturns(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  FalseReturns(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  InlineConstant(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  NullReturns(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  NonVoidMethodCalls(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  PrimitiveReturns(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  RemoveConditionals(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  RemoveIncrements(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  TrueReturns(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  ExperimentalArgumentPropagation(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  ExperimentalBigInteger(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  ExperimentalMemberVariable(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  ExperimentalNakedReceiver(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  ExperimentalSwitch(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  Negation(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  ArithmeticOperatorReplacement(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  ArithmeticOperatorDeletion(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  ConstantReplacement(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  BitwiseOperator(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  RelationalOperatorReplacement(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  },
  UnaryOperatorInsertion(false) {
    @Override
    void mutate(Mutation mutation) {
      // TODO Auto-generated method stub

    }

    @Override
    void recover(Mutation mutation) {
      // TODO Auto-generated method stub

    }
  };

  public final boolean available;
  private static final Map<Mutation, InfixExpression.Operator> ORIGINAL_INFIX_OPERATORS =
      new HashMap<>();
  private static final Map<Mutation, PostfixExpression.Operator> ORIGINAL_POSTFIX_OPERATORS =
      new HashMap<>();
  private static final Map<Mutation, PrefixExpression.Operator> ORIGINAL_PREFIX_OPERATORS =
      new HashMap<>();

  Mutator(final boolean available) {
    this.available = available;
  }

  public boolean isAvailable() {
    return this.available;
  }

  abstract void mutate(Mutation node);

  abstract void recover(Mutation node);

  /**
   * 二項演算の演算子を元に戻す
   *
   * @param mutation
   */
  private void recoverInfixOperator(final Mutation mutation) {

    // 変異が適用されていない場合は何もせずにメソッドを抜ける
    if (!ORIGINAL_INFIX_OPERATORS.containsKey(mutation)) {
      System.err.println(mutation.toString() + " is not applied.");
      return;
    }

    // 演算子を元に戻す
    assert mutation.node instanceof InfixExpression : "illegal statement";
    final InfixExpression infix = (InfixExpression) mutation.node;
    final InfixExpression.Operator operator = ORIGINAL_INFIX_OPERATORS.get(mutation);
    infix.setOperator(operator);

    // 変異が適用された演算子の群から，元に戻した演算子を取り除く
    ORIGINAL_INFIX_OPERATORS.remove(mutation);
  }

  /**
   * PostfixExpressionの演算子を元に戻す
   *
   * @param mutation
   */
  private void recoverPostfixOperator(final Mutation mutation) {

    // 変異が適用されていない場合は何もせずにメソッドを抜ける
    if (!ORIGINAL_POSTFIX_OPERATORS.containsKey(mutation)) {
      System.err.println(mutation.toString() + " is not applied.");
      return;
    }

    // 演算子を元に戻す
    assert mutation.node instanceof PostfixExpression : "illegal statement";
    final PostfixExpression infix = (PostfixExpression) mutation.node;
    final PostfixExpression.Operator operator = ORIGINAL_POSTFIX_OPERATORS.get(mutation);
    infix.setOperator(operator);

    // 変異が適用された演算子の群から，元に戻した演算子を取り除く
    ORIGINAL_POSTFIX_OPERATORS.remove(mutation);
  }

  /**
   * PrefixExpressionの演算子を元に戻す
   *
   * @param mutation
   */
  private void recoverPrefixOperator(final Mutation mutation) {

    // 変異が適用されていない場合は何もせずにメソッドを抜ける
    if (!ORIGINAL_PREFIX_OPERATORS.containsKey(mutation)) {
      System.err.println(mutation.toString() + " is not applied.");
      return;
    }

    // 演算子を元に戻す
    assert mutation.node instanceof PrefixExpression : "illegal statement";
    final PrefixExpression infix = (PrefixExpression) mutation.node;
    final PrefixExpression.Operator operator = ORIGINAL_PREFIX_OPERATORS.get(mutation);
    infix.setOperator(operator);

    // 変異が適用された演算子の群から，元に戻した演算子を取り除く
    ORIGINAL_PREFIX_OPERATORS.remove(mutation);
  }
}
