import { defineConfig } from 'vite';
import commonjs from '@rollup/plugin-commonjs';
import { resolve } from 'path';

export default defineConfig({
  resolve: {
    alias: {
      // ws is Node.js WebSocket, browsers use native WebSocket
      ws: resolve(__dirname, 'node_modules/isomorphic-ws/browser.js'),
    },
  },
  plugins: [
    commonjs({
      // Handle Kotlin/JS UMD modules
      include: [/node_modules/, /build\/dist\/js/],
      transformMixedEsModules: true,
    }),
  ],
  optimizeDeps: {
    include: ['@devmugi/scryfall-api'],
  },
  build: {
    commonjsOptions: {
      include: [/node_modules/, /build\/dist\/js/],
      transformMixedEsModules: true,
    },
  },
});
