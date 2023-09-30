# Full Stack Scala JVM on fly.io demo

This repository is an example of an application with backend and frontend both in Scala that can be deployed easily on [fly.io](https://fly.io/).

It features:

- frontend with [Laminar](https://laminar.dev/)
- frontend web-component library with [UI5](https://sap.github.io/ui5-webcomponents/)
- frontend packaging with [vitejs](https://vitejs.dev/)
- backend (on the JVM) with [cask](https://github.com/com-lihaoyi/cask)
- fat jar generation with [sbt-assembly](https://github.com/sbt/sbt-assembly)
- backend hot reload with [sbt-revolver](https://github.com/spray/sbt-revolver)
- JSON serialization with [circe](https://circe.github.io/circe/)

## Dev setup

To run this project, you will need installed

- npm (v16)
- sbt
- a jdk 11

The first time, run in one terminal:

```
cd frontend
npm ci
```

Then, in one terminal:

```
sbt
~frontend/fastLinkJS
```

In another terminal

```
sbt
~server/reStart
```

In a third (last) terminal

```
cd frontend
npm run dev
```

Then you can go to [`localhost:3000/static`](http://localhost:3000/static).

## Prod setup

You can package the whole application into a fat jar with

```
sbt packageApplication
```

Then you can run it with `java -jar dist/app.jar` then go to [`localhost:9000`](http://localhost:9000).

You can also build a Docker image for the app with (note that the tag name does not matter)

```
docker build --tag demoscalaflyio .
```

Then run it with

```
docker run --rm -p 9000:8080 demoscalaflyio
```

and then go to [`localhost:9000`](http://localhost:9000).

## Deploy to fly.io

The project contains a `fly.toml` file generated via `fly launch`.

You can apply it to your account via `fly deploy` and then see that it's live with `fly open`. (If not logged in already, you need to `fly auth login`.)

You need to have [flyctl](https://fly.io/docs/hands-on/install-flyctl/) installed for this to work.

You can see more complete instructions [here](https://fly.io/docs/languages-and-frameworks/dockerfile/).


## Source maps

`vite build` in `frontend` dir is configured to generate source maps. It calls `sbt frontend/fullLinkJS` on its own internally, and then applies production optimizations. Those source maps are good, it seems. Analysis can be viewed with `npx source-map-explorer dist/assets/index-<hash>.js`

Simply running `frontend/fullLinkJS` myself produces source maps that are invalid, source-map-explorer gives errors like "Your source map refers to generated column 20 on line 727, but the source only contains 19 column(s) on that line.
Check that you are using the correct source map." and requires that you specify the --no-border-checks option. 
