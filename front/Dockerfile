FROM node:22.20.0-alpine3.22 AS build
WORKDIR /build
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build
FROM nginx:1.29.3-alpine3.22-slim AS release
COPY nginx.conf /etc/nginx/nginx.conf
COPY nginx.conf.template /etc/nginx/templates/default.conf.template
COPY --from=build /build/dist /usr/share/nginx/html
RUN chown -R nginx:nginx /var/cache/nginx
RUN chown -R nginx:nginx /etc/nginx/conf.d
USER nginx
EXPOSE 8000