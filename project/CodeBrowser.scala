import java.nio.file.{Files, Path}
import scala.collection.JavaConverters.*
import scala.collection.mutable

object CodeBrowser {

  /** Format: file extension -> highlight.js language name */
  final val fileExtensionsToHighlightJsNames = Map(
    ".scala" -> "scala",
    ".sbt" -> "scala",
    ".less" -> "less",
    ".css" -> "css",
    ".js" -> "javascript"
  )

  /** We will only look into files with these extensions */
  final val includeFileExtensions = fileExtensionsToHighlightJsNames.keys.toList

  /** We will ignore any directory named exactly like this (names only, paths not supported) */
  final val ignoreDirectoryNames = Set(
    "target",
    "node_modules",
    "dist",
    "resources",
    ".idea",
    ".vscode"
  )

  def listFilesRecursive(path: Path): List[Path] = {
    if (!Files.exists(path)) {
      throw new Exception(s"listFilesRecursive: ${path.toAbsolutePath} does not exist")
    } else {
      walk(path)
    }
  }

  private def walk(path: Path): List[Path] = {
    val file = path.toFile
    val fileName = file.getName
    if (Files.isDirectory(path)) {
      if (ignoreDirectoryNames.contains(fileName)) {
        Nil
      } else {
        var stream: java.util.stream.Stream[Path] = null
        try {
          stream = Files.list(path)
          stream.iterator().asScala.flatMap(walk).toList
        } finally {
          if (stream != null) stream.close()
        }
      }
    } else {
      if (includeFileExtensions.exists(fileName.endsWith)) {
        List(path)
      } else {
        Nil
      }
    }
  }

  def findCodeSnippets(rootPath: Path): Map[String, List[SbtCodeSnippet]] = {
    val files = listFilesRecursive(rootPath)
    val snippetsByKey = mutable.Map[String, mutable.Buffer[SbtCodeSnippet]]()
    files.foreach { file =>
      extractCodeSnippets(file, snippetsByKey)
    }
    snippetsByKey.mapValues(_.toList).toMap
  }

  def extractCodeSnippets(filePath: Path, snippetsByKey: mutable.Map[String, mutable.Buffer[SbtCodeSnippet]]): Unit = {
    //println("> READ > " + filePath)
    val fileContent = Files.readString(filePath)
    val fileName = filePath.toFile.getName
    val dotExtension = fileName.substring(fileName.lastIndexOf("."))
    CodeParser.extractSnippets(
      filePath = filePath.toString,
      fileName = fileName,
      fileLanguage = fileExtensionsToHighlightJsNames(dotExtension),
      fileContent = fileContent,
      snippetsByKey = snippetsByKey
    )
  }
}
