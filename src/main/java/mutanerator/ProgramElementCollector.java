package mutanerator;

import java.util.Arrays;
import java.util.Stack;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ProgramElementCollector extends ASTVisitor {

  private final CompilationUnit astRootNode;
  private final MutationTargets mutationTargets;
  private final Stack<Boolean> flagsForPrimitiveReturns = new Stack<>();

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
    if (expression instanceof MethodInvocation) {
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
  public boolean visit(final MethodDeclaration node) {

    final Type returnType = node.getReturnType2();
    final boolean flagForPR = this.fitForPrimitiveReturns(returnType);
    this.flagsForPrimitiveReturns.push(flagForPR);

    return super.visit(node);
  }

  @Override
  public void endVisit(final MethodDeclaration node) {
    super.endVisit(node);
    this.flagsForPrimitiveReturns.pop();
  }

  private boolean fitForPrimitiveReturns(final Type returnType) {

    if (!(returnType instanceof PrimitiveType)) {
      return false;
    }

    final PrimitiveType primitiveReturnType = (PrimitiveType) returnType;
    final Code code = primitiveReturnType.getPrimitiveTypeCode();
    switch (code.toString()) {
      case "int":
      case "short":
      case "long":
      case "char":
      case "float":
      case "double":
        return true;
      default:
        return false;
    }
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

  @Override
  public boolean visit(final ReturnStatement node) {

    // 変異の範囲外である場合は何もしない
    if (!this.inTargetRange(node)) {
      return super.visit(node);
    }

    // PrimitiveReturns の対象メソッドの場合
    if (this.flagsForPrimitiveReturns.peek()) {

      final Expression expression = node.getExpression();

      // expression が NumberLiteral の場合のみ対象
      if (null != expression && expression instanceof NumberLiteral) {

        // return 文のオペランドがすでに0の場合は PrimitiveReturns の対象にならない
        final String token = ((NumberLiteral) expression).getToken();
        switch (token.toUpperCase()) {
          case "0":
          case "0D":
          case "0F":
          case "0L":
          case "0.0":
          case "0.0D":
          case "0.0F":
            break;
          default:
            this.mutationTargets.put(Mutator.PrimitiveReturns, expression);
            break;
        }
      }
    }

    return super.visit(node);
  }

  @Override
  public boolean visit(final TypeDeclaration node) {
    final MethodDeclaration[] methods = node.getMethods();
    if (Arrays.stream(methods)
        .anyMatch(m -> "main".equals(m.getName()))) {
      return super.visit(node);
    }

    return false;
  }

  @Override
  public boolean visit(final ForStatement node) {
    final Statement body = node.getBody();
    if (null != body) {
      body.accept(this);
    }

    return false;
  }

  private boolean inTargetRange(final ASTNode node) {
    final int line = this.astRootNode.getLineNumber(node.getStartPosition());
    final int startLine = MutaneratorConfig.SINGLETON.getStartLine();
    final int endLine = MutaneratorConfig.SINGLETON.getEndLine();
    return startLine <= line && line <= endLine;
  }
}
