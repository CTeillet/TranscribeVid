import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import compress from 'vite-plugin-compression';
import tailwindcss from "@tailwindcss/vite";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        compress({
            algorithm: 'gzip', // Ou 'brotliCompress' pour Brotli
        }),
        tailwindcss(),
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    build: {
        target: 'esnext', // Support moderne pour les navigateurs récents
        minify: 'terser', // Utilise Terser pour un minification avancée
        sourcemap: false, // Désactive les sourcemaps pour réduire la taille
        rollupOptions: {
            output: {
                manualChunks: undefined, // Tout regrouper si nécessaire
            },
        },
    },
})
