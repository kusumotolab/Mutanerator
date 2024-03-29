package mutanerator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ProgramElementCollectorBuilder {

  private String fileContent = null;

  public ProgramElementCollector build(final Path path) {

    // ファイル読込
    try {
      this.fileContent = Files.readString(path, StandardCharsets.UTF_8);
    } catch (final IOException e) {
      System.err.println("failed to read " + path.toString());
      System.exit(1);
    }

    // ASTの構築
    final ASTParser parser = createNewParser();
    parser.setSource(this.fileContent.toCharArray());
    final CompilationUnit ast = (CompilationUnit) parser.createAST(null);

    // 与えられたASTに問題があるときは何もしない
    final IProblem[] problems = ast.getProblems();
    if (null == problems || 0 < problems.length) {
      System.err.println("parse error: " + path.toString());
      System.exit(0);
    }

    // Collectorを生成
    final ProgramElementCollector collector = new ProgramElementCollector(ast);
    return collector;
  }

  public ProgramElementCollector build(final String text) {

    // ASTの構築
    final ASTParser parser = createNewParser();
    parser.setSource(text.toCharArray());
    final CompilationUnit ast = (CompilationUnit) parser.createAST(null);

    // Collectorを生成
    final ProgramElementCollector collector = new ProgramElementCollector(ast);
    return collector;
  }


  private ASTParser createNewParser() {
    ASTParser parser = ASTParser.newParser(AST.JLS13);
    final JavaVersion javaVersion = MutaneratorConfig.SINGLETON.getJavaVersion();
    final Map<String, String> options = javaVersion.getOptions();
    parser.setCompilerOptions(options);

    // TODO: Bindingが必要か検討
    parser.setResolveBindings(false);
    parser.setBindingsRecovery(false);
    parser.setEnvironment(null, null, null, true);

    return parser;
  }

  public String getFileContent() {
    return this.fileContent;
  }
}
