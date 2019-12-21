# dev
docker run --name cpto-dapp-mysql \
    -p 3306:3306 -d --restart=always \
    -v /data/cpto-dapp-dev/docker/mysql/data:/var/lib/mysql \
    -v /data/cpto-dapp-dev/docker/mysql/conf:/etc/mysql/conf.d \
    -v /data/cpto-dapp-dev/docker/mysql/logs:/logs \
    -v /data/cpto-dapp-dev/docker/mysql/data:/var/lib/mysql \
    -e MYSQL_ROOT_PASSWORD=rt@FLH301 \
    swr.cn-north-1.myhuaweicloud.com/chaincat-base/mysql:5.6

# prod
docker run --name cpto-dapp-mysql \
    -p 3306:3306 -d --restart=always  \
    -v /data/cpto-dapp-prod/docker/mysql/conf:/etc/mysql/conf.d \
    -v /data/cpto-dapp-prod/docker/mysql/logs:/logs \
    -v /data/cpto-dapp-prod/docker/mysql/data:/var/lib/mysql \
    -e MYSQL_ROOT_PASSWORD=rt@FLH301 \
    swr.cn-north-1.myhuaweicloud.com/chaincat-base/mysql:5.6