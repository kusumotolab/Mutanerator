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
        "class Less{" + //
            "  boolean less(int a, int b){" + //
            "    return a < b;" + //
            "  }" + //
            "}";

    this.testConditionalBoundary(text, InfixExpression.Operator.LESS,
        InfixExpression.Operator.LESS_EQUALS);
  }

  @Test
  public void testConditionalBoundary02() {

    final String text = //
        "class Less{" + //
            "  boolean less(int a, int b){" + //
            "    return a <= b;" + //
            "  }" + //
            "}";

    this.testConditionalBoundary(text, InfixExpression.Operator.LESS_EQUALS,
        InfixExpression.Operator.LESS);
  }

  @Test
  public void testConditionalBoundary03() {

    final String text = //
        "class Less{" + //
            "  boolean less(int a, int b){" + //
            "    return a > b;" + //
            "  }" + //
            "}";

    this.testConditionalBoundary(text, InfixExpression.Operator.GREATER,
        InfixExpression.Operator.GREATER_EQUALS);
  }

  @Test
  public void testConditionalBoundary04() {

    final String text = //
        "class Less{" + //
            "  boolean less(int a, int b){" + //
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
}
