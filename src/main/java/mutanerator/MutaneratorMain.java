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
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import com.google.common.base.Strings;

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

    // getting a list of mutations from a given program
    // ミューテーションの一覧を得る
    final Path targetFilePath = config.getPath();
    final ProgramElementCollectorBuilder builder = new ProgramElementCollectorBuilder();
    final ProgramElementCollector collector = builder.build(targetFilePath);
    collector.perform();
    final MutationTargets mutationTargets = collector.getMutationTargets();
    final List<Mutation> mutations = mutationTargets.getMutations();

    // identifying possible mutants by using the mutations
    // ミュータントを生成する
    final int minimumMutations = config.getMinimumMutations();
    final int maximumMutations = config.getMaximumMutations();
    if (maximumMutations < minimumMutations) {
      System.err.println(
          "specified minimum mutations per mutant is larger than maximum mutations.");
      System.exit(1);
    }
    final List<Mutant> mutants = Mutant.generateMutants(mutations, minimumMutations,
        maximumMutations);

    // thinning out if identified mutants are too much
    // 生成したミュータントが多すぎる場合は間引く
    final Random random = new Random(config.getSeed());
    final int mutationsToBeGenerated = config.getMutants();
    while (mutationsToBeGenerated < mutants.size()) {
      final int index = random.nextInt(mutants.size());
      mutants.remove(index);
    }

    // generating texts of mutants
    // ミュータントのテキストを生成する
    final CompilationUnit astRootNode = collector.getASTRootNode();
    final Map<Mutant, String> mutantTexts = new HashMap<>();
    for (final Mutant mutant : mutants) {
      final ASTNode copiedRootNode = ASTNode.copySubtree(astRootNode.getAST(), astRootNode);
      final String mutantText = mutant.getText(copiedRootNode, builder.getFileContent());
      mutantTexts.put(mutant, mutantText);
    }

    // outputting generated texts
    // 生成したミュータント（変異プログラム）を出力する
    final Path outputDir = config.getOutputDir();
    createDirectory(outputDir);
    final Path targetFileName = targetFilePath.getFileName();
    final int digitNumber = Integer.toString(mutants.size())
        .length();
    for (int index = 0; index < mutants.size(); index++) {
      final String mutantDirName = Strings.padStart(Integer.toString(index + 1), digitNumber, '0');
      final Path mutantDir = outputDir.resolve(mutantDirName);
      createDirectory(mutantDir);
      final Path mutantFilePath = mutantDir.resolve(targetFileName);
      final String text = mutantTexts.get(mutants.get(index));
      try {
        Files.writeString(mutantFilePath, text, StandardCharsets.UTF_8);
      } catch (final IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    // generating a log file
    // ログファイルを出力する
    final Path logFile = config.getLogFile();
    if (null != logFile) {
      final List<String> lines = new ArrayList<>();
      for (int index = 0; index < mutants.size(); index++) {
        final Mutant mutant = mutants.get(index);
        for (final String mutationText : mutant.getLog()) {
          final String mutantID = Strings.padStart(Integer.toString(index + 1), digitNumber, '0');
          lines.add(mutantID + ", " + mutationText);
        }
      }
      try {
        Files.write(logFile, lines, StandardCharsets.UTF_8);
      } catch (final IOException e) {
        e.printStackTrace();
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
