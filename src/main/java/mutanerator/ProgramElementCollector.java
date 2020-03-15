package mutanerator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

public class ProgramElementCollector extends ASTVisitor {

  private final CompilationUnit astRootNode;
  private final MutationTargets mutationTargets;

  public ProgramElementCollector(final CompilationUnit astRootNode) {
    this.astRootNode = astRootNode;
    this.mutationTargets = new MutationTargets();
  }

  public void perform() {
    this.astRootNode.accept(this);
  }

  public CompilationUnit getASTRootNode() {
    return this.astRootNode;
  }

  public MutationTargets getMutationTargets() {
    return this.mutationTargets;
  }

  @Override
  public boolean visit(final ExpressionStatement node) {

    // 変異の範囲外である場合は何もしない
    if (!this.inTargetRange(node)) {
      return super.visit(node);
    }

    final Expression expression = node.getExpression();

    // VoidMethodCalls変異の対象として登録
    if(expression instanceof MethodInvocation){
      this.mutationTargets.put(Mutator.VoidMethodCalls, node);
    }

    return super.visit(node);
  }

  @Override
  public boolean visit(final InfixExpression node) {

    // 変異の範囲外である場合は何もしない
    if (!this.inTargetRange(node)) {
      return super.visit(node);
    }

    final InfixExpression.Operator operator = node.getOperator();

    // ConditionalsBoundary変異の対象として登録
    if (operator.equals(InfixExpression.Operator.GREATER)
        || operator.equals(InfixExpression.Operator.GREATER_EQUALS)
        || operator.equals(InfixExpression.Operator.LESS)
        || operator.equals(InfixExpression.Operator.LESS_EQUALS)) {
      this.mutationTargets.put(Mutator.ConditionalsBoundary, node);
    }

    // Math変異の対象として登録
    if (operator.equals(InfixExpression.Operator.PLUS)
        || operator.equals(InfixExpression.Operator.MINUS)
        || operator.equals(InfixExpression.Operator.TIMES)
        || operator.equals(InfixExpression.Operator.DIVIDE)
        || operator.equals(InfixExpression.Operator.REMAINDER)
        || operator.equals(InfixExpression.Operator.AND)
        || operator.equals(InfixExpression.Operator.OR)
        || operator.equals(InfixExpression.Operator.XOR)
        || operator.equals(InfixExpression.Operator.LEFT_SHIFT)
        || operator.equals(InfixExpression.Operator.RIGHT_SHIFT_SIGNED)
        || operator.equals(InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED)) {
      this.mutationTargets.put(Mutator.Math, node);
    }

    // NegateConditionals変異の対象として登録
    if (operator.equals(InfixExpression.Operator.EQUALS)
        || operator.equals(InfixExpression.Operator.NOT_EQUALS)
        || operator.equals(InfixExpression.Operator.LESS_EQUALS)
        || operator.equals(InfixExpression.Operator.GREATER_EQUALS)
        || operator.equals(InfixExpression.Operator.LESS)
        || operator.equals(InfixExpression.Operator.GREATER)) {
      this.mutationTargets.put(Mutator.NegateConditionals, node);
    }
    return super.visit(node);
  }

  @Override
  public boolean visit(final PostfixExpression node) {

    // 変異の範囲外である場合は何もしない
    if (!this.inTargetRange(node)) {
      return super.visit(node);
    }

    final PostfixExpression.Operator operator = node.getOperator();

    // Increments変異の対象として登録
    if (operator.equals(PostfixExpression.Operator.DECREMENT)
        || operator.equals(PostfixExpression.Operator.INCREMENT)) {
      this.mutationTargets.put(Mutator.Increments, node);
    }

    return super.visit(node);
  }

  @Override
  public boolean visit(final PrefixExpression node) {

    // 変異の範囲外である場合は何もしない
    if (!this.inTargetRange(node)) {
      return super.visit(node);
    }

    final PrefixExpression.Operator operator = node.getOperator();

    // Increments変異の対象として登録
    if (operator.equals(PrefixExpression.Operator.DECREMENT)
        || operator.equals(PrefixExpression.Operator.INCREMENT)) {
      this.mutationTargets.put(Mutator.Increments, node);
    }

    // InvertNegatives変異の対象として登録
    if (operator.equals(PrefixExpression.Operator.MINUS)) {
      this.mutationTargets.put(Mutator.InvertNegatives, node);
    }

    return super.visit(node);
  }

  private boolean inTargetRange(final ASTNode node) {
    final int line = this.astRootNode.getLineNumber(node.getStartPosition());
    final int startLine = MutaneratorConfig.SINGLETON.getStartLine();
    final int endLine = MutaneratorConfig.SINGLETON.getEndLine();
    return startLine <= line && line <= endLine;
  }
}
