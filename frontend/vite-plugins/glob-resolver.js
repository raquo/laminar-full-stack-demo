import {globSync} from "glob";

export default function globResolverPlugin(options) {
  const prefix = '@find/'
  return {
    name: 'glob-resolver',
    resolveId (sourcePath) {
      // console.log(">>>" + sourcePath)
      if (sourcePath.startsWith(prefix)) {
        const globPattern = sourcePath.substring(prefix.length);
        const matchedFiles = globSync(globPattern, options);

        if (matchedFiles.length === 0) {
          throw new Error(`Unable to @find pattern ${globPattern}`);
        } else if (matchedFiles.length > 1) {
          throw new Error(`Ambiguous @find pattern ${globPattern}, found multiple matches:\n> ${matchedFiles.join("\n> ")}\nPlease use a more specific glob pattern.`);
        }

        return matchedFiles[0] || null;
      }
      return null; // Let Vite handle all other imports
    }
  };
}
