import { defineConfig } from "vite";
import solidPlugin from "vite-plugin-solid";
import { resolve } from "path";

export default defineConfig({
  plugins: [solidPlugin()],
  server: {
    port: 3000,
    open: "/src/solidjs/index.html",
  },
  build: {
    rollupOptions: {
      input: {
        main: resolve(__dirname, "src/solidjs/index.html"),
        // nested: resolve(__dirname, "src/solidjs/nested.html"),
        // main: resolve(__dirname, "src/index.tsx"),
        // nested: resolve(__dirname, "src/nested/index.tsx"),
      },
      output: {
        // dir: resolve(__dirname, "assets/solid"),
        entryFileNames: "assets/solid/[name].js",
        chunkFileNames: "assets/solid/[name].js",
        assetFileNames: "assets/solid/[name].[ext]",
      },
    },
  },
  experimental: {
    renderBuiltUrl(filename) {
      return "/prj301-assignment/" + filename;
    },
  },
});
