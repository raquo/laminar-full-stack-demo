import {defineConfig} from "vite";
import scalaJSPlugin from "@scala-js/vite-plugin-scalajs";
import injectHtmlVarsPlugin from "./vite-plugins/inject-html-vars.js";
import rollupPluginSourcemaps from "rollup-plugin-sourcemaps";
import globResolverPlugin from "./vite-plugins/glob-resolver.js";
import importSideEffectPlugin from "./vite-plugins/import-side-effect.js";
import rollupCopyPlugin from 'rollup-plugin-copy'
import path from "path";

export default defineConfig(
  ({
    command,
    mode,
    ssrBuild
  }) => {
    return {
      base: "/",
      publicDir: "public",
      plugins: [
        scalaJSPlugin({
          cwd: "..", // path to build.sbt
          projectID: "frontend" // scala.js project name in build.sbt
        }),
        globResolverPlugin({
          cwd: __dirname,
          ignore: [
            'node_modules/**',
            'target/**'
          ]
        }),
        importSideEffectPlugin({
          // See comments in vite-plugins/import-side-effect.js
          importFnName: "importSideEffect_3DfPjKW0ZYyY"
        }),
        injectHtmlVarsPlugin({
          SCRIPT_URL: "./index.js"
        }),
        rollupCopyPlugin({
          copyOnce: true,
          targets: [
            {
              // If changing `dest`, you must also change the call to `Shoelace.setBasePath` in your Scala.js code.
              // Required by Shoelace: https://shoelace.style/getting-started/installation
              src: path.resolve(__dirname, 'node_modules/@shoelace-style/shoelace/dist/assets/icons/*.svg'),
              dest: path.resolve(__dirname, 'public/assets/shoelace/assets/icons')
            }
          ]
        })
      ],
      build: {
        outDir: "dist",
        assetsDir: "assets", // path relative to outDir
        // outDir: "../server/src/main/resources/static", // #TODO can we do this directly?
        // cssCodeSplit: false,  // false = Output entire CSS as a separate file
        rollupOptions: {
          plugins: [rollupPluginSourcemaps()],
        },
        minify: "terser",
        sourcemap: true
      },
      server: {
        port: 3000,
        strictPort: true,
        // host: "0.0.0.0",
        proxy: {
          // Note: we only proxy /api URLs to the server, if you need more,
          // you need to set that up.
          // #TODO Can't we just proxy everything except index.html and /app?
          // #TODO hide away the proxy config (do we even need it?)
          "/api": {
            target: "http://127.0.0.1:9000",
            secure: false,
            configure: (proxy, _options) => {
              proxy.on("error", (err, _req, _res) => {
                console.log("proxy error", err);
              });
              proxy.on("proxyReq", (proxyReq, req, _res) => {
                console.log("Sending Request to the Target:", req.method, req.url);
              });
              proxy.on("proxyRes", (proxyRes, req, _res) => {
                console.log(
                  "Received Response from the Target:",
                  proxyRes.statusCode,
                  req.url
                );
              });
            },
          }
        },
        logLevel: "debug"
      }
    }
  }
)
