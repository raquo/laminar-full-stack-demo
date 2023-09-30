import { createHtmlPlugin } from 'vite-plugin-html'

import { defineConfig } from "vite";
import scalaJSPlugin from "@scala-js/vite-plugin-scalajs";
import rollupPluginSourcemaps from 'rollup-plugin-sourcemaps';

export default defineConfig(({ command, mode, ssrBuild }) => {

    const script = `<script type="module" src="./index.js"></script>`

    return {
        base: "/",
        publicDir: 'public', // #nc >>> Why did this stop working?????
        plugins: [
          scalaJSPlugin({
              cwd: '..',
              projectID: 'frontend'
          }),
          createHtmlPlugin({
            minify: process.env.NODE_ENV === 'production',
            inject: {
                data: {
                    script
                }
            }
          })
        ],
        build: {
            // outDir: "dist",
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
            // host: '0.0.0.0',
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
            logLevel: 'debug'
        }
    }
})
