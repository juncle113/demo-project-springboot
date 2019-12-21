### dev
# 制作镜像
docker build -t swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-manager-web:dev-20190417.1 .

# 创建容器
docker run --name cpto-dapp-manager-web-dev-20190417-1 -p 8083:80 -d --restart=always \
    swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-manager-web:prod.20190417.1

### prod
# 制作镜像
docker build -t swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-manager-web:prod.20190612.1 .

# 创建容器
docker run --name cpto-dapp-manager-web-prod-20190612-1 -p 8083:80 -d --restart=always \
    swr.cn-north-1.myhuaweicloud.com/chaincat-other/cpto-dapp-manager-web:prod.20190612.1