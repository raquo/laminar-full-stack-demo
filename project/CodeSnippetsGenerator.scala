import java.io.{File, FileOutputStream, PrintStream}
import java.nio.file.*
import com.raquo.domtypes.codegen.generators.SourceGenerator
import com.raquo.domtypes.codegen.CodeFormatting

// BEGIN[codesnippets/generator]
object CodeSnippetsGenerator extends SourceGenerator(CodeFormatting()) {

  private var maybeLastSnippets: Option[Map[String, List[SbtCodeSnippet]]] = None

  // #Note: All the files under `./project` are compile-time files.
  //  You need to do `sbt reload` after making changes to these files
  //  for those changes to take effect.

  /** Scan the codebase for code snippets (// BEGIN[key] and // END[key] comments),
    * and write the snippets into the designated scala file.
    * We use the snippets on the frontend to display the code of the examples.
    */
  def generate(rootPath: Path, targetPath: Path, packageName: String, objectName: String): Unit = {
    val snippetsByKey = CodeBrowser.findCodeSnippets(rootPath)
    // #Note: we're relying on structural comparison using `==`
    //  to prevent unnecessary code generation when there are no
    //  changes in snippets. Should reduce file watching thrashing.
    if (!maybeLastSnippets.contains(snippetsByKey)) {
      //println(">>> Generating")
      maybeLastSnippets = Some(snippetsByKey)
      printFile(
        packageName = packageName,
        objectName = objectName,
        snippetsByKey = snippetsByKey
      )
      writeToFile(
        filePath = targetPath.resolve(objectName + ".scala"),
        fileContent = getOutput()
      )
    } else {
      //println("-- no changes --")
    }
  }

  def printFile(
    packageName: String,
    objectName: String,
    snippetsByKey: Map[String, List[SbtCodeSnippet]]
  ): Unit = {
    line(s"package ${packageName}")
    line("")
    line("import com.raquo.app.codesnippets.CodeSnippet")
    line("import vendor.highlightjs.hljs.LanguageName")
    line("")
    line("/** This file is generated at compile-time by CodeSnippetsGenerator.scala */")
    enter(s"object ${objectName} {", "}") {
      line("")
      snippetsByKey.keys.toList.sorted.foreach { key =>
        enter(s"val `$key` = List(", ")") {
          snippetsByKey(key).foreach { snippet =>
            line(snippetRepr(snippet) + ",")
          }
        }
        line("")
      }
    }
  }

  def snippetRepr(
    snippet: SbtCodeSnippet
  ): String = {
    val fields = List(
      repr(snippet.filePath),
      repr(snippet.fileName),
      repr(snippet.fileLanguage) + ".asInstanceOf[LanguageName]",
      snippet.startLineNumber, // #TODO Add repr method for int in SDT
      snippet.endLineNumber,
      repr(snippet.key),
      snippet.lines.map(repr(_)).mkString("List(", ", ", ")"),
    )
    fields.mkString(s"CodeSnippet(", ", ", ")")
  }
  // END[codesnippets/generator]

  def writeToFile(filePath: Path, fileContent: String): File = {
    // Fun fact: if you print anything from sbt (specifically, from the
    // client/fastLinkJS task run by scalajs-vite plugin), make sure
    // to add a trailing newline, otherwise vite will break.
    // So, println() - good, print() - bad.
    //println("> WRITE > " + filePath.toString + " (" + fileContent.length + " chars)")
    val outputFile = new File(filePath.toString)
    outputFile.getParentFile.mkdirs()

    val fileOutputStream = new FileOutputStream(outputFile)
    val outputPrintStream = new PrintStream(fileOutputStream)

    outputPrintStream.print(fileContent)
    outputPrintStream.flush()

    // Flush written file contents to disk https://stackoverflow.com/a/4072895/2601788
    fileOutputStream.flush()
    fileOutputStream.getFD.sync()

    outputPrintStream.close()

    outputFile
  }
}
