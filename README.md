![](https://laminar.dev/img/brand/laminar-logo-100px-rounded.png)

# Laminar & Scala.js Full Stack Demo

This repository is an example [Laminar](https://laminar.dev/) [Scala.js](https://www.scala-js.org/) application complete with a [Vite](https://vitejs.dev/) dev server, [http4s](https://http4s.org/) backend, docker deployment of production builds, and many other features. It mainly focuses on the following aspects:

1. Laminar examples
2. Scala.js <> JS interop patterns
3. Scala.js <> JVM interop patterns
4. Build config for dev & prod + packaging

More details below.


## Live demo

### 👉 [demo.laminar.dev](https://demo.laminar.dev)

It's hosted on Fly.io for free, so the server may take a few seconds to boot up if it hasn't been used recently.


## Fun funding fact

The development of learning materials like this is made possible by [Laminar sponsors](https://github.com/sponsors/raquo).


### DIAMOND sponsor:

[![HeartAI.net](https://laminar.dev/img/sponsors/heartai-300px.png)](https://www.heartai.net/)

[HeartAI](https://www.heartai.net/) is a data and analytics platform for digital health and clinical care.


### GOLD Sponsors:

[![Yurique](https://laminar.dev/img/sponsors/yurique-50px.jpg?)](https://github.com/yurique) **[Iurii Malchenko](https://github.com/yurique)**
<br />
<br />
[![Aurinko.io](https://laminar.dev/img/sponsors/aurinko-light-250px.png)](https://www.aurinko.io/)
<br />
<br />
[Aurinko](https://aurinko.io/) is an API platform for workplace addons and integrations.
<br />
<br />
<a href="https://tawasal.ae"><img src="https://laminar.dev/img/sponsors/tawasal.svg" width="80"></a><br />
<b><a href="https://tawasal.ae">Tawasal</a></b> is a secure multi-purpose messenger and superapp, offering free voice, text, video conferencing and lifestyle services.


## Table of Contents

* [Development Setup](#development-setup)
* [Production Setup](#production-setup)
  * [Deploy to Fly.io](#deploy-to-flyio)
* [How To Use This Repo](#how-to-use-this-repo)
* [Features](#features)
* [Documentation](#documentation)
  * [Suggested CSS Styling Strategy](#suggested-css-styling-strategy)
  * [Scala 3](#scala-3)
* [Not Included](#not-included)
  * [ScalablyTypes](#scalablytyped)
  * [Your Favourite Backend Web Framework](#your-favourite-backend-web-framework)
  * [Injecting A Scala.js App Into Server-Generated HTML](#injecting-a-scalajs-app-into-server-generated-html)
  * [Not Tested On Windows](#not-tested-on-windows)
* [Wishlist](#wishlist)

## Development Setup

To run this project, you will need to have installed:

- node.js (latest LTS, e.g. v20)
- JDK (latest LTS, e.g. v17)
- sbt (latest 1.x)

Note: There are essentially two versions of sbt: the version used in the project, and the version of the sbt script that you've installed back in the day when you first started using sbt on your computer. The project sbt version defined in our `build.properties` file, but the script sbt version does not get auto-updated, so it could be very old. If it's 0.13.x, you can face weird build errors. Check using `sbt --version`, and go install the latest sbt 1.x if you see script version 0.13.x.

Note: you don't need to install Scala yourself: sbt manages all Scala dependencies according to our `build.sbt` file, including the Scala compiler itself.

The first time only, to install JS dependencies, run this in your terminal:

```
cd client
npm ci
```

Then, open another terminal window, and run there:

```
sbt
~client/fastLinkJS
```

Then, back in the first terminal, run:

```
npm run dev
```

All of the above commands will install your JS and Scala dependencies, and start the Vite dev server (`npm run dev`) along with incremental Scala compilation of your frontend (`~client/fastLinkJS`). At this point, you can already open the app at
[`localhost:3000/`](http://localhost:3000/), and everything _that doesn't need a backend_ will work, which in our case, are most pages.

The wind gradient pages, on the other hand, demonstrate interop with the backend, so to view them, you need to start the http4s server.

Open yet another terminal window, and do this:

```
sbt
~server/reStart
```

Now you have Vite dev server serving your frontend and hot-reloading your frontend assets, and sbt-revolver hot-reloading your server when you make changes to your code.

Once you're familiar with this setup, feel free to use sbt shorthand command aliases that I defined in the `build.sbt` file: `cup` for `~client/fastLinkJS`, and `sup` for `~server/reStart`. Save your fingers / wrists.

In production, there is no Vite dev server, so to test your site in an environment that matches real life more closely, yet without needing to package it into a jar or deploy it to Docker, you can run `buildClient` in sbt, and start the server with `~server/reStart`. Your site will start on localhost:9000 with frontend assets built for production, without any Vite stuff like hot reload on the frontend. This workflow is too slow for frontend development, generally you should use Vite dev server, and only call `buildClient` when you need a sanity check for a particular issue or a Vite config change, or before deploying.



## Production Setup

You can package the whole application into a fat jar with

```
sbt packageApplication
```

Then you can run it with `java -jar dist/app.jar` then go to [`localhost:9000`](http://localhost:9000).

You can also build a Docker image for the app with (note that the tag name does not matter)

```
docker build --tag laminar-demo .
```

Then run it with

```
docker run --rm -p 9000:8080 laminar-demo
```

and then go to [`localhost:9000`](http://localhost:9000). Of course, you'll need to install and launch Docker for all `docker` commands.


### Deploy to Fly.io

(First, you need to package the application into a fat jar, as explained above, using `sbt packageApplication`.)

The project contains a `fly.toml` file originally generated via the `fly launch` command.

You can apply it to your account via `fly deploy --ha=false` and then see that it's live with `fly open`. (If not logged in already, you need to authenticate with `fly auth login`.)

You need to have [flyctl](https://fly.io/docs/hands-on/install-flyctl/) installed for this to work.

You can see more complete instructions [here](https://fly.io/docs/languages-and-frameworks/dockerfile/).

This demo app is actually deployed on a free Fly.io container ([laminar-demo.fly.dev](https://laminar-demo.fly.dev)), and I also set up a custom domain using [these instructions](https://fly.io/blog/how-to-custom-domains-with-fly/), so that it's available on [demo.laminar.dev](https://demo.laminar.dev).



## How To Use This Repo

Install it and run it locally in dev mode as shown above. Then, explore the source code. I left a lot of prose comments explaining some of the patterns that I've used, and I was trying to use a variety of patterns just to show them off.

Make sure you're using a good IDE that 1) lets you go-to-definition on every term and type that you see, and 2) gives actually correct autocomplete suggestions. My preference so far is IntelliJ, despite its incomplete Scala 3 support, but VS Code with [Metals](https://scalameta.org/metals/docs/) is another popular alternative. If you don't have go-to-definition, and you don't have autocomplete that shows you which props and methods are actually available in a given context, your Scala development experience will be, frankly, subpar, because all the Scala APIs that you use will appear like puzzling black boxes that require you to obtain answers somewhere else, whereas many answers are available straight in their code.



## Features


### [Laminar](https://laminar.dev/) examples

* TodoMVC example
* Chart.js example
* Integration with Web Components ([SAP UI5](https://github.com/sherpal/LaminarSAPUI5Bindings) and [Shoelace](https://shoelace.style/))
* Network requests to JSON API
* [Waypoint](https://github.com/raquo/Waypoint) clientside URL routing


### Scala.js <> JS interop

* Styling with scoped CSS using [LESS](https://lesscss.org/)
* Manually crafted facades for JS libraries (chart.js)
* Importing JS Web Components (SAP UI5, Shoelace)
* Including arbitrary JS files and CSS resources

### Scala.js <> JVM interop

* Cross-build multi-project sbt setup (client + server + shared)
* Shared data models
* JSON serialization using [Borer](https://github.com/sirthias/borer) JSON library
* Backend support for clientside [Waypoint](https://github.com/raquo/Waypoint) URL routing
* [sbt-buildinfo](https://github.com/sbt/sbt-buildinfo) to get compile-time data from sbt to scala.js

### [Vite](https://vitejs.dev/) setup

* Special Scala.js vite plugins to work with per-component CSS / LESS files
  * Import JS / CSS / LESS file assets for their side effects without Vite warnings
    * [@raquo/vite-plugin-import-side-effect](https://github.com/raquo/vite-plugin-import-side-effect)
  * Import files from relative paths (or an approximation of that)
    * [@raquo/vite-plugin-glob-resolver](https://github.com/raquo/vite-plugin-glob-resolver)
* Minification and code transformations preserving source maps
  * To view source maps, run `npm run build`, then `npm run sourcemap-report`. You must install `source-map-explorer` globally first: `npm i -g source-map-explorer`.
* Proxying api calls to your backend
* Static assets like svg icons, static files like robots.txt and favicon
* Hot reload for Scala.js and LESS / CSS


### Backend

* [http4s](https://http4s.org/) serving SPA routes, API routes, static assets, and a 404 fallback route
* Fetching and parsing XML weather data from Environment Canada API


### Sbt setup

* Generating complex Scala.js code with compile-time data
* Passing simple compile-time data to Scala.js using [sbt-buildinfo](https://github.com/sbt/sbt-buildinfo)
* Hot server reload using [sbt-revolver](https://github.com/spray/sbt-revolver)


### Production build

* Packaging frontend & backend assets & code in a far jar using [sbt-assembly](https://github.com/sbt/sbt-assembly)
* Deploying the fat jar in a docker container (e.g. on [fly.io](https://fly.io/))



## Documentation

### Suggested CSS styling strategy

First, read [Approaches to CSS](https://laminar.dev/documentation#approaches-to-css) section of Laminar docs. Now, with that in mind, let me explain the styling pattern I'm using in this project.

In this repo, I apply some styles inline with Laminar methods, e.g. `listStyles` in `HomePageView.scala`, and some of the styling is coming from Web Components (on UI5 and Shoelace pages), JS libraries (Chart.js charts) or external stylesheets (TodoMVC standard stylesheet).

The bulk of the styling however is coming from my `.less` files. These are processed by [LESS](https://lesscss.org/) into CSS. The syntax is very much like regular CSS, but with several quality-of-life improvements. 

I have one `style.less` file which contains styles of general applicability, for example I'm resetting margins for some title elements, setting a better default line height, etc.

In the same `style.less` file I also define several utility CSS classes, and to distinguish them, I prefix their names with `u-`, for example I can apply `u-error` to any element displaying an error to make it red. 

Then, I have one `.scala` file and one `.less` file per component. For example, I have a `FormStateView` component in `FormStateView.scala`, and also `FormStateView.less` stylesheet for that component and its child elements. I assume that you understand [nesting](https://lesscss.org/#nesting) in LESS, and how the `>` [CSS child combinator](https://developer.mozilla.org/en-US/docs/Web/CSS/Child_combinator) makes the nesting declarations even more restrictive, reducing the leakage of this component's CSS declarations to child components that didn't ask for them.

In `.less` files, I distinguish components from other elements by giving them TitleCased class names. Components, for styling purposes, are standalone, self-contained elements. They aren't necessarily top-level elements in the DOM tree, for example `Tabs` is a component that is nested several layers deep inside other components.

Aside from components, there are also, simply, various child elements, that don't form a component on their own. For example, an `-inputRow` element in `FormStateView` – it does not stand on its own, it is an implementation detail of the `FormStateView`. Think of it as a "private method", even though nothing in CSS is private (Web Components notwithstanding).

The difference between Components and these internal elements is thus: each Component gets its own `.less` file, named after it. The internals of this component are not to be styled anywhere except this file, and any style definitions inside this file must be scoped appropriately to only affect this component, not its children, and not its siblings or parents. The styles for any internal elements like `-inputRow` must only be defined in the file of the component of which they are part of.

Parent components must not try to override their child components' internals, they can only set "positioning" / "layout" styles on the child components themselves, for example, like we do in `WeatherGradientView.less`.

Generally, if the parent component needs to modify the style of the child one, it should inform the child of that need by adding an `x-<variant>` CSS class to it, for example, we could add an `x-compact` class to the Tabs component, but crucially, we put the style declarations that respond to this class inside the component being styled, i.e. in `Tabs.less`, not in the parent component.

We use this `x-<variant>` naming convention whenever we want to change how something renders – think `x-selected`, `x-large`, etc. The same applies to internal elements, e.g. we add an `x-hasError` class to `-inputRow` elements in `FormStateView` when it has an error, and needs different coloring.

Lastly, as the `.less` files mirror the `.scala` component files, similar patterns are to be observed there, for example the component's scala file should be responsible for creating all of its internal elements like `-inputRow`.

You can develop this pattern further as you wish. This baseline gives us:
- Modular CSS / LESS files that can be loaded on-demand (using our vite plugins), conveniently co-located near the component's `.scala` files
- Reduced leakage of CSS rules from parent components to child components
- Easy to read component `.less` files with clear structure and limited scope

This isn't the one true CSS pattern, it's simply what I prefer myself, and I think is a good start for people who are new to frontend and CSS.

If my explanation / demonstration of this CSS styling pattern isn't very clear, please let me know.



### Scala 3

This project is written in Scala 3, but all the libraries that we use work with Scala 2.13 just as well, except the [Borer](https://sirthias.github.io/borer/) JSON library – for Scala 2 you will need a slightly older version of that one. Alternatively, you could use a different JSON library like [uPickle](https://com-lihaoyi.github.io/upickle/).

We use Scala 3 features relatively sparingly in this project. We use regular braced syntax. If you want to convert this project to Scala 2, you will need to do a few things:

* Add `new` to class instantiations that don't have them (the `new` keyword is optional in Scala 3)
* Replace extension methods with implicit value classes
* Replace `given`-s with `implicit val`-s or `def`-s
* Replace Scala 3 union types in JS interfaces with Scala.js specific alternative: `scala.scalajs.js.|`, and the `A | Unit` types specifically should be replaced with `scala.scalajs.js.UndefOr[A]`.
* For web components, use the `of` method instead of `apply`, as explained [here](https://github.com/sherpal/LaminarSAPUI5Bindings#remark-for-scala-213-users).
* Instead of exports in Shoelace web components, use boilerplate or inheritance
* Etc.

If you want to contribute a PR with Scala 2 support for this project, I am happy to host it in a `scala2` branch, however I will not be maintaining that branch going forward (but, even if outdated, it might still be useful to some people, thus the invitation).


## Not included


### ScalablyTyped

The idea of [ScalablyTyped](https://scalablytyped.org/) is appealing – it generates Scala type definitions for JS libs from their Typescript types. However, in practice I found that these generated types can be hard to grasp.

I prefer to manually create facades for JS libraries that I use. This is usually very easy – you read the library's docs, and create the types from the information therein, only covering the types and methods that you actually need or care about. You can always add more types later. And you can always make the types _more precise_ later – sometimes I leave stuff untyped (with `js.Dynamic` or `js.Dictionary` or `js.Object`) because I can't be bothered to create the elaborate types that would be needed to fully and faithfully describe some JS APIs.

I prefer this approach because I will be reading the JS library's docs anyway, and encoding my knowledge in Scala types is only a small, one-time speed bump (once you understand [how to do that](https://www.scala-js.org/doc/interoperability/facade-types.html)).

I also prefer this approach because the resulting code – including the imports – looks more similar to the library's native JS code, and so figuring out why something doesn't work is easier (because any help you'll find will be in JS, needing a translation to Scala.js).

If you want to give ScalablyTyped a shot, [installing it](https://scalablytyped.org/docs/plugin-no-bundler) is pretty easy, but make sure to read everything under [Usage](https://scalablytyped.org/docs/usage) so that you understand where you need to import the various types from. To speed things up, you should also configure `stIgnore` to avoid generating Scala types for JS dependencies that you will not be using from ScalablyTypes (at the very least that's UI5, Shoelace, and bootstrap icons in this repo).

As your first exercise, you can try reimplementing my Chart.js integration using ScalablyTyped. There are old commits in this repo with my attempts if you need a reference, although they're not complete. The [official Scala.js tutorial](https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example/) uses ScalablyTyped for Chart.js, although as of this writing, they're using an older version of Chart.js than what I'm using here, and the types have shifted around since then.


### Your Favourite Backend Web Framework

This repo focuses on the frontend, and the interop between frontend and backend. In this regard, the various backend frameworks aren't all that different. You should be able to swap out the http4s implementation for something else, just take care to set up the routes and JSON codecs similarly to how we do this with http4s, to maintain compatibility with our frontend.

Some of the old commits in this repo were using [Armeria](https://github.com/line/armeria) and [Javalin](https://javalin.io/), although I can't vouch for the quality of my implementations, since I've never used those frameworks before or since (my implementations weren't complete, either).


### Injecting A Scala.js App Into Server-Generated HTML

This repo focuses on the single page application (SPA) architecture (backend is just an API server, the HTML is all built on the frontend). If you would like to contribute an example of the server returning content in HTML, and loading the client scala.js app from _that_, talk to me about it on Laminar discord. It would be nice to show how to do this alongside the SPA architecture.


### Not Tested On Windows

I haven't tested this on Windows. I think it should work, but if you run into any Windows-specific problems, or if any setup instructions don't make sense for Windows, please let me know.


## Wishlist

I would like to:
* Expand this demo to include a cookbook of good Laminar patterns
* Showcase more Scala.js patterns, e.g. interop of Datetime types between frontend and backend
* Create integrations for more advanced browser features like Websockets
* Make good quality bindings for Shoelace web components ([WIP](https://github.com/raquo/laminar-shoelace-components))
* Etc. etc. etc.

All of that would be very nice, and would help the Laminar & Scala.js ecosystems a lot, I think. I'm not sure when I will ever get to doing all that, but if you want to speed it up, [you know what to do](https://github.com/sponsors/raquo/)! :). This repo itself was possible largely thanks to [HeartAI](https://heartai.net/)'s recent boost in their Laminar sponsorship. Things like that move the needle.


## Author

Nikita Gazarov – [@raquo](https://twitter.com/raquo)

Thanks to [Antoine](https://github.com/sherpal) for [FlyIOScalaJVMDemo](https://github.com/sherpal/FlyIOScalaJVMDemo) and [LaminarSAPUI5Bindings](https://github.com/sherpal/LaminarSAPUI5Bindings), from which this project borrows bits of code (primarily for the sbt-assembly and docker setup and some web component patterns).


## License

This repo is provided under the MIT license.

The logos and avatars of myself, Laminar, and Laminar sponsors are not covered by the MIT license. No license is granted to you for these assets. However, you may still have "fair use" rights, as stipulated by law.

The rocketship favicon file `favicon-32x32.png` file was generated from [Twitter Twemoji](https://github.com/twitter/twemoji/blob/master/assets/svg/1f680.svg), under [CC-BY 4.0](https://creativecommons.org/licenses/by/4.0/) license.
