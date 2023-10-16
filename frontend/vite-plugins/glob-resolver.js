import {globSync} from "glob";
import path from "path";

/**
 * In raw JS or Typescript, you can import local resources with a relative path,
 * for example if you have foo.js and foo.css in the same directory, you can
 * say `import "./foo.css"` in foo.js, and this will work. This is useful to
 * load CSS styles only for the components that you're actually using.
 *
 * However, in Scala.js we can't do this, because Scala.js combines multiple
 * .scala source files in the directory tree into a flat list of JS files,
 * which means that the relative paths in such resource imports don't work
 * anymore when executed in the context of the resulting JS files.
 *
 * See reasons for this here: #TODO <link> to discord
 *
 * And so, our approach to avoid manually writing out very long paths is to
 * have Vite locate the imported files by a compact glob pattern. Usually
 * you can simply import "@find/**\/foo.css", but if your resources have
 * non-unique file names, you will need to provide a more specific glob
 * pattern to disambiguate.
 */
export default function globResolverPlugin(globOptions) {
  if (globOptions === null || !globOptions.cwd || !globOptions.ignore) {
    throw new Error("globResolverPlugin: you must provide globOptions (at least `cwd` and `ignore`) to configure the glob search. See https://www.npmjs.com/package/glob")
  }
  const prefix = '@find/'
  return {
    name: 'glob-resolver',
    resolveId (sourcePath) {
      // console.log(">>>" + sourcePath)
      if (sourcePath.startsWith(prefix)) {
        const globPattern = sourcePath.substring(prefix.length);
        const matchedFiles = globSync(globPattern, globOptions);

        if (matchedFiles.length === 0) {
          throw new Error(`Unable to @find pattern ${globPattern}`);
        } else if (matchedFiles.length > 1) {
          throw new Error(`Ambiguous @find pattern ${globPattern}, found multiple matches:\n> ${matchedFiles.join("\n> ")}\nPlease use a more specific glob pattern.`);
        } else {
          const matchedFile = path.resolve(globOptions.cwd, matchedFiles[0])
          // console.log(">>>" + matchedFile)
          return matchedFile;
        }
      }
      return null; // Let Vite handle all other imports
    }
  };
}
