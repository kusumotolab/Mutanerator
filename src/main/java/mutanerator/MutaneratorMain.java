package mutanerator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class MutaneratorMain {

  public static void main(final String[] args) {

    // コマンドライン引数の処理
    final MutaneratorConfig config = MutaneratorConfig.SINGLETON;
    final CmdLineParser cmdLineParser = new CmdLineParser(config);
    try {
      cmdLineParser.parseArgument(args);
    } catch (final CmdLineException e) {
      cmdLineParser.printUsage(System.err);
      System.exit(1);
    }

    final Path targetFilePath = config.getPath();
    final ProgramElementCollectorBuilder builder = new ProgramElementCollectorBuilder();
    final ProgramElementCollector collector = builder.build(targetFilePath);
    collector.perform();
    final MutationTargets mutationTargets = collector.getMutationTargets();

    final Random random = new Random(config.getSeed());
    final int mutationsToBeGenerated = config.getMutants();
    final List<Mutation> mutations = new ArrayList<>();
    int duplicatedGeneration = 0;
    while (mutations.size() < mutationsToBeGenerated && duplicatedGeneration < 10) {
      final int randomValue = Math.abs(random.nextInt());
      final Mutation mutation = mutationTargets.select(randomValue);
      if (mutations.contains(mutation)) {
        duplicatedGeneration++;
      } else {
        mutations.add(mutation);
        duplicatedGeneration = 0;
      }
    }

    final CompilationUnit astRootNode = collector.getASTRootNode();
    final Map<Mutation, String> mutationTexts = new HashMap<>();
    for (final Mutation mutation : mutations) {
      final ASTNode copiedRootNode = ASTNode.copySubtree(astRootNode.getAST(), astRootNode);
      final String mutationText = mutation.apply(copiedRootNode, builder.getFileContent());
      mutationTexts.put(mutation, mutationText);
    }

    // 生成したミュータント（変異プログラム）を出力する
    final Path outputDir = config.getOutputDir();
    createDirectory(outputDir);
    final Path targetFileName = targetFilePath.getFileName();
    for (int index = 0; index < mutations.size(); index++) {
      final String text = mutationTexts.get(mutations.get(index));
      final Path mutationDir = outputDir.resolve(Integer.toString(index));
      createDirectory(mutationDir);
      final Path mutationFilePath = mutationDir.resolve(targetFileName);
      try {
        Files.writeString(mutationFilePath, text, StandardCharsets.UTF_8);
      } catch (final IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
  }

  static boolean createDirectory(final Path dir) {

    // すでにディレクトリが存在する場合は何もしない
    if (Files.isDirectory(dir)) {
      return false;
    }

    // ディレクトリが存在しない場合は作る
    try {
      Files.createDirectory(dir);
    } catch (final IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return true;
  }
}
