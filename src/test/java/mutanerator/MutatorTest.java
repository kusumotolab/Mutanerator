package mutanerator;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.junit.Test;

public class MutatorTest {

  @Test
  public void testConditionalBoundary01() {

    final String text = //
        "class Test01{" + //
            "  boolean method01(int a, int b){" + //
            "    return a < b;" + //
            "  }" + //
            "}";

    this.testConditionalBoundary(text, InfixExpression.Operator.LESS,
        InfixExpression.Operator.LESS_EQUALS);
  }

  @Test
  public void testConditionalBoundary02() {

    final String text = //
        "class Test02{" + //
            "  boolean method02(int a, int b){" + //
            "    return a <= b;" + //
            "  }" + //
            "}";

    this.testConditionalBoundary(text, InfixExpression.Operator.LESS_EQUALS,
        InfixExpression.Operator.LESS);
  }

  @Test
  public void testConditionalBoundary03() {

    final String text = //
        "class Test03{" + //
            "  boolean method03(int a, int b){" + //
            "    return a > b;" + //
            "  }" + //
            "}";

    this.testConditionalBoundary(text, InfixExpression.Operator.GREATER,
        InfixExpression.Operator.GREATER_EQUALS);
  }

  @Test
  public void testConditionalBoundary04() {

    final String text = //
        "class Test04{" + //
            "  boolean method04(int a, int b){" + //
            "    return a >= b;" + //
            "  }" + //
            "}";

    this.testConditionalBoundary(text, InfixExpression.Operator.GREATER_EQUALS,
        InfixExpression.Operator.GREATER);
  }

  private void testConditionalBoundary(final String text,
      final InfixExpression.Operator assumedOriginalOperator,
      final InfixExpression.Operator assumedMutatedOperator) {

    final ProgramElementCollectorBuilder builder = new ProgramElementCollectorBuilder();
    final ProgramElementCollector collector = builder.build(text);
    collector.perform();
    final MutationTargets mutationTargets = collector.getMutationTargets();
    final List<ASTNode> targetNodes = mutationTargets.getTargetNodes(Mutator.ConditionalsBoundary);
    assertThat(targetNodes).hasSize(1);

    final ASTNode node = targetNodes.get(0);
    assertThat(node).isInstanceOf(InfixExpression.class);

    final InfixExpression.Operator originalOperator = ((InfixExpression) node).getOperator();
    assertThat(originalOperator).isEqualTo(assumedOriginalOperator);

    final Mutation mutation = new Mutation(Mutator.ConditionalsBoundary, node);
    mutation.mutator.mutate(mutation);

    final InfixExpression.Operator mutatedOperator = ((InfixExpression) node).getOperator();
    assertThat(mutatedOperator).isEqualTo(assumedMutatedOperator);
  }

  @Test
  public void testMath01() {

    final String text = //
        "class Test01{" + //
            "  int method01(int a, int b){" + //
            "    return a + b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.PLUS, InfixExpression.Operator.MINUS);
  }

  @Test
  public void testMath02() {

    final String text = //
        "class Test02{" + //
            "  int method02(int a, int b){" + //
            "    return a - b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.MINUS, InfixExpression.Operator.PLUS);
  }

  @Test
  public void testMath03() {

    final String text = //
        "class Test03{" + //
            "  int method03(int a, int b){" + //
            "    return a * b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.TIMES, InfixExpression.Operator.DIVIDE);
  }

  @Test
  public void testMath04() {

    final String text = //
        "class Test04{" + //
            "  int method04(int a, int b){" + //
            "    return a / b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.DIVIDE, InfixExpression.Operator.TIMES);
  }

  @Test
  public void testMath05() {

    final String text = //
        "class Test05{" + //
            "  int method05(int a, int b){" + //
            "    return a % b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.REMAINDER, InfixExpression.Operator.TIMES);
  }

  @Test
  public void testMath06() {

    final String text = //
        "class Test06{" + //
            "  boolean method06(int a, int b){" + //
            "    return a & b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.AND, InfixExpression.Operator.OR);
  }

  @Test
  public void testMath07() {

    final String text = //
        "class Test07{" + //
            "  boolean method07(int a, int b){" + //
            "    return a | b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.OR, InfixExpression.Operator.AND);
  }

  @Test
  public void testMath08() {

    final String text = //
        "class Test08{" + //
            "  boolean method08(int a, int b){" + //
            "    return a ^ b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.XOR, InfixExpression.Operator.AND);
  }

  @Test
  public void testMath09() {

    final String text = //
        "class Test09{" + //
            "  byte method09(byte a, int b){" + //
            "    return a << b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.LEFT_SHIFT,
        InfixExpression.Operator.RIGHT_SHIFT_SIGNED);
  }

  @Test
  public void testMath10() {

    final String text = //
        "class Test10{" + //
            "  byte method10(byte a, int b){" + //
            "    return a >> b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.RIGHT_SHIFT_SIGNED,
        InfixExpression.Operator.LEFT_SHIFT);
  }

  @Test
  public void testMath11() {

    final String text = //
        "class Test11{" + //
            "  byte method11(byte a, int b){" + //
            "    return a >>> b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED,
        InfixExpression.Operator.LEFT_SHIFT);
  }

  private void testMath(final String text, final InfixExpression.Operator assumedOriginalOperator,
      final InfixExpression.Operator assumedMutatedOperator) {

    final ProgramElementCollectorBuilder builder = new ProgramElementCollectorBuilder();
    final ProgramElementCollector collector = builder.build(text);
    collector.perform();
    final MutationTargets mutationTargets = collector.getMutationTargets();
    final List<ASTNode> targetNodes = mutationTargets.getTargetNodes(Mutator.Math);
    assertThat(targetNodes).hasSize(1);

    final ASTNode node = targetNodes.get(0);
    assertThat(node).isInstanceOf(InfixExpression.class);

    final InfixExpression.Operator originalOperator = ((InfixExpression) node).getOperator();
    assertThat(originalOperator).isEqualTo(assumedOriginalOperator);

    final Mutation mutation = new Mutation(Mutator.Math, node);
    mutation.mutator.mutate(mutation);

    final InfixExpression.Operator mutatedOperator = ((InfixExpression) node).getOperator();
    assertThat(mutatedOperator).isEqualTo(assumedMutatedOperator);
  }
}
