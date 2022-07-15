FROM jenkins-inbound-agent
RUN cat /proc/version
RUN echo “dash dash/sh boolean false” | debconf-set-selections
RUN DEBIAN_FRONTEND=noninteractive dpkg-reconfigure dash
RUN mkdir -p /temp
ADD obsutil /temp/obsutil
ADD setup.sh /temp/setup.sh
WORKDIR /temp
RUN sh ./setup.sh /temp/obsutil
RUN /obsutil/obsutil version
RUN /obsutil/obsutil config -i=xxxx -k=xxxx -e=http://0.0.0.0
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