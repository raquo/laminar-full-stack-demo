// Because we need this class at compile time, we can't put it in `shared`,
// so the frontend, where we also need this class, has its own copy of this
// data structure, `case class CodeSnippet`.
case class SbtCodeSnippet(
  filePath: String,
  fileName: String,
  fileLanguage: String,
  startLineNumber: Int,
  endLineNumber: Int,
  key: String,
  lines: List[String]
)
