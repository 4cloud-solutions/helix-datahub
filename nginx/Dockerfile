FROM nginx:1.17.8-alpine
COPY ./nginx.conf.template /nginx.conf.template
ENV BACKEND_SERVICE=localhost:9090
CMD ["/bin/sh" , "-c" , "envsubst '$BACKEND_SERVICE' < /nginx.conf.template > /etc/nginx/nginx.conf && exec nginx -g 'daemon off;'"]
