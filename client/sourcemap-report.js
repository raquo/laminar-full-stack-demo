import fs from "fs"
import path from "path"
import childProcess from "child_process"

// Note: this script requires source-map-explorer package to be installed globally.
// npm i -g source-map-explorer
// npm list -g --depth=0 // see the list of globally installed npm packages

// If calling this script from `npm run sourcemap-report`, and you want to pass
// arguments to it, you need to use `--`, e.g. `npm run sourcemap-report -- foo.js`

// -- PARAMS --

const directory = path.join("dist", "assets")
const filePattern = /^index-[a-z0-9]+\.js$/

// -- HELPERS --

function getJsBundleFilePath() {
  if (!fs.existsSync(directory)) {
    console.error(`Error: Directory ${directory} does not exist! See sourcemap-report.js`)
    process.exit(1)
  }

  const files = fs.readdirSync(directory)
  const matchingFiles = files.filter(file => filePattern.test(file))

  if (matchingFiles.length === 0) {
    console.error(`Error: No matching files found in directory ${directory}`)
    process.exit(1)
  } else if (matchingFiles.length > 1) {
    console.error(`Error: Multiple matching files found: ${matchingFiles.join(", ")}`)
    process.exit(1)
  }

  return path.join(directory, matchingFiles[0])
}

// -- SCRIPT --

let args = process.argv
args.shift() // remove path to node.js
args.shift() // remove this script's name

// If JS file name is not provided, find and inject the bundle JS file path automatically
const indexOfJsFilename = args.findIndex(arg => arg.toLowerCase().endsWith(".js"))
if (indexOfJsFilename === -1) {
  args.unshift(getJsBundleFilePath())
} else if (!fs.existsSync(indexOfJsFilename)) {
  console.error(`Error: File ${args[indexOfJsFilename]} does not exist.`)
  process.exit(1)
}

const command = `source-map-explorer ${args.join(" ")}`

console.log("> " + command + "\n")

childProcess.exec(
  command,
  (error, stdout, stderr) => {
    if (error) {
      console.error(error)
      return
    }
    console.log(stdout)
    console.error(stderr)
  }
)
