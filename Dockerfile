FROM eclipse-temurin:22-jre

RUN apt -y update && apt -y upgrade
RUN apt -y install \
    # For development and debug
    bash curl vim net-tools zip \
    && apt clean \
    && rm -rf /var/lib/apt/lists/*

ARG workdir="working_dir"
WORKDIR /${workdir}
