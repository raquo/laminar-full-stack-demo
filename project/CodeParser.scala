import scala.collection.mutable

// This file has no JVM dependencies, so we could even use it on the frontend if needed.
// We would just need to make sure the regex is compatible with JS.
// Note: nested snippets are not supported, in those cases only the outer one will be matched.
object CodeParser {

  class BeginComment(val key: String, val lineNumber: Int, val whitespacePrefix: String)

  class EndComment(val key: String, val lineNumber: Int)

  // Matches lines like `// BEGIN[foo]`, capturing the whitespace indent / prefix, and `foo`.
  private val beginPattern = """^([ \t]*)//(?:\s*)BEGIN\[([^]]+)]""".r

  // Matches lines like `// END[foo]`, capturing `foo`.
  private val endPattern = """^(?:[ \t]*)//(?:\s*)END\[([^]]+)]""".r

  def extractSnippets(
    filePath: String,
    fileName: String,
    fileLanguage: String,
    fileContent: String,
    snippetsByKey: mutable.Map[String, mutable.Buffer[SbtCodeSnippet]]
  ): Unit = {

    val beginComments = mutable.ArrayBuffer.empty[BeginComment]
    val endComments = mutable.ArrayBuffer.empty[EndComment]

    fileContent.linesIterator.zipWithIndex.foreach { case (line, index) =>
      line match {
        case beginPattern(whitespacePrefix, key) =>
          beginComments += new BeginComment(key, lineNumber = index + 1, whitespacePrefix)
        case endPattern(key) =>
          endComments += new EndComment(key, lineNumber = index + 1)
        case _ => // do nothing
      }
    }

    // Step 2: Pair up the BEGIN and END tags.
    beginComments.foreach { beginComment =>
      val key = beginComment.key
      val endIdx = endComments.indexWhere { endComment =>
        endComment.key == key && endComment.lineNumber > beginComment.lineNumber
      }
      if (endIdx != -1) {
        val endComment = endComments(endIdx)
        endComments.remove(endIdx)

        val startLineNumber = beginComment.lineNumber + 1
        val endLineNumber = endComment.lineNumber - 1
        val snippetLines = fileContent.linesIterator
          .slice(startLineNumber - 1, endLineNumber)
          .map { line =>
            if (line.startsWith(beginComment.whitespacePrefix))
              line.substring(beginComment.whitespacePrefix.length)
            else
              line
          }
          .toList
        val snippet = SbtCodeSnippet(
          filePath = filePath,
          fileName = fileName,
          fileLanguage = fileLanguage,
          startLineNumber = startLineNumber,
          endLineNumber = endLineNumber,
          key = beginComment.key,
          lines = snippetLines
        )
        if (snippetsByKey.contains(key)) {
          snippetsByKey(key) += snippet
        } else {
          snippetsByKey.update(key, mutable.Buffer(snippet))
        }
      } else {
        // Just printing to the console because I don't want to disturb development process.
        // #TODO But maybe we can emit a better formatted warning somehow?
        println(s"WARNING: Closing code snippet comment `// END[${beginComment.key}]` not found in ${filePath} - the opening comment on line ${beginComment.lineNumber} has no match.")
      }
    }

    // endComment should be empty at this point
    endComments.foreach { endComment =>
      println(s"WARNING: Opening code snippet comment `// BEGIN[${endComment.key}]` not found in ${filePath} - the closing comment on line ${endComment.lineNumber} has no match.")
    }
  }

  // This can't handle overlapping snippets, or even just multiple snippets for the same key.
  // It matches greedily, and I couldn't make negative look-ahead work.
  //
  //private val pattern = """(?m)([ \t]*)// BEGIN\[([^]]+)]([\s\S]*)// END\[\2]""".r
  //
  //def extractSnippetsOld(
  //  filePath: String,
  //  fileName: String,
  //  fileContent: String
  //): mutable.Map[String, mutable.Buffer[CodeSnippet]] = {
  //  val snippetsByKey = mutable.Map[String, mutable.Buffer[CodeSnippet]]()
  //
  //  pattern.findAllIn(fileContent).matchData.foreach {
  //    case m if m.groupCount >= 3 =>
  //      val key = m.group(2)
  //      val leadingWhitespace = m.group(1)
  //      val contentWithoutLeadingWhitespace = m.group(3)
  //        .split("\n")
  //        .map(line => line.replaceFirst(s"^$leadingWhitespace", ""))
  //
  //      val startLineNumber = fileContent.substring(0, m.start(3)).count(_ == '\n') + 1 + 1
  //      val endLineNumber = fileContent.substring(0, m.end(3)).count(_ == '\n')
  //      println(key, startLineNumber, endLineNumber)
  //
  //      val snippet = CodeSnippet(
  //        filePath = filePath,
  //        fileName = fileName,
  //        startLineNumber = startLineNumber,
  //        endLineNumber = endLineNumber,
  //        key = key,
  //        lines = contentWithoutLeadingWhitespace
  //      )
  //
  //      if (snippetsByKey.contains(key)) {
  //        snippetsByKey(key).append(snippet)
  //      } else {
  //        snippetsByKey.update(key, mutable.Buffer(snippet))
  //      }
  //
  //    case m =>
  //      println(s"WARNING: Bad code snippet match in file ${filePath}. Captured groups (expected indent, key, content):\n${m.subgroups}")
  //  }
  //
  //  snippetsByKey
  //}
}
