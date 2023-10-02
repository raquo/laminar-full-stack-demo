import {defineConfig} from "vite";
import scalaJSPlugin from "@scala-js/vite-plugin-scalajs";
import injectHtmlVarsPlugin from "./vite-plugins/inject-html-vars.js"
import rollupPluginSourcemaps from "rollup-plugin-sourcemaps";

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
          cwd: "..",
          projectID: "frontend"
        }),
        injectHtmlVarsPlugin({
          SCRIPT_URL: "./index.js",
          SCRIPT_URL_2: "./index2.js"
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
        // host: "0.0.0.0",
        proxy: {
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
