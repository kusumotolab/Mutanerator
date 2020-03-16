package mutanerator;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
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

    final String expectedText = //
        "class Test01{" + //
            "  boolean method01(int a, int b){" + //
            "    return a <= b;" + //
            "  }" + //
            "}";

    this.testConditionalBoundary(text, InfixExpression.Operator.LESS, expectedText);
  }

  @Test
  public void testConditionalBoundary02() {

    final String text = //
        "class Test02{" + //
            "  boolean method02(int a, int b){" + //
            "    return a <= b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test02{" + //
            "  boolean method02(int a, int b){" + //
            "    return a < b;" + //
            "  }" + //
            "}";

    this.testConditionalBoundary(text, InfixExpression.Operator.LESS_EQUALS, expectedText);
  }

  @Test
  public void testConditionalBoundary03() {

    final String text = //
        "class Test03{" + //
            "  boolean method03(int a, int b){" + //
            "    return a > b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test03{" + //
            "  boolean method03(int a, int b){" + //
            "    return a >= b;" + //
            "  }" + //
            "}";

    this.testConditionalBoundary(text, InfixExpression.Operator.GREATER, expectedText);
  }

  @Test
  public void testConditionalBoundary04() {

    final String text = //
        "class Test04{" + //
            "  boolean method04(int a, int b){" + //
            "    return a >= b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test04{" + //
            "  boolean method04(int a, int b){" + //
            "    return a > b;" + //
            "  }" + //
            "}";

    this.testConditionalBoundary(text, InfixExpression.Operator.GREATER_EQUALS, expectedText);
  }

  private void testConditionalBoundary(final String text,
      final InfixExpression.Operator assumedOriginalOperator,
      final String expectedText) {

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
    final ASTNode rootNode = node.getRoot();
    final String mutatedText = mutation.apply(rootNode, text);
    assertThat(mutatedText).isEqualTo(expectedText);
  }

  @Test
  public void testIncrement01() {

    final String text = //
        "class Test01{" + //
            "  int method01(int a){" + //
            "    return a++;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test01{" + //
            "  int method01(int a){" + //
            "    return a--;" + //
            "  }" + //
            "}";

    this.testIncrements0A(text, PostfixExpression.Operator.INCREMENT, expectedText);
  }

  @Test
  public void testIncrement02() {

    final String text = //
        "class Test02{" + //
            "  int method02(int a){" + //
            "    return a--;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test02{" + //
            "  int method02(int a){" + //
            "    return a++;" + //
            "  }" + //
            "}";

    this.testIncrements0A(text, PostfixExpression.Operator.DECREMENT, expectedText);
  }

  @Test
  public void testIncrement03() {

    final String text = //
        "class Test03{" + //
            "  int method03(int a){" + //
            "    return ++a;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test03{" + //
            "  int method03(int a){" + //
            "    return --a;" + //
            "  }" + //
            "}";

    this.testIncrements0B(text, PrefixExpression.Operator.INCREMENT, expectedText);
  }

  @Test
  public void testIncrement04() {

    final String text = //
        "class Test04{" + //
            "  int method04(int a){" + //
            "    return --a;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test04{" + //
            "  int method04(int a){" + //
            "    return ++a;" + //
            "  }" + //
            "}";

    this.testIncrements0B(text, PrefixExpression.Operator.DECREMENT, expectedText);
  }

  private void testIncrements0A(final String text,
      final PostfixExpression.Operator assumedOriginalOperator,
      final String expectedText) {

    final ProgramElementCollectorBuilder builder = new ProgramElementCollectorBuilder();
    final ProgramElementCollector collector = builder.build(text);
    collector.perform();
    final MutationTargets mutationTargets = collector.getMutationTargets();
    final List<ASTNode> targetNodes = mutationTargets.getTargetNodes(Mutator.Increments);
    assertThat(targetNodes).hasSize(1);

    final ASTNode node = targetNodes.get(0);
    assertThat(node).isInstanceOf(PostfixExpression.class);

    final PostfixExpression.Operator originalOperator = ((PostfixExpression) node).getOperator();
    assertThat(originalOperator).isEqualTo(assumedOriginalOperator);

    final Mutation mutation = new Mutation(Mutator.Increments, node);
    final ASTNode rootNode = node.getRoot();
    final String mutatedText = mutation.apply(rootNode, text);
    assertThat(mutatedText).isEqualTo(expectedText);
  }

  private void testIncrements0B(final String text,
      final PrefixExpression.Operator assumedOriginalOperator,
      final String expectedText) {

    final ProgramElementCollectorBuilder builder = new ProgramElementCollectorBuilder();
    final ProgramElementCollector collector = builder.build(text);
    collector.perform();
    final MutationTargets mutationTargets = collector.getMutationTargets();
    final List<ASTNode> targetNodes = mutationTargets.getTargetNodes(Mutator.Increments);
    assertThat(targetNodes).hasSize(1);

    final ASTNode node = targetNodes.get(0);
    assertThat(node).isInstanceOf(PrefixExpression.class);

    final PrefixExpression.Operator originalOperator = ((PrefixExpression) node).getOperator();
    assertThat(originalOperator).isEqualTo(assumedOriginalOperator);

    final Mutation mutation = new Mutation(Mutator.Increments, node);
    final ASTNode rootNode = node.getRoot();
    final String mutatedText = mutation.apply(rootNode, text);
    assertThat(mutatedText).isEqualTo(expectedText);
  }


  @Test
  public void testInvertNegatives01() {

    final String text = //
        "class Test01{" + //
            "  int method01(int a){" + //
            "    return -a;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test01{" + //
            "  int method01(int a){" + //
            "    return a;" + //
            "  }" + //
            "}";

    this.testInvertNegatives(text, PrefixExpression.Operator.MINUS, expectedText);
  }

  private void testInvertNegatives(final String text,
      final PrefixExpression.Operator assumedOriginalOperator,
      final String expectedText) {

    final ProgramElementCollectorBuilder builder = new ProgramElementCollectorBuilder();
    final ProgramElementCollector collector = builder.build(text);
    collector.perform();
    final MutationTargets mutationTargets = collector.getMutationTargets();
    final List<ASTNode> targetNodes = mutationTargets.getTargetNodes(Mutator.InvertNegatives);
    assertThat(targetNodes).hasSize(1);

    final ASTNode node = targetNodes.get(0);
    assertThat(node).isInstanceOf(PrefixExpression.class);

    final PrefixExpression.Operator originalOperator = ((PrefixExpression) node).getOperator();
    assertThat(originalOperator).isEqualTo(PrefixExpression.Operator.MINUS);

    final Mutation mutation = new Mutation(Mutator.InvertNegatives, node);
    final ASTNode rootNode = node.getRoot();
    final String mutatedText = mutation.apply(rootNode, text);
    assertThat(mutatedText).isEqualTo(expectedText);
  }

  @Test
  public void testMath01() {

    final String text = //
        "class Test01{" + //
            "  int method01(int a, int b){" + //
            "    return a + b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test01{" + //
            "  int method01(int a, int b){" + //
            "    return a - b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.PLUS, expectedText);
  }

  @Test
  public void testMath02() {

    final String text = //
        "class Test02{" + //
            "  int method02(int a, int b){" + //
            "    return a - b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test02{" + //
            "  int method02(int a, int b){" + //
            "    return a + b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.MINUS, expectedText);
  }

  @Test
  public void testMath03() {

    final String text = //
        "class Test03{" + //
            "  int method03(int a, int b){" + //
            "    return a * b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test03{" + //
            "  int method03(int a, int b){" + //
            "    return a / b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.TIMES, expectedText);
  }

  @Test
  public void testMath04() {

    final String text = //
        "class Test04{" + //
            "  int method04(int a, int b){" + //
            "    return a / b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test04{" + //
            "  int method04(int a, int b){" + //
            "    return a * b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.DIVIDE, expectedText);
  }

  @Test
  public void testMath05() {

    final String text = //
        "class Test05{" + //
            "  int method05(int a, int b){" + //
            "    return a % b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test05{" + //
            "  int method05(int a, int b){" + //
            "    return a * b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.REMAINDER, expectedText);
  }

  @Test
  public void testMath06() {

    final String text = //
        "class Test06{" + //
            "  boolean method06(int a, int b){" + //
            "    return a & b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test06{" + //
            "  boolean method06(int a, int b){" + //
            "    return a | b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.AND, expectedText);
  }

  @Test
  public void testMath07() {

    final String text = //
        "class Test07{" + //
            "  boolean method07(int a, int b){" + //
            "    return a | b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test07{" + //
            "  boolean method07(int a, int b){" + //
            "    return a & b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.OR, expectedText);
  }

  @Test
  public void testMath08() {

    final String text = //
        "class Test08{" + //
            "  boolean method08(int a, int b){" + //
            "    return a ^ b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test08{" + //
            "  boolean method08(int a, int b){" + //
            "    return a & b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.XOR, expectedText);
  }

  @Test
  public void testMath09() {

    final String text = //
        "class Test09{" + //
            "  byte method09(byte a, int b){" + //
            "    return a << b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test09{" + //
            "  byte method09(byte a, int b){" + //
            "    return a >> b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.LEFT_SHIFT, expectedText);
  }

  @Test
  public void testMath10() {

    final String text = //
        "class Test10{" + //
            "  byte method10(byte a, int b){" + //
            "    return a >> b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test10{" + //
            "  byte method10(byte a, int b){" + //
            "    return a << b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.RIGHT_SHIFT_SIGNED, expectedText);
  }

  @Test
  public void testMath11() {

    final String text = //
        "class Test11{" + //
            "  byte method11(byte a, int b){" + //
            "    return a >>> b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test11{" + //
            "  byte method11(byte a, int b){" + //
            "    return a << b;" + //
            "  }" + //
            "}";

    this.testMath(text, InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED, expectedText);
  }

  private void testMath(final String text, final InfixExpression.Operator assumedOriginalOperator,
      final String expectedText) {

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
    final ASTNode rootNode = node.getRoot();
    final String mutatedText = mutation.apply(rootNode, text);
    assertThat(mutatedText).isEqualTo(expectedText);
  }

  @Test
  public void testNegateConditionals01() {

    final String text = //
        "class Test01{" + //
            "  boolean method01(int a, int b){" + //
            "    return a == b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test01{" + //
            "  boolean method01(int a, int b){" + //
            "    return a != b;" + //
            "  }" + //
            "}";

    this.testNegateConditionals(text, InfixExpression.Operator.EQUALS, expectedText);
  }

  @Test
  public void testNegateConditionals02() {

    final String text = //
        "class Test02{" + //
            "  boolean method02(int a, int b){" + //
            "    return a != b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test02{" + //
            "  boolean method02(int a, int b){" + //
            "    return a == b;" + //
            "  }" + //
            "}";

    this.testNegateConditionals(text, InfixExpression.Operator.NOT_EQUALS, expectedText);
  }

  @Test
  public void testNegateConditionals03() {

    final String text = //
        "class Test03{" + //
            "  boolean method03(int a, int b){" + //
            "    return a <= b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test03{" + //
            "  boolean method03(int a, int b){" + //
            "    return a > b;" + //
            "  }" + //
            "}";

    this.testNegateConditionals(text, InfixExpression.Operator.LESS_EQUALS, expectedText);
  }

  @Test
  public void testNegateConditionals04() {

    final String text = //
        "class Test04{" + //
            "  boolean method04(int a, int b){" + //
            "    return a >= b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test04{" + //
            "  boolean method04(int a, int b){" + //
            "    return a < b;" + //
            "  }" + //
            "}";

    this.testNegateConditionals(text, InfixExpression.Operator.GREATER_EQUALS, expectedText);
  }

  @Test
  public void testNegateConditionals05() {

    final String text = //
        "class Test05{" + //
            "  boolean method05(int a, int b){" + //
            "    return a < b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test05{" + //
            "  boolean method05(int a, int b){" + //
            "    return a >= b;" + //
            "  }" + //
            "}";

    this.testNegateConditionals(text, InfixExpression.Operator.LESS, expectedText);
  }

  @Test
  public void testNegateConditionals06() {

    final String text = //
        "class Test06{" + //
            "  boolean method06(int a, int b){" + //
            "    return a > b;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test06{" + //
            "  boolean method06(int a, int b){" + //
            "    return a <= b;" + //
            "  }" + //
            "}";

    this.testNegateConditionals(text, InfixExpression.Operator.GREATER, expectedText);
  }

  private void testNegateConditionals(final String text,
      final InfixExpression.Operator assumedOriginalOperator,
      final String expectedText) {

    final ProgramElementCollectorBuilder builder = new ProgramElementCollectorBuilder();
    final ProgramElementCollector collector = builder.build(text);
    collector.perform();
    final MutationTargets mutationTargets = collector.getMutationTargets();
    final List<ASTNode> targetNodes = mutationTargets.getTargetNodes(Mutator.NegateConditionals);
    assertThat(targetNodes).hasSize(1);

    final ASTNode node = targetNodes.get(0);
    assertThat(node).isInstanceOf(InfixExpression.class);

    final InfixExpression.Operator originalOperator = ((InfixExpression) node).getOperator();
    assertThat(originalOperator).isEqualTo(assumedOriginalOperator);

    final Mutation mutation = new Mutation(Mutator.NegateConditionals, node);
    final ASTNode rootNode = node.getRoot();
    final String mutatedText = mutation.apply(rootNode, text);
    assertThat(mutatedText).isEqualTo(expectedText);
  }

  @Test
  public void testVoidMethodCalls01() {

    final String text = //
        "class Test01{" + //
            "  boolean method01(){" + //
            "    System.out.println();" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test01{" + //
            "  boolean method01(){" + //
            "  }" + //
            "}";

    this.testVoidMethodCalls(text, expectedText);
  }

  private void testVoidMethodCalls(final String text, final String expectedText) {

    final ProgramElementCollectorBuilder builder = new ProgramElementCollectorBuilder();
    final ProgramElementCollector collector = builder.build(text);
    collector.perform();
    final MutationTargets mutationTargets = collector.getMutationTargets();
    final List<ASTNode> targetNodes = mutationTargets.getTargetNodes(Mutator.VoidMethodCalls);
    assertThat(targetNodes).hasSize(1);

    final ASTNode node = targetNodes.get(0);
    assertThat(node).isInstanceOf(ExpressionStatement.class);

    final Expression expression = ((ExpressionStatement) node).getExpression();
    assertThat(expression).isInstanceOf(MethodInvocation.class);

    final Mutation mutation = new Mutation(Mutator.VoidMethodCalls, node);
    final ASTNode rootNode = node.getRoot();
    final String mutatedText = mutation.apply(rootNode, text);
    assertThat(mutatedText).isEqualTo(expectedText);
  }

  @Test
  public void testPrimitiveReturns01() {

    final String text = //
        "class Test01{" + //
            "  int method01(){" + //
            "    return 1;" + //
            "  }" + //
            "}";

    final String expectedText = //
        "class Test01{" + //
            "  int method01(){" + //
            "    return 0;" + //
            "  }" + //
            "}";

    this.testPrimitiveReturns(text, expectedText);
  }

  @Test
  public void testPrimitiveReturns02() {

    final String text = //
        "class Test02{" + //
            "  int method02(){" + //
            "    return 0;" + //
            "  }" + //
            "}";

    final ProgramElementCollectorBuilder builder = new ProgramElementCollectorBuilder();
    final ProgramElementCollector collector = builder.build(text);
    collector.perform();
    final MutationTargets mutationTargets = collector.getMutationTargets();
    final List<ASTNode> targetNodes = mutationTargets.getTargetNodes(Mutator.PrimitiveReturns);
    assertThat(targetNodes).hasSize(0);
  }

  @Test
  public void testPrimitiveReturns03() {

    final String text = //
        "class Test03{" + //
            "  Integer method03(){" + //
            "    return new Integer(0);" + //
            "  }" + //
            "}";

    final ProgramElementCollectorBuilder builder = new ProgramElementCollectorBuilder();
    final ProgramElementCollector collector = builder.build(text);
    collector.perform();
    final MutationTargets mutationTargets = collector.getMutationTargets();
    final List<ASTNode> targetNodes = mutationTargets.getTargetNodes(Mutator.PrimitiveReturns);
    assertThat(targetNodes).hasSize(0);
  }

  private void testPrimitiveReturns(final String text, final String expectedText) {

    final ProgramElementCollectorBuilder builder = new ProgramElementCollectorBuilder();
    final ProgramElementCollector collector = builder.build(text);
    collector.perform();
    final MutationTargets mutationTargets = collector.getMutationTargets();
    final List<ASTNode> targetNodes = mutationTargets.getTargetNodes(Mutator.PrimitiveReturns);
    assertThat(targetNodes).hasSize(1);

    final ASTNode node = targetNodes.get(0);
    assertThat(node).isInstanceOf(Expression.class);

    final Mutation mutation = new Mutation(Mutator.PrimitiveReturns, node);
    final ASTNode rootNode = node.getRoot();
    final String mutatedText = mutation.apply(rootNode, text);
    assertThat(mutatedText).isEqualTo(expectedText);
  }
}
