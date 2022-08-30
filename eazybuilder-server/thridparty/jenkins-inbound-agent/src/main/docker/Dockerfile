FROM jenkins/inbound-agent:4.7-1

USER root
RUN sed -i 's/deb.debian.org/mirrors.ustc.edu.cn/g' /etc/apt/sources.list
RUN apt-get update 
RUN apt-get install -y wget ansible sshpass ant 

#install maven
RUN wget https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz
RUN tar -xvf apache-maven-3.5.4-bin.tar.gz && rm -f apache-maven-3.5.4-bin.tar.gz &&  mv apache-maven-3.5.4 /usr/share/maven
COPY settings.xml /usr/share/maven/conf/settings.xml
ENV PATH="/usr/share/maven/bin:${PATH}"

#install nodejs
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash - && apt-get install -y nodejs && npm config set registry https://registry.npm.taobao.org && \ 
  npm install -g nrm && npm install -g npm-cli-login && npm install -g yarn && nrm add eazybuilder http://0.0.0.0:4873/ && npm config set cache "/usr/share/maven-repo/npm-cache"

#instal gradle
RUN cd /opt && wget https://downloads.gradle-dn.com/distributions/gradle-6.8.3-bin.zip && unzip gradle-6.8.3-bin.zip -d /opt/gradle && rm -f /opt/gradle-6.8.3-bin.zip
ENV PATH="/opt/gradle/gradle-6.8.3/bin:${PATH}"

#install sonarscanner
RUN wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.6.0.2311.zip && unzip sonar-scanner-cli-4.6.0.2311.zip -d /usr/share/sonar-scanner
ENV PATH="/usr/share/sonar-scanner/sonar-scanner-4.6.0.2311/bin:${PATH}"

#install subversion
RUN apt-get install -y subversion
#install zh-CN lang pack
RUN apt-get install -y locales && localedef -c -f UTF-8 -i zh_CN zh_CN.utf8

#copy ci files
COPY ansible.cfg /etc/ansible/ansible.cfg
COPY push-war.yml /opt/push-war.yml
ENV ANSIBLE_HOST_KEY_CHECKING="False"
RUN mkdir -p /opt/ci-tool/lib
COPY ci-tool/lib/*.jar  /opt/ci-tool/lib/

ENV LANG="zh_CN.utf-8"
ENV LC_ALL="zh_CN.utf-8"

RUN cat /proc/version
RUN echo “dash dash/sh boolean false” | debconf-set-selections
RUN DEBIAN_FRONTEND=noninteractive dpkg-reconfigure dash
RUN mkdir -p /temp
ADD obsutil /temp/obsutil
ADD setup.sh /temp/setup.sh
WORKDIR /temp
RUN sh ./setup.sh /temp/obsutil
RUN /obsutil/obsutil version
RUN /obsutil/obsutil config -i=BNR0DD7MXUGZYU5V6YWW -k=L6iB7XjNrSjFJxzjCwJj1NGT7UxoG20IYM9Be0L6 -e=http://0.0.0.0:30081
ADD .git-credentials /root/
RUN git config --global credential.helper store
COPY settings.xml /usr/share/maven/conf/settings.xml
#ADD openjdk-11.0.2_linux-x64_bin.tar.gz /usr/java/jdk/jdk-11.0.2
#RUN chmod 777 /usr/java/jdk/jdk-11.0.2
#ENV JAVA_HOME="/usr/java/jdk/jdk-11.0.2/"


USER root

RUN lsb_release -a

RUN apt install -y apt-transport-https dirmngr gnupg ca-certificates
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 3FA7E0328081BFF6A14DA29AA6A19B38D3D831EF
RUN echo "deb https://download.mono-project.com/repo/debian stable-buster main" | tee /etc/apt/sources.list.d/mono-official-stable.list
RUN apt --allow-releaseinfo-change update
RUN apt install mono-devel -y

RUN wget -qO- https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.asc.gpg
RUN mv microsoft.asc.gpg /etc/apt/trusted.gpg.d/
RUN wget -q https://packages.microsoft.com/config/debian/10/prod.list
RUN mv prod.list /etc/apt/sources.list.d/microsoft-prod.list
RUN chown root:root /etc/apt/trusted.gpg.d/microsoft.asc.gpg
RUN chown root:root /etc/apt/sources.list.d/microsoft-prod.list
RUN apt --allow-releaseinfo-change update
RUN apt install apt-transport-https -y
RUN apt-get install dotnet-sdk-3.1 -y
RUN apt install nuget -y
RUN dotnet tool install --global dotnet-sonarscanner
RUN dotnet tool install --global coverlet.console --version 1.3.0


#ADD mono-6.12.0.122.tar.xz /usr/bin/mono-6.12.0.122.tar.xz
#RUN cd /usr/bin && tar -xvJf mono-6.12.0.122.tar.xz
#RUN cd mono-6.12.0.122
#RUN ./configure --prefix=/usr/local/mono
#RUN make
#RUN make install
#RUN ln -s /usr/local/mono/bin/mono-sgen /usr/bin/mono