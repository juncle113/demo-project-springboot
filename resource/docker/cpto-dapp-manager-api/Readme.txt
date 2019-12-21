### dev
# 制作镜像
docker build -t swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-manager-api:dev.20190423.1 .

# 创建容器
docker run --name cpto-dapp-manager-api-dev-20190423-1 -p 8082:8082 -d --restart=always \
    swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-manager-api:dev.20190423.1

### prod
# 制作镜像
docker build -t swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-manager-api:prod.20190620.3 .

# 创建容器
docker run --name cpto-dapp-manager-api-prod-20190620-2 -p 8082:8082 -d --restart=always \
    -e "JAVA_OPTS=-Xms32m -Xmx64m -XX:+UseG1GC -XX:MaxGCPauseMillis=200" \
    swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-manager-api:prod.20190620.2

    docker run --name cpto-dapp-manager-api-prod-20190620-3 -p 8082:8082 -d --restart=always \
        -e "JAVA_OPTS=-Xms32m -Xmx64m -XX:+UseG1GC -XX:MaxGCPauseMillis=200" \
        swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-manager-api:prod.20190620.3