FROM httpd:2-bookworm

# Setup working directory
WORKDIR /usr/local/apache2/htdocs/
WORKDIR /usr/local/apache2/htdocs/flamingo

# Install missing packages
RUN apt update && apt install -y php8.2 php8.2-mysql libapache2-mod-php8.2
RUN a2enmod php8.2

# Setup resources
# RUN chmod 777 /usr/local/apache2/htdocs/resources

# Run apache
EXPOSE 8080
COPY ./httpd.conf /usr/local/apache2/conf/httpd.conf
