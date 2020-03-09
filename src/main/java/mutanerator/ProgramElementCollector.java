package mutanerator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

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
  public boolean visit(final InfixExpression node) {

    // 変異の範囲外である場合は何もしない
    if (!this.inTargetRange(node)) {
      return super.visit(node);
    }

    final Operator operator = node.getOperator();

    // ConditionalsBoundary変異の対象として登録
    if (operator.equals(Operator.GREATER) || operator.equals(Operator.GREATER_EQUALS)
        || operator.equals(Operator.LESS) || operator.equals(Operator.LESS_EQUALS)) {
      this.mutationTargets.put(Mutator.ConditionalsBoundary, node);
    }

    // Math変異の対象として登録
    if (operator.equals(Operator.PLUS) || operator.equals(Operator.MINUS)
        || operator.equals(Operator.TIMES) || operator.equals(Operator.DIVIDE)
        || operator.equals(Operator.REMAINDER) || operator.equals(Operator.AND)
        || operator.equals(Operator.OR) || operator.equals(Operator.XOR)
        || operator.equals(Operator.LEFT_SHIFT) || operator.equals(Operator.RIGHT_SHIFT_SIGNED)
        || operator.equals(Operator.RIGHT_SHIFT_UNSIGNED)) {
      this.mutationTargets.put(Mutator.Math, node);
    }

    // NegateConditionals変異の対象として登録
    if (operator.equals(Operator.EQUALS) || operator.equals(Operator.NOT_EQUALS)
        || operator.equals(Operator.LESS_EQUALS) || operator.equals(Operator.GREATER_EQUALS)
        || operator.equals(Operator.LESS) || operator.equals(Operator.GREATER)) {
      this.mutationTargets.put(Mutator.NegateConditionals, node);
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
