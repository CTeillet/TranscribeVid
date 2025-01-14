# Étape 1 : Construire l'application avec Vite
FROM node:18-alpine AS build-stage

# Réduire la taille de l'image en n'installant que les outils nécessaires
RUN apk add --no-cache bash

# Définir le répertoire de travail
WORKDIR /app

# Copier uniquement les fichiers nécessaires pour l'installation des dépendances
COPY package*.json ./

# Installer uniquement les dépendances de production
RUN npm install --production

# Copier le reste des fichiers source
COPY . .

# Construire l'application
RUN npm run build

# Nettoyer les fichiers inutiles après le build
RUN rm -rf node_modules src public

# Étape 2 : Servir l'application avec une image NGINX légère
FROM nginx:stable-alpine AS production-stage

# Copier les fichiers construits dans le dossier NGINX par défaut
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Ajouter une configuration NGINX personnalisée pour gérer les routes (facultatif)
# COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exposer le port 80
EXPOSE 80

# Lancer NGINX
CMD ["nginx", "-g", "daemon off;"]
