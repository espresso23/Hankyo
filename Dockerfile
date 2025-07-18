FROM tomcat:9-jdk17

# Xóa ứng dụng mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file WAR đã build vào Tomcat
COPY target/Hankyo.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080 