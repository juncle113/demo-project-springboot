### dev
# 制作镜像
docker build -t swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-web:dev-20190404.1 .

# 创建容器
docker run --name cpto-dapp-web-dev-20190404-1 -p 8084:80 -d --restart=always \
    swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-web:prod.20190404.1

### prod
# 制作镜像
docker build -t swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-web:prod.20190404.1 .

# 创建容器
docker run --name cpto-dapp-web-prod-20190404-1 -p 8084:80 -d --restart=always \
    swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-web:prod.20190404.1