#!/bin/sh

# Générer dynamiquement le fichier env.js
echo "window._env_ = { VITE_API_SECRET: \"${VITE_API_SECRET}\" };" > /usr/share/nginx/html/env.js

# Lancer Nginx
exec nginx -g "daemon off;"
