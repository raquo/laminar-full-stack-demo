import { createHtmlPlugin } from 'vite-plugin-html'

import { defineConfig } from 'vite'

const scalaVersion = "3.3.1"

export default defineConfig(({ command, mode, ssrBuild }) => {

    const htmlPlugin = createHtmlPlugin()

    const mainJS = `/target/scala-${scalaVersion}/frontend-${mode === 'production' ? 'opt' : 'fastopt'}/main.js`
    console.log('mainJS', mainJS)
    const script = `<script type="module" src="${mainJS}"></script>`

    return {
        publicDir: './public',
        plugins: createHtmlPlugin({
            minify: process.env.NODE_ENV === 'production',
            inject: {
                data: {
                    script
                }
            }
        }),
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
        },
        base: "/static/"
    }
})
