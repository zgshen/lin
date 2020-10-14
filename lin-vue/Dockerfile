FROM nginx:1.19.0
COPY default.conf /etc/nginx/conf.d/
COPY dist /usr/share/nginx/html
RUN chown -R nginx:nginx /usr/share/nginx/html
